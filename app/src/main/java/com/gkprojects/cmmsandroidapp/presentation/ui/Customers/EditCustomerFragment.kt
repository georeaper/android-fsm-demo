package com.gkprojects.cmmsandroidapp.presentation.ui.Customers


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment

import android.widget.Toast

import androidx.lifecycle.ViewModelProvider


import com.gkprojects.cmmsandroidapp.data.local.entity.Customer


import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CustomerVM

import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.databinding.FragmentCustomerInsertUpdatedBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID


class EditCustomerFragment : Fragment() {

    private lateinit var customerViewModel: CustomerVM
    private lateinit var toolbar: MaterialToolbar
    private var customer : Customer? = null
    private var customerID : String? =null
    private lateinit var binding:FragmentCustomerInsertUpdatedBinding




    override  fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle? ): View
    {
        binding=FragmentCustomerInsertUpdatedBinding.inflate(inflater, container, false)
        val args =this.arguments
        customerID= args?.getString("id")

        toolbar=binding.insertToolbar

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customerViewModel = ViewModelProvider(this)[CustomerVM::class.java]


        if (customerID != null) {
            GlobalScope.launch(Dispatchers.Main) {
                customerViewModel.getCustomerDataByID(requireContext(), customerID!!)
                    .observe(viewLifecycleOwner)
                        {

                            binding.customerInsertName.setText(it.Name)
                            binding.customerInsertPhoneNumber.setText(it.Phone)
                            binding.customerInsertDescription.setText(it.Description)
                            binding.customerInsertAddress.setText(it.Address)
                            binding.customerInsertZipCode.setText(it.ZipCode)
                            binding.customerInsertEmail.setText(it.Email)
                            binding.customerInsertNotes.setText(it.Notes)
                            binding.customerInsertCity  .setText(it.City)
                            if (it.CustomerStatus != null) {
                                binding.customerInserStatus.isChecked = it.CustomerStatus!!
                            }
                            customer= it as Customer
                        }
            }


        }

       setupToolbar()

    }



    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Access the custom action layout and button after menu is ready
        val menuItem = toolbar.menu.findItem(R.id.insert_toolbar_save)
        val actionView = menuItem?.actionView
        val saveBtn = actionView?.findViewById<MaterialButton>(R.id.btn_layout_menu_insert)



        saveBtn?.setOnClickListener {


                if (customerID == null) {
                    Log.d("InsertCustomer","Insert")
                    insertCustomer()
                }else{
                    Log.d("InsertCustomer","Update")
                    updateCustomer()
                }


            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertCustomer(){

        val remoteID : Int? =null
        val lastModified  =getCurrentDateAsString()
        val version = null

        val status =binding.customerInserStatus.isChecked
        val dateCreated =getCurrentDateAsString()
        customer= Customer(
            UUID.randomUUID().toString(),
            remoteID,
            binding.customerInsertName.text.toString(),
            binding.customerInsertPhoneNumber.text.toString(),
            binding.customerInsertEmail.text.toString(),
            binding.customerInsertAddress.text.toString(),
            binding.customerInsertZipCode.text.toString(),
            binding.customerInsertCity.text.toString(),
            binding.customerInsertNotes.text.toString(),
            binding.customerInsertDescription.text.toString(),
            status,lastModified,dateCreated,version)
            Log.d("InsertCustomer",customer.toString())

            customerViewModel.insert(requireContext(), customer!!)


    }

    private fun updateCustomer(){


        val remoteID =customer?.RemoteID
        val lastModified = getCurrentDateAsString()
        val dateCreated =customer?.DateCreated
        val version =customer?.Version

        customer= Customer(customerID!!,
            remoteID,
            binding.customerInsertName.text.toString(),
            binding.customerInsertPhoneNumber.text.toString(),
            binding.customerInsertEmail.text.toString(),
            binding.customerInsertAddress.text.toString(),
            binding.customerInsertZipCode.text.toString(),
            binding.customerInsertCity.text.toString(),
            binding.customerInsertNotes.text.toString(),
            binding.customerInsertDescription.text.toString(),
            binding.customerInserStatus.isChecked,
            lastModified,
            dateCreated,
            version)

        Log.d("updateCustomer",customer.toString())

        customerViewModel.update(requireContext(), customer!!)

        }


    fun getCurrentDateAsString(): String {
        // Get the current date
        val currentDate = LocalDate.now()

        // Define a format for the date (optional, you can skip this step if you don't need a specific format)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        // Format the date to a string
        val formattedDate = currentDate.format(formatter)

        return formattedDate
    }

    }





