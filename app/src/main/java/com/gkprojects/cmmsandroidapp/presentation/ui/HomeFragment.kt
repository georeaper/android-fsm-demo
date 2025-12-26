package com.gkprojects.cmmsandroidapp.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.gkprojects.cmmsandroidapp.presentation.adapter.MainOverviewAdapter
import com.gkprojects.cmmsandroidapp.data.local.dto.AppData

import com.gkprojects.cmmsandroidapp.data.local.dto.OverviewMainData

import com.gkprojects.cmmsandroidapp.presentation.ui.Contracts.ContractsVM
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoContracts
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CasesVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.ContractsViewModelFactory

import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.databinding.FragmentHomeBinding
import com.google.android.material.badge.ExperimentalBadgeUtils

import kotlin.Exception



@ExperimentalBadgeUtils
class HomeFragment : Fragment() {

    private lateinit var contractViewModel: ContractsVM
    private lateinit var ticketsViewModel : CasesVM
    private lateinit var workOrdersRecyclerView: RecyclerView
    private var ticketList = ArrayList<OverviewMainData>()
    private lateinit var adapterHomeWorkOrder: MainOverviewAdapter

    private lateinit var binding:FragmentHomeBinding

    private var isReadPermissionGranted =false
    private var isWritePermissionGranted =false
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        isReadPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissionGranted
        isWritePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWritePermissionGranted
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }


    override fun   onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val repositoryContract = RepoContracts.getInstance(context)
        val factory = ContractsViewModelFactory(repositoryContract)
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        contractViewModel = ViewModelProvider(this, factory)[ContractsVM::class.java]

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val activity =requireActivity()
        val drawerLayout = activity.findViewById<DrawerLayout>(R.id.DrawLayout)
        val toggle = ActionBarDrawerToggle(activity, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("testAppData","${AppData.userId}")

        ticketList.clear()




        ticketsViewModel= ViewModelProvider(this)[CasesVM::class.java]

        workOrdersRecyclerView =view.findViewById(R.id.recyclerViewHomeWorkOrder)


        adapterHomeWorkOrder= context?.let { MainOverviewAdapter(it,ArrayList<OverviewMainData>()) }!!

        ticketsViewModel = ViewModelProvider(this)[CasesVM::class.java]

        try {
            context?.let {
                ticketsViewModel.getOverviewData(it).observe(viewLifecycleOwner,Observer{it->
                    ticketList = it as ArrayList<OverviewMainData>

                    datasetRv(workOrdersRecyclerView,adapterHomeWorkOrder,ticketList)
                })
            }

        }catch (e:Exception){
            Log.d("ticketOverview",e.toString())
        }

    }

    private fun datasetRv(recyclerview : RecyclerView, adapterRv : MainOverviewAdapter, input :ArrayList<OverviewMainData>){
        Log.d("debugHomeFragmentData",input.toString())
        recyclerview.apply { setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = adapterRv }
        adapterRv.setData(input)

    }

    private fun requestPermission(){

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