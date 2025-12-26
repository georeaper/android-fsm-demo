package com.gkprojects.cmmsandroidapp.presentation.ui.Customers


import ConfirmDeleteDialog
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListPopupWindow

import android.widget.RadioGroup

import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat

import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.presentation.adapter.CustomerAdapter
import com.gkprojects.cmmsandroidapp.presentation.adapter.FilterCustomer
import com.gkprojects.cmmsandroidapp.presentation.adapter.FilterCustomerAdapter
import com.gkprojects.cmmsandroidapp.data.local.entity.Customer

import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CustomerVM
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.core.utils.FilterType.DateRangeFilter
import com.gkprojects.cmmsandroidapp.core.utils.FilterType.ListFilter
import com.gkprojects.cmmsandroidapp.core.utils.FilterType.StatusFilter
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult
import com.gkprojects.cmmsandroidapp.core.utils.UiOperationMessages
import com.gkprojects.cmmsandroidapp.databinding.FragmentCustomerBinding
import com.gkprojects.cmmsandroidapp.presentation.common.ModalFilterWindow
import com.gkprojects.cmmsandroidapp.presentation.common.filterPopWindow
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.MaterialAutoCompleteTextView

import java.util.*
import kotlin.collections.ArrayList


class CustomerFragment : Fragment() {
    private lateinit var customerRecyclerView: RecyclerView
    private lateinit var binding: FragmentCustomerBinding

    private var selectedRadioButtonId: Int = R.id.radioCustomerButtonAll

    private var customerList =ArrayList<Customer>()
    private var filteredCustomerList =ArrayList<Customer>()

    private lateinit var customerAdapter: CustomerAdapter
    private lateinit var customerViewModel: CustomerVM
    private lateinit var bottomSheetFragment : filterPopWindow

    private var lastFilterValues: Map<String, Any?> = emptyMap()

    private lateinit var adapterFilter: FilterCustomerAdapter
    private lateinit var filterItems: List<FilterCustomer>
    private lateinit var listPopupWindow: ListPopupWindow



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentCustomerBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment


        return binding.root
    }
    override fun onResume() {
        super.onResume()
        val activity =requireActivity()

        val drawerLayout = activity.findViewById<DrawerLayout>(R.id.DrawLayout)

        val toolbar: MaterialToolbar = activity.findViewById(R.id.main_toolbar)
        //toolbar.title="Customer"
        val drawable = ContextCompat.getDrawable(activity, R.drawable.menu_svgrepo_com)
        toolbar.navigationIcon = drawable


        val toggle = ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

    }


    @SuppressLint("UseRequireInsteadOfGet", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customerRecyclerView = view.findViewById(R.id.customer_recyclerview)
        customerAdapter = this.context?.let { CustomerAdapter(it, ArrayList()) }!!


        customerRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = customerAdapter
        }
        customerViewModel = ViewModelProvider(this)[CustomerVM::class.java]


            customerViewModel.getAllCustomerData(requireContext()).observe(viewLifecycleOwner)
                { list->
                    customerList.clear()
                    customerList=list as ArrayList<Customer>
                    customerAdapter.setData(customerList)
                    filterItems=customerList.map {
                        FilterCustomer(it.CustomerID,it.Name!!,it.Address!!)
                    }

            }


        val searchBar = binding.searchEditTextCustomer
        binding.filterIcon.setOnClickListener {
            //filterDialog()
            showFilterModal()
        }
        val searchIcon=binding.searchIcon

        searchIcon.setOnClickListener {
            searchBar.text.clear()
        }
        searchBar.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

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


        val openbtn =view.findViewById<FloatingActionButton>(R.id.openCustomerFragment)

        customerAdapter.setOnClickListener(object : CustomerAdapter.OnClickListener{
            override fun onClick(position: Int, model: Customer) {
//                var temp: java.io.Serializable = model as java.io.Serializable
                //Toast.makeText(context,model.toString(),Toast.LENGTH_LONG).show()
                passDataCustomer(model)


            }
        })

        customerAdapter.setOnLongClickListener(object :CustomerAdapter.OnLongClickListener{
            override fun onLongClick(position: Int, model: Customer) {
                ConfirmDeleteDialog(
                    message = "Delete ${model.Name} - ${model.Address}?",
                    onConfirm = {

                        customerViewModel.deleteCustomer(
                            requireContext(),
                            model ){ result ->
                            UiOperationMessages.handleResult(
                                context = requireContext(),
                                result = result,
                                rootView = binding.root,
                                successMessage = "Equipment deleted successfully"
                            )
                            if (result is OperationResult.Success){
                                val updatedList = ArrayList(customerList) // make a copy
                                updatedList.remove(model) // remove the deleted item
                                customerAdapter.setData(updatedList)


                            }
                        }
                    }
                ).show(childFragmentManager, "confirmDeleteEquipment")
            }

        })

openbtn?.setOnClickListener {


            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout1, EditCustomerFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }


    }




    private fun filterList(query:String){
     if (query.isNotEmpty()){
        val filteredList= ArrayList<Customer>()
         for (i in customerList){
             if (i.Name?.lowercase(Locale.ROOT)?.contains(query)==true)
                 filteredList.add(i)
             Log.d("dataCustomer", filteredList.toString())
         }
         if (filteredList.isEmpty() ){
            Toast.makeText(context,"Empty List",Toast.LENGTH_SHORT).show()

         }else{
             customerAdapter.setData(filteredList)
         }
     }


 }


    private fun passDataCustomer(data : Customer){

     val bundle = Bundle()
     data.CustomerID.let { bundle.putString("id", it) }

     bundle.putString("name", data.Name)
     bundle.putString("address", data.Address)
     bundle.putString("email", data.Email)
     bundle.putString("phone", data.Phone)

     Log.d("bundlecheck",bundle.toString())

        if (bundle.isEmpty){
            Toast.makeText(context,"Bundle is Null",Toast.LENGTH_SHORT).show()

        }else {

            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = EditCustomerFragment()
            fragment.arguments = bundle
            fragmentTransaction.replace(R.id.frameLayout1, fragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }

    }

    private fun showFilterModal() {
        val customerNames = customerList
            .mapNotNull { it.Name }       // Get names, ignore nulls
            .filter { it.isNotBlank() }           // Remove empty strings
            .distinct()                           // Keep unique names
            .sorted()
        val zipCode =customerList
            .mapNotNull { it.ZipCode }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
        val city =customerList
            .mapNotNull { it.City }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()

        val filters = listOf(
            ListFilter("name", customerNames),
            ListFilter("city", city),
            ListFilter("zipCode", zipCode),
            StatusFilter("status")


        )


        val filterModal = ModalFilterWindow(filters, { result ->
            if(result.values.isEmpty()) { customerAdapter.setData(filteredCustomerList) }
            else {
                filteredCustomerList = customerList

                lastFilterValues = result.values // 👈 save for next time
                filteredCustomerList = customerList.filter { customer ->
                    result.values.all { (key, value) ->
                        when (key) {
                            "name", "city", "zipCode" -> {
                                when (value) {
                                    is Set<*> -> value.any { v ->
                                        customer.getProperty(key)
                                            ?.contains(v.toString(), ignoreCase = true) == true
                                    }

                                    is String -> customer.getProperty(key)
                                        ?.contains(value, ignoreCase = true) == true

                                    else -> true // ignore unknown type
                                }
                            }

                            "status" -> {
                                when (value) {
                                    is Boolean -> customer.CustomerStatus == value
                                    else -> true
                                }
                            }
                            "InstallationDate"->{
                                when (value){
                                    is String -> customer.getProperty(key)?.contains(value) == true
                                    else -> true
                                }
                            }

                            else -> true
                        }
                    }
                }.toCollection(ArrayList())
                customerAdapter.setData(filteredCustomerList)
            }
            //contractViewModel.applyFilters(result)
            //contractsViewModel.applyContractFilters(result.values)
        }, lastFilterValues)



        filterModal.show(parentFragmentManager, "CustomerFilterModal")
    }

    private fun Customer.getProperty(key: String): String? = when (key) {
        "name" -> this.Name
        "city" -> this.City
        "zipCode" -> this.ZipCode
        else -> null
    }



}


