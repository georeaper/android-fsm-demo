package com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders

import ConfirmDeleteDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat

import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.presentation.adapter.WorkOrdersAdapter
import com.gkprojects.cmmsandroidapp.data.local.dto.WorkOrdersList
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.FieldReportVMFactory

import com.gkprojects.cmmsandroidapp.presentation.viewmodel.WorkOrdersVM
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.core.utils.FilterType.DateRangeFilter
import com.gkprojects.cmmsandroidapp.core.utils.FilterType.ListFilter
import com.gkprojects.cmmsandroidapp.core.utils.FilterType.StatusFilter
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult
import com.gkprojects.cmmsandroidapp.core.utils.UiOperationMessages
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentSelectCustomerName
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrders
import com.gkprojects.cmmsandroidapp.databinding.FragmentWorkOrdersBinding
import com.gkprojects.cmmsandroidapp.presentation.common.ModalFilterWindow
import com.gkprojects.cmmsandroidapp.presentation.common.filterPopWindow
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale


class WorkOrdersFragment : Fragment() {
    private lateinit var binding: FragmentWorkOrdersBinding
    private lateinit var recyclerViewWorkOrder: RecyclerView
    private lateinit var adapterWorkOrder: WorkOrdersAdapter
    private lateinit var searchViewWorkOrder: TextInputEditText
    private lateinit var workOrderViewModel: WorkOrdersVM
    private var workOrdersList = ArrayList<WorkOrdersList>()
    private var filteredWorkOrderList = ArrayList<WorkOrdersList>()


    private var lastFilterValues: Map<String, Any?> = emptyMap()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onResume() {
        super.onResume()
        val activity =requireActivity()

        val drawerLayout = activity.findViewById<DrawerLayout>(R.id.DrawLayout)
        val navView: NavigationView = activity.findViewById(R.id.navView)
        val toolbar: MaterialToolbar = activity.findViewById(R.id.main_toolbar)

        val drawable = ContextCompat.getDrawable(activity, R.drawable.menu_svgrepo_com)
        toolbar.navigationIcon = drawable

        val toggle = ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkOrdersBinding.inflate(inflater, container, false)

        val context = requireContext()
        val workOrderRepository = RepoWorkOrders.getInstance(context)
        val workOrderFactory = FieldReportVMFactory(workOrderRepository)
        workOrderViewModel= ViewModelProvider(this,workOrderFactory)[WorkOrdersVM::class.java]
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sharedViewModel: SharedViewModel by activityViewModels()
        super.onViewCreated(view, savedInstanceState)
        recyclerViewWorkOrder=binding.workOrdersRecyclerView
        adapterWorkOrder= WorkOrdersAdapter(workOrdersList)
        recyclerViewWorkOrder.apply {
            setHasFixedSize(true)
            layoutManager= LinearLayoutManager(this.context)
            adapter= adapterWorkOrder
        }


        workOrderViewModel.getWorkOrdersCustomerName().observe(viewLifecycleOwner)
             {
                workOrdersList=it as ArrayList<WorkOrdersList>
                adapterWorkOrder.setData(workOrdersList)
            }
        val searchBar = binding.searchEditTextWorkOrder
        binding.filterIcon.setOnClickListener {
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
                if (s?.isNotEmpty() == true) {

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





        adapterWorkOrder.setOnClickListener(object : WorkOrdersAdapter.OnClickListener{
            override fun onClick(position: Int, model: WorkOrdersList) {

                val bundle = Bundle()
                bundle.putString("reportId", model.workOrderID!!)
                val fragmentManager =parentFragmentManager
                val fragmentTransaction=fragmentManager.beginTransaction()
                val fragment = WorkOrdersInsertFragment()
                fragment.arguments = bundle
                fragmentTransaction.replace(R.id.frameLayout1,fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                //passData(model.workOrderID)
            }

        })
        adapterWorkOrder.setOnLongClickListener(object :WorkOrdersAdapter.OnLongClickListener{
            override fun onLongClick(position: Int, model: WorkOrdersList) {
                ConfirmDeleteDialog(
                    message = "Delete ${model.reportNumber} - ${model.customerName}?",
                    onConfirm = {
                        workOrderViewModel.delete(
                            id = model.workOrderID!!,
                        ) { result ->
                            UiOperationMessages.handleResult(
                                context = requireContext(),
                                result = result,
                                rootView = binding.root,
                                successMessage = "Work order deleted successfully"
                            )
                            if (result is OperationResult.Success){
//                                val updatedList = ArrayList(workOrdersList) // make a copy
//                                updatedList.remove(model) // remove the deleted item
//                                adapterWorkOrder.setData(updatedList)
                                adapterWorkOrder.removeItemAt(position)



                            }
                        }
                    }
                ).show(childFragmentManager, "confirmDeleteWorkOrder")
            }

        })


        val btnFloat= binding.workOrdersFloatButton

        btnFloat.setOnClickListener {
            val fragmentManager =parentFragmentManager
            val fragmentTransaction=fragmentManager.beginTransaction()
            val fragment = WorkOrdersInsertFragment()
            fragmentTransaction.replace(R.id.frameLayout1,fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }

    private fun showFilterModal() {
        val customerNames = workOrdersList
            .mapNotNull { it.customerName }       // Get names, ignore nulls
            .filter { it.isNotBlank() }           // Remove empty strings
            .distinct()                           // Keep unique names
            .sorted()

        val reportNumber = workOrdersList
            .mapNotNull { it.reportNumber }       // Get names, ignore nulls
            .filter { it.isNotBlank() }           // Remove empty strings
            .distinct()                           // Keep unique names
            .sorted()



        val filters = listOf(
            ListFilter("name", customerNames),
            ListFilter("reportNumber", reportNumber),

            DateRangeFilter("dateOpened"),
            DateRangeFilter("dateClosed"),
            StatusFilter("status")


        )


        val filterModal = ModalFilterWindow(filters, { result ->
            if(result.values.isEmpty()) { adapterWorkOrder.setData(filteredWorkOrderList) }
            else {
                filteredWorkOrderList = workOrdersList

                lastFilterValues = result.values // 👈 save for next time

                filteredWorkOrderList = workOrdersList.filter { workOrder ->
                    result.values.all { (key, value) ->
                        when (key) {
                            // Text-based filters
                            "name", "reportNumber" -> {
                                when (value) {
                                    is Set<*> -> value.any { v ->
                                        workOrder.getProperty(key)
                                            ?.contains(v.toString(), ignoreCase = true) == true
                                    }
                                    is String -> workOrder.getProperty(key)
                                        ?.contains(value, ignoreCase = true) == true
                                    else -> true
                                }
                            }

                            // Boolean filter
                            "status" -> {
                                when (value) {
                                    is Boolean -> workOrder.status == value
                                    else -> true
                                }
                            }

                            // ✅ Date range filtering
                            "dateOpened_from", "dateOpened_to" -> {
                                val fromStr = result.values["dateOpened_from"] as? String
                                val toStr = result.values["dateOpened_to"] as? String
                                Log.d("debugE", "fromStr: $fromStr, toStr: $toStr")

                                val workOrderOpenDateStr = workOrder.dateOpened
                                Log.d("debugE", "equipmentDateStr: $workOrderOpenDateStr")
                                //val equipmentDate = DateUtils.parse(equipmentDateStr ?: "")
                                val normalizedDateStr = DateUtils.normalize(workOrderOpenDateStr ?: "")
                                Log.d("debugE", "normalizedDateStr: $normalizedDateStr")
                                val workOrderOpenDate = normalizedDateStr?.let { DateUtils.parse(it) }
                                Log.d("debugE", "equipmentDate: $workOrderOpenDate")

                                // If parsing fails, keep it visible
                                if (workOrderOpenDate == null) return@all true

                                val fromDate = fromStr?.let { DateUtils.parse(it) }
                                val toDate = toStr?.let { DateUtils.parse(it) }
                                Log.d("debugE", "fromDate: $fromDate, toDate: $toDate")

                                when {
                                    fromDate != null && toDate != null -> workOrderOpenDate >= fromDate && workOrderOpenDate <= toDate
                                    fromDate != null -> workOrderOpenDate >= fromDate
                                    toDate != null -> workOrderOpenDate <= toDate
                                    else -> true
                                }
                            }
                            "dateClosed_from", "dateClosed_to" -> {
                                val fromStr = result.values["dateClosed_from"] as? String
                                val toStr = result.values["dateClosed_to"] as? String
                                Log.d("debugE", "fromStr: $fromStr, toStr: $toStr")

                                val workOrderCloseDateStr = workOrder.dateClosed
                                Log.d("debugE", "equipmentDateStr: $workOrderCloseDateStr")
                                //val equipmentDate = DateUtils.parse(equipmentDateStr ?: "")
                                val normalizedDateStr = DateUtils.normalize(workOrderCloseDateStr ?: "")
                                Log.d("debugE", "normalizedDateStr: $normalizedDateStr")
                                val workOrderCloseDate = normalizedDateStr?.let { DateUtils.parse(it) }
                                Log.d("debugE", "equipmentDate: $workOrderCloseDate")

                                // If parsing fails, keep it visible
                                if (workOrderCloseDate == null) return@all true

                                val fromDate = fromStr?.let { DateUtils.parse(it) }
                                val toDate = toStr?.let { DateUtils.parse(it) }
                                Log.d("debugE", "fromDate: $fromDate, toDate: $toDate")

                                when {
                                    fromDate != null && toDate != null -> workOrderCloseDate >= fromDate && workOrderCloseDate <= toDate
                                    fromDate != null -> workOrderCloseDate >= fromDate
                                    toDate != null -> workOrderCloseDate <= toDate
                                    else -> true
                                }
                            }

                            else -> true
                        }
                    }
                }.toCollection(ArrayList())
                adapterWorkOrder.setData(filteredWorkOrderList)
            }
            //contractViewModel.applyFilters(result)
            //contractsViewModel.applyContractFilters(result.values)
        }, lastFilterValues)



        filterModal.show(parentFragmentManager, "CustomerFilterModal")
    }
    private fun WorkOrdersList.getProperty(key: String): String? = when (key) {
        "name" -> this.customerName
        "reportNumber" -> this.reportNumber
        else -> null
    }


    private fun filterList(query:String){
        val filteredList= java.util.ArrayList<WorkOrdersList>()
        for (i in workOrdersList){
            if((i.customerName?.lowercase(Locale.ROOT)?.contains(query)==true) or (i.reportNumber?.lowercase(Locale.ROOT)?.contains(query) == true)or(i.dateOpened?.lowercase(Locale.ROOT)?.contains(query) == true))

                filteredList.add(i)
            Log.d("dataEquipment", filteredList.toString())
        }
        if (filteredList.isEmpty() ){
            Toast.makeText(context,"Empty List", Toast.LENGTH_SHORT).show()

        }else{
            adapterWorkOrder.setData(filteredList)
        }


    }


}