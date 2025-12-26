package com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.presentation.adapter.WorkOrdersViewPagerAdapter
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomCheckListWithEquipmentData
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomWorkOrderPDFDATA
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedFieldReportViewmodel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.FieldReportVMFactory
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.WorkOrdersVM
import com.gkprojects.cmmsandroidapp.core.utils.PdfFileMaker
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportInventoryCustomData
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrders
import com.gkprojects.cmmsandroidapp.databinding.FragmentWorkOrdersInsertBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.tabs.TabLayoutMediator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID


class WorkOrdersInsertFragment : Fragment() {
    private lateinit var binding: FragmentWorkOrdersInsertBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val sharedFieldReportsViewModel: SharedFieldReportViewmodel by activityViewModels()

    private lateinit var toolbar: MaterialToolbar

    private lateinit var workOrderViewModel: WorkOrdersVM

    private var reportId : String?=null
    private var userId : String?=null
    private var printEquipmentList =ArrayList<CustomCheckListWithEquipmentData>()
    private var printToolsList = ArrayList<FieldReportToolsCustomData>()
    private var printInventoryList = ArrayList<FieldReportInventoryCustomData>()

    private var isReadPermissionGranted =false
    private var isWritePermissionGranted =false
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        isReadPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissionGranted
        isWritePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWritePermissionGranted
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkOrdersInsertBinding.inflate(inflater, container, false)
        toolbar=binding.insertToolbar

        val context = requireContext()
        val workOrderRepository = RepoWorkOrders.getInstance(context)
        val workOrderFactory = FieldReportVMFactory(workOrderRepository)
        workOrderViewModel= ViewModelProvider(this,workOrderFactory)[WorkOrdersVM::class.java]
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
        reportId = arguments?.getString("reportId")
        if (reportId.isNullOrEmpty()){
            sharedFieldReportsViewModel.clearFieldReport()
        }
        sharedViewModel.reportId.value = reportId

        userId=sharedViewModel.user.value!!.UserID
        Log.d("testWorkOrder","$userId")

        val adapter = WorkOrdersViewPagerAdapter(requireActivity(),reportId)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            // Configure tab titles
            tab.text = when (position) {
                0 -> "General Info"
                1 -> "Equipment List"
                2 -> "Tools"
                3 -> "Spare Parts"
                // add more cases as necessary
                else -> "Tab $position"
            }

        }.attach()

        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Access the custom action layout and button after menu is ready
        val menuSaveItem = toolbar.menu.findItem(R.id.insert_toolbar_save)
        val actionView = menuSaveItem?.actionView
        val saveButton = actionView?.findViewById<MaterialButton>(R.id.btn_layout_menu_insert)

        val menuDownloadItem = toolbar.menu.findItem(R.id.insert_workorder_download_pdf)
        val actionView2 = menuDownloadItem?.actionView
        val downloadButton = actionView2?.findViewById<ImageButton>(R.id.btn_layout_menu_insert_download)

        saveButton?.setOnClickListener {
            upsertFieldReport()

            //storeToDatabase(casesID)
            Toast.makeText(requireContext(), "Saved", Toast.LENGTH_SHORT).show()
        }

        downloadButton?.setOnClickListener {
            requestPermission()

            getDataForEquipmentList()
            getDataForToolsList()
            getDataForSparePartsList()
            printPDFData()
            //storeToDatabase(casesID)
            Toast.makeText(requireContext(), "Downloaded", Toast.LENGTH_SHORT).show()
        }


    }



    private fun upsertFieldReport() {
        val reportData = sharedFieldReportsViewModel.fieldReport.value

        if (reportData == null) {
            Log.e("debug_work_order", "Field report data is null")
            return
        }

        when {
            reportData.FieldReportID.isBlank() -> {
                // Generate a new UUID
                val newReport = reportData.copy(
                    FieldReportID = UUID.randomUUID().toString(),
                    UserID = userId,
                    CustomerID = reportData.CustomerID?.ifBlank {
                        sharedViewModel.customerId.value ?: "" // fallback if still null
                    }
                )

                // Only insert if CustomerID is not empty
                if (newReport.CustomerID?.isNotBlank() == true) {
                    workOrderViewModel.insert(newReport)
                    Log.d("debug_work_order", "Inserting new report: $newReport")
                } else {
                    Log.w("debug_work_order", "Cannot insert: CustomerID is empty")
                }
            }

            else -> {
                workOrderViewModel.update(reportData)
                Log.d("debug_work_order", "Updating report: $reportData")
            }
        }
    }


    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.now().format(formatter)
    }


    private fun getDataForEquipmentList() {
        //reportID
        //A list that contains Equipment Info and FieldEquipmentInfo to Feed CheckForm
        Log.d("testingViewModel","$reportId")

        workOrderViewModel.getDataEquipmentListAndCheckListByReportID(reportId!!).observe(
            viewLifecycleOwner){
            if (it!=null){
                printEquipmentList =it as ArrayList<CustomCheckListWithEquipmentData>
                Log.d("testingViewModel","$printEquipmentList")
            }
            else{
                Log.d("testingViewModel","$it")
            }

        }


    }
    private fun getDataForSparePartsList(){
        //reportID
        workOrderViewModel.printDataInventoryListByReportID(reportId!!).observe(
            viewLifecycleOwner) {
                printInventoryList =it as ArrayList<FieldReportInventoryCustomData>
                Log.d("testing2","$printInventoryList")
            }


    }
    private fun getDataForToolsList(){
        //reportID
        workOrderViewModel.printDataToolsListByReportID(reportId!!).observe(
            viewLifecycleOwner) {
                printToolsList =it as ArrayList<FieldReportToolsCustomData>
                Log.d("testing3","$printToolsList")
            }


    }
    private fun printPDFData(){

        workOrderViewModel.printWorkOrder(reportId!!).observe(
            viewLifecycleOwner) {
                val tempData : CustomWorkOrderPDFDATA =it as CustomWorkOrderPDFDATA
                Log.d("testing4","$tempData")
                val pdfMake = PdfFileMaker(requireContext(),tempData)
                pdfMake.printEquipmentList=printEquipmentList
                pdfMake.printInventoryList=printInventoryList
                pdfMake.printToolsList=printToolsList
                pdfMake.printTest()
                Log.d("testing5","$tempData")
            }




    }
    private fun requestPermission(){
        Log.d("reqPermission ","requestPermission")

        isReadPermissionGranted= ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )== PackageManager.PERMISSION_GRANTED


        isWritePermissionGranted= ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )== PackageManager.PERMISSION_GRANTED

        val permissionRequest :MutableList<String> = ArrayList()

        if(!isReadPermissionGranted){
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if(!isWritePermissionGranted){
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if(permissionRequest.isNotEmpty()){

            permissionLauncher.launch(permissionRequest.toTypedArray())
        }

    }
}



