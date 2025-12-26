package com.gkprojects.cmmsandroidapp.presentation.ui.Equipments

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

import com.gkprojects.cmmsandroidapp.presentation.adapter.RvAdapterFindCustomers
import com.gkprojects.cmmsandroidapp.domain.usecase.NotificationService
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect

import com.gkprojects.cmmsandroidapp.data.local.entity.Equipments
import com.gkprojects.cmmsandroidapp.data.local.dto.Notification
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.CategoryRepository
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CategoryViewModelFactory
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.ManufacturerRepository
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.ManufacturerViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.ManufacturerViewModelFactory
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.ModelRepository
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.ModelViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.ModelViewModelFactory
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CategoryViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.EquipmentVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.EquipmentViewModelFactory
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoEquipment
import com.gkprojects.cmmsandroidapp.databinding.FragmentEquipmentInsertUpdatedBinding
import com.gkprojects.cmmsandroidapp.presentation.common.CustomerSelectionDialog
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*
import kotlin.collections.ArrayList


@Suppress("DEPRECATION")
class EquipmentInsertFragment : Fragment() {

    private lateinit var equipmentViewModel: EquipmentVM
    private lateinit var toolbar: MaterialToolbar
    private lateinit var binding : FragmentEquipmentInsertUpdatedBinding
    private var dialog: Dialog? = null
    private var rvAdapter: RvAdapterFindCustomers? = null
    private lateinit var filterText : SearchView
    private var customerId : String?= null
    private var equipmentId : String?= null
    private var lastModified : String? = null
    private var dateCreated : String? = null
    private var version : String? = null
    private var customerSearch =ArrayList<CustomerSelect>()
    private lateinit var vmManufacturer : ManufacturerViewModel
    private lateinit var vmModel : ModelViewModel
    private lateinit var vmCategory : CategoryViewModel
    private lateinit var manufacturerAutoComplete:AutoCompleteTextView
    private lateinit var modelAutoComplete :AutoCompleteTextView
    private lateinit var categoryAutoComplete  :AutoCompleteTextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEquipmentInsertUpdatedBinding.inflate(inflater, container, false)

        toolbar=binding.insertToolbar

        val context = requireContext()
        val repositoryManu = ManufacturerRepository.getInstance(context)
        val factoryManu = ManufacturerViewModelFactory(repositoryManu)
        vmManufacturer = ViewModelProvider(this, factoryManu)[ManufacturerViewModel::class.java]

        val repositoryCat = CategoryRepository.getInstance(context)
        val factoryCat = CategoryViewModelFactory(repositoryCat)
        vmCategory = ViewModelProvider(this, factoryCat)[CategoryViewModel::class.java]

        val repositoryModel = ModelRepository.getInstance(context)
        val factoryModel = ModelViewModelFactory(repositoryModel)
        vmModel = ViewModelProvider(this, factoryModel)[ModelViewModel::class.java]

        val repositoryEquipment = RepoEquipment.getInstance(context)
        val factoryEquipmentVM = EquipmentViewModelFactory(repositoryEquipment)
        equipmentViewModel= ViewModelProvider(this,factoryEquipmentVM)[EquipmentVM::class.java]


        return binding.root
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modelAutoComplete=binding.equipmentInsertTextInputEdittextModel
        manufacturerAutoComplete=binding.equipmentInsertAutoCompleteTextViewManufacturer
        categoryAutoComplete  =binding.equipmentInsertAutoCompleteTextViewCategory

        val args =this.arguments
        equipmentId= args?.getString("EquipmentId")



        vmManufacturer.manufacturerData.observe(viewLifecycleOwner){ manufacturer->
            val manufacturerArray = manufacturer.map { it.Name } // Assuming ManufacturerAsset has a 'name' property
            val manufacturerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, manufacturerArray)

            manufacturerAutoComplete.setAdapter(manufacturerAdapter)
        }

        vmModel.modelAssetData.observe(viewLifecycleOwner){ models->
            val modelArray = models.map { it.Name } // Assuming ModelAsset has a 'name' property
            val modelAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, modelArray)

            modelAutoComplete.setAdapter(modelAdapter)
        }


        vmCategory.categoryData.observe(viewLifecycleOwner) { categories ->
            Log.d("DataCategory","$categories")
            val categoryArray = categories.map { it.Name }
            val categoryAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                categoryArray
            )
            categoryAutoComplete.setAdapter(categoryAdapter)
        }
        loadData()

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Access the custom action layout and button after menu is ready
        val menuItem = toolbar.menu.findItem(R.id.insert_toolbar_save)
        val actionView = menuItem?.actionView
        val saveButton = actionView?.findViewById<MaterialButton>(R.id.btn_layout_menu_insert)



        saveButton?.setOnClickListener {
            Log.d("equipmentInsert","buttonPressed")
            storeToDatabase(equipmentId ?: "")
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
        }

        val installation=binding.equipmentInsertDateStart
        val customerNameTV=binding.tvSelectCustomer
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        installation.setOnClickListener {
            fragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
        }

        picker.addOnPositiveButtonClickListener { timestamp ->
            // Convert timestamp to Date
            val date = Date(timestamp)

            // Use DateUtils to get the normalized date string
            val normalizedDate = DateUtils.format(date) // "dd/MM/yyyy HH:mm:ss"

            // Display only the date part in the EditText
            installation.setText(normalizedDate.substringBefore(" "))
        }


        //fetching data based on equipmentID

        if (equipmentId!=null){
            equipmentViewModel.getRecordById(equipmentId!!) { record ->
                // This is called on the main thread after the suspend call is finished.

                // Use the data safely in the UI
                binding.equipmentInsertTextInputEdittextSn.setText(record.SerialNumber)
                binding.equipmentInsertTextInputEdittextModel.setText(record.Model)
                binding.equipmentInsertAutoCompleteTextViewManufacturer.setText(record.Manufacturer)
                binding.equipmentInsertTextInputEdittextDescription.setText(record.Description)
                binding.equipmentInsertTextInputEdittextVersion.setText(record.Version)
                binding.equipmentInsertAutoCompleteTextViewCategory.setText(record.EquipmentCategory)
                binding.equipmentInsertTextInputEdittextWarranty.setText(record.Warranty)
                binding.equipmentInsertCheckboxStatus.isChecked = record.EquipmentStatus!!
                binding.equipmentInsertDateStart.setText(record.InstallationDate)

                // Store ID or other fields if needed
                equipmentId = record.EquipmentID
                dateCreated = record.DateCreated
                customerId  = record.CustomerID
                setCustomer(customerId!!)
            }


        }else{
            equipmentViewModel.getCustomerId{record ->
                customerSearch = record as ArrayList<CustomerSelect>

            }
        }
        binding.equipmentInsertCustomerSelectBtn.setOnClickListener {

       //     selectCustomerDialog(customerNameTV)
            val customerDialog= CustomerSelectionDialog(
            context = requireContext(),
            customers = customerSearch,
            onCustomerSelect = { selectedCustomer ->
                customerNameTV.text = selectedCustomer.CustomerName
                customerId = selectedCustomer.CustomerID
            }
        )
        customerDialog.show()
        }
    }

    private fun storeToDatabase(equipmentId: String) {
        if (equipmentId.isNullOrEmpty()){
            Log.d("equipmentInsert", equipmentId)
            insertData()
        }else{
            Log.d("equipmentUpdate", equipmentId)
            updateData()
        }


    }



    private fun filterList(query: String,searchCustomer : ArrayList<CustomerSelect>) {
        val filteredList= java.util.ArrayList<CustomerSelect>()
        for (i in searchCustomer){
            if (i.CustomerName!!.lowercase(Locale.ROOT).contains(query))
                filteredList.add(i)
            Log.d("datafilterDialog", filteredList.toString())
        }
        if (filteredList.isEmpty() ){
            Toast.makeText(context,"Empty List", Toast.LENGTH_SHORT).show()

        }else{

            rvAdapter?.filterList(filteredList)
        }

    }

    private fun getValuesFromdb(data : ArrayList<CustomerSelect>, id :String?, tv :TextView) {
        val customerNameIndexed = mutableMapOf<String?, String?>()

        for (i in data.indices) {

            customerNameIndexed[data[i].CustomerID] = data[i].CustomerName
        }
        tv.text = customerNameIndexed[id]
    }
    private fun setCustomer(id : String){
        val customerTextView=binding.tvSelectCustomer
        equipmentViewModel.getCustomerId{record ->
            customerSearch = record as ArrayList<CustomerSelect>
            Log.d("CustomerID3","$customerId")
            getValuesFromdb(customerSearch, id, customerTextView)
        }

    }







    private fun insertData() {

        val insertEquipment = Equipments(UUID.randomUUID().toString(),
            null,
            null,
            binding.equipmentInsertTextInputEdittextSn.text.toString(),
            binding.equipmentInsertTextInputEdittextModel.text.toString(),
            binding.equipmentInsertAutoCompleteTextViewManufacturer.text.toString(),
            null,
            binding.equipmentInsertTextInputEdittextDescription.text.toString(),
            binding.equipmentInsertTextInputEdittextVersion.text.toString(),
            binding.equipmentInsertAutoCompleteTextViewCategory.text.toString(),
            binding.equipmentInsertTextInputEdittextWarranty.text.toString(),
            binding.equipmentInsertCheckboxStatus.isChecked,
            binding.equipmentInsertDateStart.text.toString(),
            lastModified,
            dateCreated,
            version,
            customerId

            )
        Log.d("equipmentInsert","$insertEquipment")

//        GlobalScope.launch(Dispatchers.IO) {
//            equipmentViewModel.insert(requireContext(),insertEquipment)  }
        equipmentViewModel.insertEquipment(insertEquipment) { insertedEquipment ->
            val notificationService = NotificationService.getInstance(requireContext())
            notificationService.addNotification(
                Notification(
                    1,
                    timeStamp = insertedEquipment.LastModified!!,
                    title = "New equipment",
                    description = "Equipment: ${insertedEquipment.Name} has been added",
                    type = "Added",
                    function = "None",
                    seen = false
                )
            )
        }
        val fragmentManager =parentFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        val fragment = EquipmentFragment()
        fragmentTransaction.replace(R.id.frameLayout1,fragment)
        fragmentTransaction.commit()
    }

    private fun updateData() {


        val updateEquipment = Equipments(equipmentId!!,
            null,
            null,
            binding.equipmentInsertTextInputEdittextSn.text.toString(),
            binding.equipmentInsertTextInputEdittextModel.text.toString(),
            binding.equipmentInsertAutoCompleteTextViewManufacturer.text.toString(),
            null,
            binding.equipmentInsertTextInputEdittextDescription.text.toString(),
            binding.equipmentInsertTextInputEdittextVersion.text.toString(),
            binding.equipmentInsertAutoCompleteTextViewCategory.text.toString(),
            binding.equipmentInsertTextInputEdittextWarranty.text.toString(),
            binding.equipmentInsertCheckboxStatus.isChecked,
            binding.equipmentInsertDateStart.text.toString(),
            lastModified,
            dateCreated,
            version,
            customerId
        )
        equipmentViewModel.updateEquipment(updateEquipment) { updatedEquipment ->
            val notificationService = NotificationService.getInstance(requireContext())
            notificationService.addNotification(
                Notification(
                    1,
                    timeStamp = updatedEquipment.LastModified!!,
                    title = "New equipment",
                    description = "Equipment: ${updatedEquipment.Model +" "+ updatedEquipment.SerialNumber} has been added",
                    type = "Added",
                    function = "None",
                    seen = false
                )
            )
        }

//        GlobalScope.launch(Dispatchers.IO) {equipmentViewModel.updateEquipment(requireContext(),updateEquipment)  }

        val fragmentManager =parentFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        val fragment = EquipmentFragment()
        fragmentTransaction.replace(R.id.frameLayout1,fragment)
        fragmentTransaction.commit()
    }


    private fun loadData(){
        vmManufacturer.loadManufacturer()
        vmCategory.loadCategories()
        vmModel.loadAllModelAsset()
    }
}






