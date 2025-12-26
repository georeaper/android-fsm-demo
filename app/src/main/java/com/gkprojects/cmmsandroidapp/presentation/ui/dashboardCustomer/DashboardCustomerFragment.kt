package com.gkprojects.cmmsandroidapp.presentation.ui.dashboardCustomer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.entity.Customer
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerContractsDataClass
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerEquipmentDataClass
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerTechnicalCasesDataClass
import com.gkprojects.cmmsandroidapp.presentation.ui.Customers.CustomerFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.Customers.EditCustomerFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.Equipments.EquipmentFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.WorkOrdersFragment
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CustomerVM
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.databinding.FragmentDashboardCustomerBinding
import com.google.android.material.appbar.MaterialToolbar


class DashboardCustomerFragment : Fragment() {

    private lateinit var binding: FragmentDashboardCustomerBinding
    private lateinit var customerViewModel: CustomerVM
    private var customerList : Customer? = null
    private lateinit var toolbar: MaterialToolbar

    private lateinit var equipmentRv: RecyclerView
    private lateinit var equipmentAdapter: CustomerDashboardAdapter

    private lateinit var contractRv: RecyclerView
    private lateinit var contractAdapter: CustomerDashboardAdapterContracts

    private lateinit var workOrderRv: RecyclerView

    private lateinit var technicalCaseRv: RecyclerView
    private lateinit var technicalCasesAdapter : CustomerDashboardAdapterTechnicalCase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
//        bottomNavigationView.selectedItemId=R.id.action_home
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDashboardCustomerBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customerViewModel= ViewModelProvider(this)[CustomerVM::class.java]
        val args =this.arguments
        val customerId= args!!.getString("id")
        toolbar = requireActivity().findViewById(R.id.topAppBar)
        toolbar.title = " Dashboard Customers"
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24)
        val navigationIcon = toolbar.navigationIcon
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener {
            toolbar.navigationIcon = navigationIcon
            val fragmentManager =parentFragmentManager
            val fragmentTransaction=fragmentManager.beginTransaction()
            val fragment = CustomerFragment()
            fragmentTransaction.replace(R.id.frameLayout1,fragment)
            fragmentTransaction.commit()
        }
        setEquipmentList(customerId!!)
        setContracts(customerId!!)
        setTechnicalCases(customerId!!)

        customerViewModel.getCustomerDataByID(requireContext(),customerId).observe(viewLifecycleOwner,
            Observer {
                customerList=it
                setContactInfo(it)
            })


        Log.d("Dashboardid",customerId.toString())


        val customerInformation : ImageButton= view.findViewById(R.id.ibDashboardGeneralInformationImageButton)
        val assetBtn : ImageButton =view.findViewById(R.id.imageDashboardButonAssets)
        val workOrderBtn : ImageButton =view.findViewById(R.id.imageDashboardButtonWorkOrders)
        val contractBtn : ImageButton =view.findViewById(R.id.imageDashboardButonContracts)
        val technicalCaseBtn : ImageButton =view.findViewById(R.id.imageDashboardButonTechnicalCases)
        val notesBtn : ImageButton =view.findViewById(R.id.imageDashboardButonNotes)

        customerInformation.setOnClickListener {
            sentIDtoFragments(customerId!!, EditCustomerFragment())
        }
        assetBtn.setOnClickListener {
            sentIDtoFragments(customerId!!, EquipmentFragment())
        }
        workOrderBtn.setOnClickListener {
            sentIDtoFragments(customerId!!, WorkOrdersFragment())
        }
//        contractBtn.setOnClickListener {
//            sentIDtoFragments(customerId!!, ContractFragment())
//        }
//        technicalCaseBtn.setOnClickListener {
//            sentIDtoFragments(customerId!!, CasesFragment())
//        }




    }
    private fun setContactInfo(customerList : Customer){
        binding.textviewCustomerNameDashboardCustomer.text=customerList.Name
        binding.textviewCustomerNameDashboardAddress.text=customerList.Address
        binding.textviewCustomerNameDashboardEmail.text=customerList.Email
        binding.textviewCustomerNameDashboardPhone.text=customerList.Phone
    }

    private fun sentIDtoFragments(id: String ,fragment :Fragment){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val bundle = Bundle()
        bundle.putString("id",id)
        fragment.arguments = bundle
        fragmentTransaction.replace(R.id.frameLayout1, fragment)
        fragmentTransaction.commit()

    }

    private fun setEquipmentList ( id :String){
        var rv =binding.recyclerviewCustomerdashboardAsset
        equipmentAdapter= CustomerDashboardAdapter(ArrayList<DashboardCustomerEquipmentDataClass>())
        rv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = equipmentAdapter
        }
        customerViewModel.getCustomerEquipmentsPerCustomer(requireContext(),id).observe(viewLifecycleOwner,
            Observer {
                var tempList = ArrayList<DashboardCustomerEquipmentDataClass>()
                for(i in it.indices){
                    tempList.add(it[i])

                }
                equipmentAdapter.setData(tempList)
            })

    }
    private fun setContracts(id :String){
        val rvContracts =binding.recyclerviewCustomerdashboardContracts
        contractAdapter= CustomerDashboardAdapterContracts(ArrayList<DashboardCustomerContractsDataClass>())
        rvContracts.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this.context)
            adapter=contractAdapter
        }
        customerViewModel.getCustomerContractsPerCustomer(requireContext(),id).observe(viewLifecycleOwner,
            Observer {
                var contractList = ArrayList<DashboardCustomerContractsDataClass>()
                for(i in it.indices){
                    contractList.add(it[i])
                }
                contractAdapter.setData(contractList)
            })

    }
    private fun setTechnicalCases(id :String){
        val rvTechnicalCase=binding.recyclerviewCustomerdashboardTechnicalCases
        technicalCasesAdapter= CustomerDashboardAdapterTechnicalCase(ArrayList<DashboardCustomerTechnicalCasesDataClass>())
        rvTechnicalCase.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this.context)
            adapter=technicalCasesAdapter
        }
        customerViewModel.getCustomerTechnicalCasesPerCustomer(requireContext(),id).observe(viewLifecycleOwner,
            Observer {
                var technicalCaseList= ArrayList<DashboardCustomerTechnicalCasesDataClass>()
                for (i in it.indices){
                    technicalCaseList.add(it[i])
                }
                technicalCasesAdapter.setData(technicalCaseList)
            })
    }


}