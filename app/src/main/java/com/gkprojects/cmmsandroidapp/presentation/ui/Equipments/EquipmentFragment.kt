package com.gkprojects.cmmsandroidapp.presentation.ui.Equipments


import ConfirmDeleteDialog
import android.annotation.SuppressLint
import android.content.Context

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.gkprojects.cmmsandroidapp.presentation.adapter.EquipmentAdapter
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentSelectCustomerName

import com.gkprojects.cmmsandroidapp.data.local.entity.Equipments

import com.gkprojects.cmmsandroidapp.presentation.viewmodel.EquipmentVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.EquipmentViewModelFactory

import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.core.utils.FilterType.DateRangeFilter
import com.gkprojects.cmmsandroidapp.core.utils.FilterType.ListFilter
import com.gkprojects.cmmsandroidapp.core.utils.FilterType.StatusFilter
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult
import com.gkprojects.cmmsandroidapp.core.utils.UiOperationMessages
import com.gkprojects.cmmsandroidapp.data.local.entity.Customer
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoEquipment
import com.gkprojects.cmmsandroidapp.databinding.FragmentEquipmentBinding
import com.gkprojects.cmmsandroidapp.presentation.common.ModalFilterWindow

import com.gkprojects.cmmsandroidapp.presentation.common.filterPopWindow
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.*




import java.util.*
import kotlin.collections.ArrayList


class EquipmentFragment : Fragment() {
    private lateinit var equipmentRecyclerView: RecyclerView
    @SuppressLint("StaticFieldLeak")
    private lateinit var equipmentAdapter: EquipmentAdapter
    private lateinit var equipmentViewModel: EquipmentVM

    private var equipmentList = ArrayList<EquipmentSelectCustomerName>()
    private var filteredEquipmentList =ArrayList<EquipmentSelectCustomerName>()

    private var lastFilterValues: Map<String, Any?> = emptyMap()

    private var uniqueCustomerName :List<String> = emptyList()
    private var uniqueCategory :List<String> = emptyList()
    private var uniqueModel :List<String> = emptyList()
    private var uniqueManu :List<String> = emptyList()
    private var customerList : ArrayList<String> = ArrayList()
    private var equipmentCatList : ArrayList<String> = ArrayList()
    private var modelList : ArrayList<String> = ArrayList()
    private var manufacturerList : ArrayList<String> = ArrayList()
    private var selectedRadioButtonId: Int = R.id.equipmentStatusAll
    private var equipmentStatusRadio : Boolean? = null
    private lateinit var binding: FragmentEquipmentBinding
    private lateinit var filterWindow : filterPopWindow


    override fun onResume() {
        super.onResume()
        val activity =requireActivity()

        val drawerLayout = activity.findViewById<DrawerLayout>(R.id.DrawLayout)
        //val navView: NavigationView = activity.findViewById(R.id.navView)
        val toolbar: MaterialToolbar = activity.findViewById(R.id.main_toolbar)
        //toolbar.title="Equipments"
        val drawable = ContextCompat.getDrawable(activity, R.drawable.menu_svgrepo_com)
        toolbar.navigationIcon = drawable
        val toggle = ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }


    @SuppressLint("SuspiciousIndentation", "UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        equipmentRecyclerView=view.findViewById(R.id.equipment_recyclerview)
        equipmentAdapter= this.context?.let { EquipmentAdapter(it, ArrayList<EquipmentSelectCustomerName>()) }!!
        equipmentRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this.context)
            adapter= equipmentAdapter
        }
        equipmentViewModel= ViewModelProvider(this)[EquipmentVM::class.java]


        try{
            lifecycleScope.launch {
                withContext(Dispatchers.Main){
                     equipmentViewModel.getCustomerName{ list->
                            equipmentList.clear()
                            equipmentList=list as ArrayList<EquipmentSelectCustomerName>

                            equipmentAdapter.setData(list)
                            uniqueModel=equipmentList.map{it.Model.toString()}.distinct()
                            uniqueManu=equipmentList.map{it.Manufacturer.toString()}.distinct()
                            uniqueCategory=equipmentList.map{it.EquipmentCategory.toString()}.distinct()
                            uniqueCustomerName=equipmentList.map { it.CustomerName.toString() }.distinct()
                        }
                }
            }

            reloadData(context!!)

        }catch (e:java.lang.Exception){
            Log.d("debugE",e.toString())
        }


        val searchBar = binding.searchEditTextEquipment
        binding.filterIcon.setOnClickListener {
            //filterDialog()
            showFilterModal()
        }
        val searchIcon=binding.searchIcon
        searchIcon.setOnClickListener {
            searchBar.text.clear()
        }




        searchBar.addTextChangedListener (object :TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //searchBar.hint=""

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchBar.hint=""
                if (s != null) {


                    filterList(s.toString().lowercase(Locale.ROOT))
                    searchIcon.setImageResource(R.drawable.search_cancel_clear)
                }
                else{

                    searchIcon.setImageResource(R.drawable.search_icon)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        equipmentAdapter.setOnClickListener(object : EquipmentAdapter.OnClickListener{
            override fun onClick(position: Int, model: EquipmentSelectCustomerName) {

                //Toast.makeText(context,model.toString(),Toast.LENGTH_LONG).show()
                passDataEquipment(model)


            }
        })
        equipmentAdapter.setOnLongClickListener(object : EquipmentAdapter.OnLongClickListener {
            override fun onLongClick(position: Int, model: EquipmentSelectCustomerName) {

                ConfirmDeleteDialog(
                    message = "Delete ${model.Model} - ${model.SerialNumber}?",
                    onConfirm = {
                        equipmentViewModel.deleteEquipmentById(
                            context = requireContext(),
                            id = model.EquipmentID!!,
                        ) { result ->
                            UiOperationMessages.handleResult(
                                context = requireContext(),
                                result = result,
                                rootView = binding.root,
                                successMessage = "Equipment deleted successfully"
                            )
                            if (result is OperationResult.Success){
                                val updatedList = ArrayList(equipmentList) // make a copy
                                updatedList.remove(model) // remove the deleted item
                                equipmentAdapter.setData(updatedList)


                            }
                        }
                    }
                ).show(childFragmentManager, "confirmDeleteEquipment")

            }
        })



    val btnFloat=view.findViewById<FloatingActionButton>(R.id.openEquipmentFragment)
        btnFloat.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout1, EquipmentInsertFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }


    }
    private fun filterList(query:String){
        val filteredList= java.util.ArrayList<EquipmentSelectCustomerName>()
        for (i in equipmentList){
            if((i.Model?.lowercase(Locale.ROOT)?.contains(query)==true) or (i.SerialNumber?.lowercase(Locale.ROOT)?.contains(query) == true)or(i.CustomerName?.lowercase(Locale.ROOT)?.contains(query) == true))
                filteredList.add(i)

        }
        if (filteredList.isEmpty() ){
            Toast.makeText(context,"Empty List", Toast.LENGTH_SHORT).show()

        }else{
            equipmentAdapter.setData(filteredList)
        }


    }







    private fun passDataEquipment(data : EquipmentSelectCustomerName){
        //var temp: java.io.Serializable = data as java.io.Serializable
        val bundle = Bundle()
        //bundle.putString("")=data.EquipmentID
        data.EquipmentID?.let { bundle.putString("EquipmentId", it) }
        val fragmentManager =parentFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        val fragment = EquipmentInsertFragment()
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.frameLayout1,fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }

    private fun reloadData(context: Context){
        lifecycleScope.launch(Dispatchers.Main){
            equipmentViewModel.getCustomerName{list->
                equipmentList.clear()
                equipmentList=list as ArrayList<EquipmentSelectCustomerName>

                equipmentAdapter.setData(list )


            }
        }


    }

    private fun showFilterModal() {
        val customerNames =equipmentList
            .mapNotNull { it.CustomerName }       // Get names, ignore nulls
            .filter { it.isNotBlank() }           // Remove empty strings
            .distinct()                           // Keep unique names
            .sorted()

        val models = equipmentList
            .mapNotNull { it.Model }       // Get names, ignore nulls
            .filter { it.isNotBlank() }           // Remove empty strings
            .distinct()                           // Keep unique names
            .sorted()
        val serialNumbers=equipmentList
            .mapNotNull { it.SerialNumber }
            .distinct()
            .sorted()
        val manufacturer =equipmentList
            .mapNotNull { it.Manufacturer }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
        val category =equipmentList
            .mapNotNull { it.EquipmentCategory }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()


        val filters = listOf(
            ListFilter("name", customerNames),
            ListFilter("models", models),
            ListFilter("serialNumbers", serialNumbers),
            ListFilter("category", category),
            ListFilter("manufacturer", manufacturer),
            DateRangeFilter("InstallationDate"),
            StatusFilter("status")


        )


        val filterModal = ModalFilterWindow(filters, { result ->
            if(result.values.isEmpty()) { equipmentAdapter.setData(filteredEquipmentList) }
            else {
                filteredEquipmentList = equipmentList

                lastFilterValues = result.values // 👈 save for next time

                filteredEquipmentList = equipmentList.filter { equipment ->
                    result.values.all { (key, value) ->
                        when (key) {
                            // Text-based filters
                            "name", "models", "serialNumbers", "category", "manufacturer" -> {
                                when (value) {
                                    is Set<*> -> value.any { v ->
                                        equipment.getProperty(key)
                                            ?.contains(v.toString(), ignoreCase = true) == true
                                    }
                                    is String -> equipment.getProperty(key)
                                        ?.contains(value, ignoreCase = true) == true
                                    else -> true
                                }
                            }

                            // Boolean filter
                            "status" -> {
                                when (value) {
                                    is Boolean -> equipment.EquipmentStatus == value
                                    else -> true
                                }
                            }

                            // ✅ Date range filtering
                            "InstallationDate_from", "InstallationDate_to" -> {
                                val fromStr = result.values["InstallationDate_from"] as? String
                                val toStr = result.values["InstallationDate_to"] as? String
                                Log.d("debugE", "fromStr: $fromStr, toStr: $toStr")

                                val equipmentDateStr = equipment.InstallationDate
                                Log.d("debugE", "equipmentDateStr: $equipmentDateStr")
                                //val equipmentDate = DateUtils.parse(equipmentDateStr ?: "")
                                val normalizedDateStr = DateUtils.normalize(equipmentDateStr ?: "")
                                Log.d("debugE", "normalizedDateStr: $normalizedDateStr")
                                val equipmentDate = normalizedDateStr?.let { DateUtils.parse(it) }
                                Log.d("debugE", "equipmentDate: $equipmentDate")

                                // If parsing fails, keep it visible
                                if (equipmentDate == null) return@all true

                                val fromDate = fromStr?.let { DateUtils.parse(it) }
                                val toDate = toStr?.let { DateUtils.parse(it) }
                                Log.d("debugE", "fromDate: $fromDate, toDate: $toDate")

                                when {
                                    fromDate != null && toDate != null -> equipmentDate >= fromDate && equipmentDate <= toDate
                                    fromDate != null -> equipmentDate >= fromDate
                                    toDate != null -> equipmentDate <= toDate
                                    else -> true
                                }
                            }

                            else -> true
                        }
                    }
                }.toCollection(ArrayList())
                equipmentAdapter.setData(filteredEquipmentList)
            }
            //contractViewModel.applyFilters(result)
            //contractsViewModel.applyContractFilters(result.values)
        }, lastFilterValues)



        filterModal.show(parentFragmentManager, "CustomerFilterModal")
    }
    private fun EquipmentSelectCustomerName.getProperty(key: String): String? = when (key) {
        "name" -> this.CustomerName
        "category" -> this.EquipmentCategory
        "manufacturer" -> this.Manufacturer
        "models"->this.Model
        "serialNumbers" -> this.SerialNumber
        else -> null
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentEquipmentBinding.inflate(layoutInflater,container,false)
        val context = requireContext()
        val repositoryEquipment = RepoEquipment.getInstance(context)
        val factoryEquipmentVM = EquipmentViewModelFactory(repositoryEquipment)
        equipmentViewModel= ViewModelProvider(this,factoryEquipmentVM)[EquipmentVM::class.java]


        return binding.root
    }



}