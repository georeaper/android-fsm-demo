package com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders

import com.gkprojects.cmmsandroidapp.core.utils.PdfFileMaker
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.presentation.adapter.RvAdapterFindCustomers
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomCheckListWithEquipmentData
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomWorkOrderPDFDATA
import com.gkprojects.cmmsandroidapp.data.local.entity.Customer
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReports
import com.gkprojects.cmmsandroidapp.data.local.entity.Users
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.SettingsRepository
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SettingsViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SettingsViewModelFactory
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CustomerVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedFieldReportViewmodel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.UsersVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.FieldReportVMFactory
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.WorkOrdersVM
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportInventoryCustomData
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrders
import com.gkprojects.cmmsandroidapp.presentation.common.SignatureView
import com.gkprojects.cmmsandroidapp.databinding.FragmentWorkOrderCustomerDataBinding
import com.gkprojects.cmmsandroidapp.presentation.common.CustomerSelectionDialog
import com.google.android.material.datepicker.MaterialDatePicker

import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale


class CustomerInfoFragment : Fragment(){
    private lateinit var customerViewModel: CustomerVM
    private lateinit var workOrderViewModel: WorkOrdersVM
    private lateinit var usersViewModel : UsersVM
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val sharedFieldReportsViewModel: SharedFieldReportViewmodel by activityViewModels()


    private lateinit var binding : FragmentWorkOrderCustomerDataBinding
    private var customers =ArrayList<Customer>()
    val customerSearch = ArrayList<CustomerSelect>()
    private var dialog: Dialog? = null
    private var rvAdapter: RvAdapterFindCustomers? = null
    private lateinit var filterText : SearchView

    private var customerId : String?=null
    private var contractId : String?=null
    private var caseId : String?=null
    private var userId : String?=null
    private var userDetails : Users?= null
    private var reportId : String?=null

    private var reportNumber : String?=null
    private var remoteDBiD : Int?=null
    private var reportCostValue : Double?=null
    private var lastModified : String?=null
    private var version : String?=null
    private var dateCreated : String?=null
    private var clientSignature :ByteArray? = null
    private lateinit var reportType: AutoCompleteTextView

    private var fieldReport : FieldReports?=null

    private var printEquipmentList =ArrayList<CustomCheckListWithEquipmentData>()
    private var printToolsList = ArrayList<FieldReportToolsCustomData>()
    private var printInventoryList = ArrayList<FieldReportInventoryCustomData>()


    private lateinit var viewModel: SettingsViewModel
    private val settingKey="ReportType"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkOrderCustomerDataBinding.inflate(inflater, container, false)

        val context = requireContext()
        val repository = SettingsRepository.getInstance(context)
        val factory = SettingsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        val workOrderRepository = RepoWorkOrders.getInstance(context)
        val workOrderFactory = FieldReportVMFactory(workOrderRepository)
        workOrderViewModel= ViewModelProvider(this,workOrderFactory)[WorkOrdersVM::class.java]
        // Inflate the layout for this fragment

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customerViewModel= ViewModelProvider(this)[CustomerVM::class.java]
        getCustomers()
        reportType = binding.woInsertCustomerDataReportType

        usersViewModel =ViewModelProvider(this)[UsersVM::class.java]


        sharedViewModel.user.observe(viewLifecycleOwner) {
            userId = it.UserID
            usersViewModel.loadSingleUser(requireContext(), userId!!)
        }

        usersViewModel.userDetails.observe(viewLifecycleOwner) { user ->
            userDetails = user
            // now you have the loaded user
        }


        viewModel.settingsData.observe(viewLifecycleOwner, Observer { settings->
            val reportArray=settings.map { it.SettingsValue }
            val adapter = ArrayAdapter(requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                reportArray)
           reportType.setAdapter(adapter)
        })
        loadData()
        passDataToParent()

        val signatureView = SignatureView(requireContext())

        sharedViewModel.reportId.observe(viewLifecycleOwner){
            iD->
            if (iD!=null){
                reportId=iD
                Log.d("reportId->", reportId!!)
                workOrderViewModel.getWorkOrderByID(reportId!!).observe(viewLifecycleOwner) { report->

                        setUpData(report as FieldReports)
                        fieldReport =report
                    getDataForEquipmentList()
                    getDataForToolsList()
                    getDataForSparePartsList()

                }


            }
        }


        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        val picker2 =builder.build()
        binding.woInsertCustomerDataDateEnd.setOnClickListener{
            fragmentManager?.let { it1 -> picker2.show(it1, picker2.toString()) }
        }
        picker2.addOnPositiveButtonClickListener {timestamp ->
            // Convert timestamp to Date
            val date = Date(timestamp)

            // Use DateUtils to get the normalized date string
            val normalizedDate = DateUtils.format(date) // "dd/MM/yyyy HH:mm:ss"

            // Display only the date part in the EditText
            binding.woInsertCustomerDataDateEnd.setText(normalizedDate.substringBefore(" "))
//            val calendar2 = Calendar.getInstance()
//            calendar2.timeInMillis = it
//            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//            val selectedDate = format.format(calendar2.time)
//            binding.woInsertCustomerDataDateEnd.setText(selectedDate)
        }

        binding.woInsertCustomerDataDateStart.setOnClickListener {
            fragmentManager?.let { it1 -> picker.show(it1, picker.toString()) }
        }
        picker.addOnPositiveButtonClickListener {timestamp ->
            // Convert timestamp to Date
            val date = Date(timestamp)

            // Use DateUtils to get the normalized date string
            val normalizedDate = DateUtils.format(date) // "dd/MM/yyyy HH:mm:ss"

            // Display only the date part in the EditText
            binding.woInsertCustomerDataDateStart.setText(normalizedDate.substringBefore(" "))
//            val calendar = Calendar.getInstance()
//            calendar.timeInMillis = it
//            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//            val selectedDate = format.format(calendar.time)
//            binding.woInsertCustomerDataDateStart.setText(selectedDate)
        }





        val signBtn =binding.woInsertCustomerDataSignatureBtn
        signBtn.setOnClickListener {
            Log.d("clientSignature ","${clientSignature.toString()}")
            if (clientSignature!=null) {
                val bitmap = BitmapFactory.decodeByteArray(clientSignature, 0, clientSignature!!.size)
                // Display the bitmap in an ImageView
                val imageView = ImageView(requireContext())
                imageView.setImageBitmap(bitmap)
                AlertDialog.Builder(requireContext())
                    .setTitle("Your signature")
                    .setView(imageView)
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            } else {
                val dialog = AlertDialog.Builder(requireContext())
                    .setTitle("Draw your signature")
                    .setView(signatureView)
                    .setPositiveButton("Save") { _, _ ->
                        val bitmap = signatureView.getSignatureBitmap()
                        val stream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                        val byteArray = stream.toByteArray()
                        // Save byteArray to your database
                        clientSignature = byteArray
                        sharedFieldReportsViewModel.updateFieldReport {
                            ClientSignature = clientSignature
                        }

                    }
                    .setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNeutralButton("Clear") { _, _ ->
                        signatureView.clear()
                    }
                    .create()
                dialog.setOnDismissListener {
                    (signatureView.parent as ViewGroup).removeView(signatureView)
                }
                dialog.show()
            }
        }




        val customerSelectionBtn =binding.workOrderInsertCustomerDataCustomerSelectionBtn
        customerSelectionBtn.setOnClickListener {
            val customerDialog= CustomerSelectionDialog(
            context = requireContext(),
            customers = customerSearch,
            onCustomerSelect = { selectedCustomer ->
                binding.tvSelectCustomer.text = selectedCustomer.CustomerName
                customerId = selectedCustomer.CustomerID
                Log.d("testWorkOrder","$customerId")
                sharedFieldReportsViewModel.updateFieldReport {
                    CustomerID = customerId
                    Log.d("testWorkOrder","$CustomerID")
                }
            }
        )
        customerDialog.show()
            //setDialog()

        }

    }
    private fun loadData(){
        viewModel.loadSettingsByKey(settingKey)
    }

    private fun passDataToParent() {
        sharedFieldReportsViewModel.updateFieldReport {
            ClientSignature = clientSignature
        }

        binding.woInsertCustomerDataWoNumber.addTextChangedListener {
            sharedFieldReportsViewModel.updateFieldReport {
                ReportNumber = it.toString()
            }
        }

        binding.woInsertCustomerDataTitle.addTextChangedListener {
            sharedFieldReportsViewModel.updateFieldReport {
                Title = it.toString()
            }
        }
        binding.woInsertCustomerDataReportType.addTextChangedListener {
            sharedFieldReportsViewModel.updateFieldReport {
                ReportStatus = it.toString()
            }
        }
        binding.woInsertCustomerDataDateStart.addTextChangedListener {
            sharedFieldReportsViewModel.updateFieldReport {
                StartDate = it.toString()
                Log.d("testWorkOrder","$this")
            }
        }
        binding.woInsertCustomerDataDateEnd.addTextChangedListener {
            sharedFieldReportsViewModel.updateFieldReport {
                EndDate = it.toString()
            }
        }

        binding.woInsertCustomerDataSigneeName.addTextChangedListener {
            sharedFieldReportsViewModel.updateFieldReport {
                ClientName = it.toString()
            }
        }
        binding.woInsertCustomerDataDescription.addTextChangedListener {
            sharedFieldReportsViewModel.updateFieldReport {
                Description = it.toString()
            }
        }

        sharedFieldReportsViewModel.updateFieldReport {
            ClientSignature=clientSignature
            CustomerID=customerId
            ContractID=contractId
            CaseID=caseId
            UserID=userId
            FieldReportID=reportId ?: ""
            RemoteID=remoteDBiD
            Value=reportCostValue
            LastModified=lastModified
            DateCreated=dateCreated
            Version=version
            ReportNumber=reportNumber

            Log.d("testWorkOrder2","$this")
        }

    }


    private fun setUpData(fieldReports: FieldReports) {
        caseId=fieldReports.CaseID
        customerId=fieldReports.CustomerID
        val sharedViewModel2: SharedViewModel by activityViewModels()
        sharedViewModel2.customerId.value=customerId
        userId=fieldReports.UserID
        contractId=fieldReports.ContractID
        clientSignature=fieldReports.ClientSignature
        remoteDBiD=fieldReports.RemoteID
        reportCostValue=fieldReports.Value
        version=fieldReports.Version
        dateCreated=fieldReports.DateCreated
        lastModified=fieldReports.LastModified
        reportNumber=fieldReports.ReportNumber
        binding.woInsertCustomerDataWoNumber.setText(reportNumber)
        binding.woInsertCustomerDataDescription.setText(fieldReports.Description)
        binding.woInsertCustomerDataTitle.setText(fieldReports.Title)
        binding.woInsertCustomerDataDateStart.setText(fieldReports.StartDate)
        binding.woInsertCustomerDataDateEnd.setText(fieldReports.EndDate)
//        binding.dep.setText(fieldReports.Department)
        binding.woInsertCustomerDataSigneeName.setText(fieldReports.ClientName)
        binding.woInsertCustomerDataReportType.setText(fieldReports.ReportStatus,false)
        sharedFieldReportsViewModel.updateFieldReport {
            ClientSignature=clientSignature
            ContractID=contractId
            CaseID=caseId
            UserID=userId
            FieldReportID=reportId ?: ""
            RemoteID=remoteDBiD
            Value=reportCostValue
            LastModified=lastModified
            DateCreated=dateCreated
            Version=version
            ReportNumber=reportNumber
            CustomerID=customerId

            Log.d("testWorkOrder2","$this")
        }

        customerViewModel.getAllCustomerData(requireContext()).observe(viewLifecycleOwner){
                customers=it as ArrayList<Customer>

                val customerName= customers.find { it.CustomerID== customerId   }
                binding.tvSelectCustomer.text=customerName!!.Name
                sharedFieldReportsViewModel.updateFieldReport {
                CustomerID =customerId
                }
            }
    }


    private fun getCurrentDate(): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        return LocalDate.now().format(formatter)
    }
    private fun formatNumber(number: Int?, digits: Int): String {
        return String.format("%0${digits}d", number)
    }
    private fun getCustomers(){

            customerViewModel.getAllCustomerData(requireContext()).observe(viewLifecycleOwner){
                    customers=it as ArrayList<Customer>
                    customerSearch.addAll(
                        customers.map { customer ->
                            CustomerSelect(customer.CustomerID, customer.Name!!)
                        }
                    )
                    updateAdapter(customers)
                    Log.d("CustomerInfoVM","$customers")
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
    private fun updateAdapter(customers: ArrayList<Customer>){
        val customerSearch = ArrayList<CustomerSelect>()
        Log.d("customers","$customers")
        for (i in customers.indices){
            val temp = CustomerSelect(customers[i].CustomerID,customers[i].Name!!)
            customerSearch.add(temp)
        }
        rvAdapter= RvAdapterFindCustomers(requireContext(),customerSearch)
        rvAdapter!!.filterList(customerSearch)


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
            viewLifecycleOwner, Observer {
                printInventoryList =it as ArrayList<FieldReportInventoryCustomData>
                Log.d("testing2","$printInventoryList")
            }
        )

    }
    private fun getDataForToolsList(){
        //reportID
        workOrderViewModel.printDataToolsListByReportID(reportId!!).observe(
            viewLifecycleOwner,
            Observer {
                printToolsList =it as ArrayList<FieldReportToolsCustomData>
                Log.d("testing3","$printToolsList")
            }
        )

    }
    private fun printPDFData(){

        workOrderViewModel.printWorkOrder(reportId!!).observe(
            viewLifecycleOwner,
            Observer {
                val tempData : CustomWorkOrderPDFDATA =it as CustomWorkOrderPDFDATA
                Log.d("testing4","$tempData")
                val pdfMake = PdfFileMaker(requireContext(),tempData)
                pdfMake.printEquipmentList=printEquipmentList
                pdfMake.printInventoryList=printInventoryList
                pdfMake.printToolsList=printToolsList
                pdfMake.printTest()
                Log.d("testing5","$tempData")
            }
        )



    }





    private fun saveFileToInternalStorage(uri: Uri) {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = requireContext().openFileOutput("my_file.html", Context.MODE_PRIVATE)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream?.close()
    }

    fun readFileFromInternalStorage(filename: String): String {
        return try {
            val fileInputStream = requireContext().openFileInput(filename)
            fileInputStream.bufferedReader().use { it.readText() }
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
    fun createPdfFromHtml(htmlString: String, filename: String) {
        val webView = WebView(requireContext())
        webView.webViewClient = object : WebViewClient() {
            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                Log.d("WebViewTEST", "Error while loading HTML content: ${error.description}")
            }
        }
        webView.loadDataWithBaseURL(null, htmlString, "text/HTML", "UTF-8", null)

        val printManager = requireContext().getSystemService(Context.PRINT_SERVICE) as PrintManager
        webView.createPrintDocumentAdapter(filename).also { printAdapter ->
            printManager.print(filename, printAdapter, PrintAttributes.Builder().build())
        }
    }
    fun readFileContent(uri: Uri): String {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            inputStream?.bufferedReader().use { it?.readText() } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }



}