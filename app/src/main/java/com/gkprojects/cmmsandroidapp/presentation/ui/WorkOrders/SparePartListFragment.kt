package com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportInventory
import com.gkprojects.cmmsandroidapp.data.local.entity.Inventory
import com.gkprojects.cmmsandroidapp.presentation.ui.Inventory.InventoryVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.FieldReportInventoryVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.FieldReportInventoryVMFactory
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportInventoryCustomData
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrderInventory
import com.gkprojects.cmmsandroidapp.databinding.FragmentWorkOrderInventoryDataBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.UUID


@OptIn(DelicateCoroutinesApi::class)
class SparePartListFragment : Fragment() {

    private lateinit var binding :FragmentWorkOrderInventoryDataBinding
    private var isObserverSetUp = false
    private var reportId : String?=null

    private lateinit var inventoryVM : InventoryVM
    private lateinit var fieldReportInventoryVM : FieldReportInventoryVM

    private lateinit var fieldInventoryAdapter : AdapterFieldReportRecyclerViewInventory

    private var inventoryList =ArrayList<Inventory>()
    private var inventory = Inventory("",null,null,null,null,null,null,null,null,null)

    private var customInventoryList =ArrayList<FieldReportInventoryCustomData>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentWorkOrderInventoryDataBinding.inflate(inflater,container,false)
        val context = requireContext()
        val repositoryInventory = RepoWorkOrderInventory.getInstance(context)
        val factoryInventory = FieldReportInventoryVMFactory(repositoryInventory)
        fieldReportInventoryVM= ViewModelProvider(this,factoryInventory)[FieldReportInventoryVM::class.java]
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedViewModel: SharedViewModel by activityViewModels()
        inventoryVM = ViewModelProvider(this)[InventoryVM::class.java]

        inventoryVM.getAllInventory(requireContext()).observe(
            viewLifecycleOwner,
            Observer {
                Log.d("inventoryListVM","$it")
                inventoryList= it as ArrayList<Inventory>
                Log.d("inventoryListVM2","$inventoryList")
                populateDropDown()
            }
        )
        fieldInventoryAdapter= AdapterFieldReportRecyclerViewInventory(customInventoryList)
        binding.workOrderInventoryRecyclerview.apply {
            layoutManager=LinearLayoutManager(this.context)
            setHasFixedSize(true)
            adapter=fieldInventoryAdapter
        }
        
        if (!isObserverSetUp) {
            sharedViewModel.reportId.observe(viewLifecycleOwner,
                Observer { id ->
                Log.d("sharedViewModelReportId", "$id")
                reportId = id
                    populateRecyclerviewInventory()
            })
            isObserverSetUp = true
        }
        
        binding.workOrderInventoryAddBtn.setOnClickListener {
            if(inventory.InventoryID=="") {
                Toast.makeText(requireContext(), "Please select a spare part", Toast.LENGTH_SHORT).show()
            }else{
                insertIntoDatabase(inventory)
                populateRecyclerviewInventory()
                binding.workOrderInventorySelection.setText("", false)
                inventory = Inventory("",null,null,null,null,null,null,null,null,null)
            }
        }
    }

    private fun insertIntoDatabase(inventory: Inventory) {
        val tempFieldReportTools=
            FieldReportInventory(UUID.randomUUID().toString(),null,null,null,null,reportId,inventory.InventoryID)

            fieldReportInventoryVM.insert(tempFieldReportTools)


    }

    private fun  populateDropDown(){
        val inventoryAdapter= ArrayAdapterDropDownSpareParts(requireContext(),inventoryList)
        val autocomplete =binding.workOrderInventorySelection
        autocomplete.setAdapter(inventoryAdapter)
        autocomplete.setOnItemClickListener { parent, _, position, _ ->
            val selectedTool = parent.getItemAtPosition(position) as Inventory
            // `selectedTool` is the selected item
            Log.d("inventoryListDrop","$selectedTool")
            inventory=selectedTool
            Log.d("inventoryListDrop2","$inventory")
        }
    }

    private fun populateRecyclerviewInventory() {
        Log.d("reportIDinInventory","$reportId")
        fieldReportInventoryVM.getInventoryByFieldReportID(reportId!!).observe(
            viewLifecycleOwner) {
                if(it.isNotEmpty()){
                    customInventoryList=it as ArrayList<FieldReportInventoryCustomData>
                    Log.d("customInventoryList","$customInventoryList")
                    fieldInventoryAdapter.setData(customInventoryList)
                }
            }

    }


}