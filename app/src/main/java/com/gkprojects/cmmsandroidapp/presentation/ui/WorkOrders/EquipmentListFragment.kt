package com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.presentation.adapter.AdapterRecyclerViewChecklist
import com.gkprojects.cmmsandroidapp.presentation.adapter.AdapterRecyclerViewDialogEquipmentList
import com.gkprojects.cmmsandroidapp.presentation.adapter.AdapterRecyclerViewEquipmentList
import com.gkprojects.cmmsandroidapp.presentation.adapter.DropDownAdapter
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomDisplayDatFieldReportEquipments
import com.gkprojects.cmmsandroidapp.data.local.entity.Equipments
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportCheckForm
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportEquipment
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReports
import com.gkprojects.cmmsandroidapp.data.local.entity.Maintenances
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CheckFormVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.EquipmentVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.FieldReportCheckListVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.FieldReportEquipmentVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.MaintenancesVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.EquipmentViewModelFactory
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.FieldReportChecklistVMFactory
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.FieldReportEquipmentVMFactory
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoCheckListItems
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoEquipment
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoFieldReportEquipment
import com.gkprojects.cmmsandroidapp.databinding.DialogChecklistWorkorderPerEquipmentBinding
import com.gkprojects.cmmsandroidapp.databinding.DialogEquipmentListWorkordersBinding
import com.gkprojects.cmmsandroidapp.databinding.FragmentWorkOrderEquipmentDataBinding
import kotlinx.coroutines.*
import java.util.UUID


@OptIn(DelicateCoroutinesApi::class)
class EquipmentListFragment : Fragment() , WorkOrderTabData {
    private lateinit var binding : FragmentWorkOrderEquipmentDataBinding

    private lateinit var adapterEquipmentList : AdapterRecyclerViewEquipmentList
    private lateinit var adapterEquipmentChecklist: AdapterRecyclerViewChecklist
    private lateinit var adapterDropDownChecklist : DropDownAdapter

    private lateinit var adapterDropdownEquipments: ArrayAdapterDropdownEquipments

    private var reportId : String?=null
    private var customerId : String? =null
    private var isObserverSetUp = false
    private var customerObserverSetUp = false
    private var selected : Equipments? = null
    private lateinit var equipmentViewModel : EquipmentVM
    private lateinit var equipmentFieldReportViewModel : FieldReportEquipmentVM
    private lateinit var checkFormVM : CheckFormVM
    private lateinit var maintenancesVM : MaintenancesVM
    private lateinit var fieldReportCheckListVM : FieldReportCheckListVM

    private lateinit var recyclerViewEquipmentList: RecyclerView
    private var equipmentsList = ArrayList<CustomDisplayDatFieldReportEquipments>()
    private var masterEquipmentList = ArrayList<Equipments>()


    private var maintenancesList = ArrayList<Maintenances>()
    private var checkformList = ArrayList<FieldReportCheckForm>()
    private var equipmentListFieldReport = ArrayList<FieldReportEquipment>()


    private var maintenanceCheckListItems=ArrayList<FieldReportCheckForm>()

    private var dialogEquipmentList = ArrayList<Equipments>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWorkOrderEquipmentDataBinding.inflate(inflater, container, false)

        val context = requireContext()
        val repositoryEquipment = RepoEquipment.getInstance(context)
        val factoryEquipmentVM = EquipmentViewModelFactory(repositoryEquipment)
        equipmentViewModel= ViewModelProvider(this,factoryEquipmentVM)[EquipmentVM::class.java]

        val repositoryEquipmentFieldReport = RepoFieldReportEquipment.getInstance(context)
        val factoryEquipmentFieldReportVM = FieldReportEquipmentVMFactory(repositoryEquipmentFieldReport)
        equipmentFieldReportViewModel= ViewModelProvider(this,factoryEquipmentFieldReportVM)[FieldReportEquipmentVM::class.java]

        val repositoryFieldReportCheckList = RepoCheckListItems.getInstance(context)
        val factoryFieldReportCheckListVM = FieldReportChecklistVMFactory(repositoryFieldReportCheckList)
        fieldReportCheckListVM= ViewModelProvider(this,factoryFieldReportCheckListVM)[FieldReportCheckListVM::class.java]

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        maintenancesVM=ViewModelProvider(this)[MaintenancesVM::class.java]
        checkFormVM=ViewModelProvider(this)[CheckFormVM::class.java]



        val sharedViewModel: SharedViewModel by activityViewModels()

        if (!isObserverSetUp) {
            sharedViewModel.reportId.observe(viewLifecycleOwner) { id ->
                Log.d("sharedViewModelReportId", id)
                reportId = id
                getEquipmentsByWorkOrderID(reportId as String)
            }
            isObserverSetUp = true
        }
        if (!customerObserverSetUp) {
            sharedViewModel.customerId.observe(viewLifecycleOwner) { id ->
                Log.d("sharedViewModelCustomerId", id)
                customerId = id
                getEquipmentsByID()
                //getEquipmentsByID(reportId as Int)
            }
            customerObserverSetUp = true
        }
        maintenancesVM.getAllMaintenances(requireContext()).observe(
            viewLifecycleOwner){
                maintenancesList =it as ArrayList<Maintenances>
                Log.d("testMaintenanceMap", "$maintenancesList")

            }

        val autoComplete= binding.workOrderEquipmentsSelection
        adapterDropdownEquipments=ArrayAdapterDropdownEquipments(requireContext(),masterEquipmentList)
        autoComplete.setAdapter(adapterDropdownEquipments)

        autoComplete.setOnItemClickListener { parent, _, position, _ ->
            adapterDropdownEquipments.selectItemAt(position)

            // Show formatted text in AutoComplete
            selected = adapterDropdownEquipments.selectedEquipment

            if (selected != null) {
                autoComplete.setText("${selected!!.Model} - ${selected!!.SerialNumber}", false)
                Toast.makeText(requireContext(),"$selected",Toast.LENGTH_SHORT).show()
            }
        }


        recyclerViewEquipmentList=binding.workOrderEquipmentsRecyclerview
        adapterEquipmentList= AdapterRecyclerViewEquipmentList(equipmentsList)
        recyclerViewEquipmentList.apply {
            setHasFixedSize(true)
            layoutManager= LinearLayoutManager(this.context)
            adapter= adapterEquipmentList
        }
        adapterEquipmentList.setOnItemClickListener(object : AdapterRecyclerViewEquipmentList.OnItemClickListener {
            override fun onItemClick(equipment: CustomDisplayDatFieldReportEquipments) {
                // Handle the click
                insertCheckListDialog(equipment.idFieldReportEquipment)
            }
        })

        val btnOpenDialogAdd =binding.workOrderEquipmentsAddBtn

        btnOpenDialogAdd.setOnClickListener {
            Log.d("test","test123")
            if(selected!!.EquipmentID.isNotEmpty())
            insertFieldReportEquipments(selected!!.EquipmentID)
            //openCustomDialog()
        }

    }


    private fun getEquipmentsByWorkOrderID(id : String){
        Log.d("CheckID", id)
        try {
            equipmentFieldReportViewModel.loadCustomDisplay(id)
                .observe(
                    viewLifecycleOwner
                ) { it ->
                    Log.d("EquipmentList", "$it")
                    equipmentsList = it as ArrayList<CustomDisplayDatFieldReportEquipments>
                    if (equipmentsList.isNullOrEmpty()) {
                        // The list is empty
                        Log.d("EquipmentList", "No data returned")
                    } else {
                        // The list is not empty
                        adapterEquipmentList.setData(equipmentsList)
                    }

                }

        }catch (e:Exception){
            Log.d("equipmentList","$e")
        }

        try {
            equipmentFieldReportViewModel.loadAllEquipmentsByReportID(id).observe(viewLifecycleOwner){
                list ->
                equipmentListFieldReport =list as ArrayList<FieldReportEquipment>
            }
        }catch (e:Exception){
            Log.d("equipmentList error","$e")
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun insertFieldReportEquipments(equipmentId :String){
        val tempEquipments= FieldReportEquipment(UUID.randomUUID().toString(),null,false,null,null,null,reportId,equipmentId,null)
        Toast.makeText(requireContext(),"$tempEquipments",Toast.LENGTH_SHORT).show()
        GlobalScope.launch(Dispatchers.IO) { equipmentFieldReportViewModel.insert(tempEquipments) }

    }
    @SuppressLint("SuspiciousIndentation")
    private fun insertCheckListDialog(maintenanceEquipmentCheckListID :String?){
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle("Title") // Set the title of the dialog if needed

// Inflate the custom layout and set it as the content of the dialog
        val binding = DialogChecklistWorkorderPerEquipmentBinding.inflate(layoutInflater)
        dialogBuilder.setView(binding.root)

        // Get the SearchView and RecyclerView from the layout

        val recyclerView = binding.dialogChecklistWorkorderPerEquipmentRecyclerView
        adapterEquipmentChecklist= AdapterRecyclerViewChecklist(checkformList)

        // Set the adapter for the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapterEquipmentChecklist
        recyclerView.setHasFixedSize(true)
        adapterDropDownChecklist = DropDownAdapter(requireContext(), R.layout.dropdown_adapter_tools_work_orders, maintenancesList)
        binding.dialogChecklistWorkorderPerEquipmentSelectCheckFormAutoComplete.setAdapter(adapterDropDownChecklist)
        binding.dialogChecklistWorkorderPerEquipmentSelectCheckFormAutoComplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedTool = parent.getItemAtPosition(position) as Maintenances
            // `selectedTool` is the selected item
            maintenanceCheckListItems.clear()
            Log.d("toolsListDrop","$selectedTool")
            populateChecklistByCheckFormID(selectedTool.MaintenanceID,maintenanceEquipmentCheckListID!!)

        }




// Set the positive button
        dialogBuilder.setPositiveButton("Positive") { dialog, which ->
        val list = adapterEquipmentChecklist.getData()
            Log.d("checkChanged","$list")
            if (list.all { it.Result != null }) {
                for (item in list){
                    item.FieldReportCheckFormID=UUID.randomUUID().toString()

                    insertFieldCheckInDB(item)
                    updateFieldReportStatus(1 , maintenanceEquipmentCheckListID)
                }
            } else {
                // Handle the case where some Result values are null
                // For example, you can show a message to the user
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

// Set the negative (clear) button
        dialogBuilder.setNegativeButton("Close") { dialog, which ->
            // Clear the dialog or do something else
            dialog.dismiss()
        }

// Create and show the dialog
        val dialog = dialogBuilder.create()
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Confirm Save")
                .setMessage("Do you want to continue and save the form?")
                .setPositiveButton("Yes") { _, _ ->
                    // Perform save action here
                }
                .setNegativeButton("No", null)
                .show()
        }

        val window = dialog.window
        window?.setLayout((resources.displayMetrics.widthPixels * 0.9).toInt(), WindowManager.LayoutParams.WRAP_CONTENT)

        window?.setGravity(Gravity.CENTER)

    }

    private fun updateFieldReportStatus(value: Int, maintenanceEquipmentCheckListID: String?) {
        Log.d("UpdateSuccessful","$maintenanceEquipmentCheckListID")

            equipmentFieldReportViewModel.updateStatus(maintenanceEquipmentCheckListID!!,value)


    }

    private fun insertFieldCheckInDB(item: FieldReportCheckForm) {

            fieldReportCheckListVM.insertFieldReportCheckList(item)


    }

    private fun getEquipmentsByID(){
        if (customerId!=null) {
//            equipmentViewModel
//                .getAllEquipmentDataByCustomerID(customerId!!)
//                .observe(viewLifecycleOwner){ list ->
//
//                Log.d("dialogEquipmentList", "$it")
//                masterEquipmentList = it as ArrayList<Equipments>
//                adapterDropdownEquipments.updateData(masterEquipmentList)
//
//            }
            equipmentViewModel.getAllEquipmentDataByCustomerID(customerId!!) { list ->
                Log.d("dialogEquipmentList", "$list")

                masterEquipmentList = ArrayList(list)
                adapterDropdownEquipments.updateData(masterEquipmentList)
            }
        }
        else{
            Log.d("DebugEquipmentList","$customerId")
        }

    }

    private fun populateChecklistByCheckFormID(selectedId: String,maintenanceFieldEquipmentID :String) {
        Log.d("fieldChecklist","here")
        checkFormVM.getCheckFormFields(requireContext(),selectedId).removeObservers(viewLifecycleOwner)
        checkFormVM.getCheckFormFields(requireContext(),selectedId).observe(
            viewLifecycleOwner){items->

                for (item in items){

                    val tempListCheckList = FieldReportCheckForm("",null,
                        maintenanceFieldEquipmentID,item.Description,item.ValueExpected,
                        null,null,null,null,null)
                    maintenanceCheckListItems.add(tempListCheckList)
                }
                adapterEquipmentChecklist.setData(maintenanceCheckListItems)

                Log.d("fieldchecklist","$items")



            }



    }

    override fun collectData(): WorkOrderPartialData {

        return WorkOrderPartialData(
            workOrder = FieldReports.empty(),
            tools = emptyList(),
            equipments = equipmentListFieldReport,
            spareParts = emptyList(),
            checkForms = checkformList
        )
    }


}