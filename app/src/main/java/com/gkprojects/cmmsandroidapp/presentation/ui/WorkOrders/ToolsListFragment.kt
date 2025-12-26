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
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportTools

import com.gkprojects.cmmsandroidapp.data.local.entity.Tools

import com.gkprojects.cmmsandroidapp.presentation.ui.SpecialTools.SpecialToolsVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.FieldReportToolsVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.factoryVM.FieldReportToolsVMFactory
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrderTools

import com.gkprojects.cmmsandroidapp.databinding.FragmentWorkOrderToolsDataBinding
import java.util.UUID


class ToolsListFragment : Fragment() {

    private lateinit var binding :FragmentWorkOrderToolsDataBinding
    private var isObserverSetUp = false
    private var reportId : String?=null

    private lateinit var toolsVM : SpecialToolsVM
    private lateinit var fieldReportToolsVM : FieldReportToolsVM

    private lateinit var fieldToolsAdapter : AdapterFieldReportRecyclerViewTools

    private var toolsList =ArrayList<Tools>()
    private var tool = Tools("",null,null,null,null,null,null,null,null,null,null)

    private var customToolsList =ArrayList<FieldReportToolsCustomData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentWorkOrderToolsDataBinding.inflate(inflater,container,false)

        val context = requireContext()
        val repositoryTools = RepoWorkOrderTools.getInstance(context)
        val factoryTools = FieldReportToolsVMFactory(repositoryTools)
        fieldReportToolsVM= ViewModelProvider(this,factoryTools)[FieldReportToolsVM::class.java]
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedViewModel: SharedViewModel by activityViewModels()



        toolsVM = ViewModelProvider(this)[SpecialToolsVM::class.java]


        toolsVM.getTools(requireContext()).observe(
            viewLifecycleOwner)
             {tools->
                Log.d("toolsListVM","$tools")
                toolsList= tools as ArrayList<Tools>
                Log.d("toolsListVM2","$toolsList")
                populateDropDown()
            }

        fieldToolsAdapter = AdapterFieldReportRecyclerViewTools(customToolsList)
        binding.workOrderToolRecyclerview.apply {
            adapter=fieldToolsAdapter
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this.context)
        }
        if (!isObserverSetUp) {
            sharedViewModel.reportId.observe(viewLifecycleOwner,
                Observer {
                    reportId=it
                    populateRecyclerviewTools()
                })

            isObserverSetUp = true
        }


        binding.workOrderToolAddBtn.setOnClickListener {

            if(tool.ToolsID==""){
                Toast.makeText(requireContext(),"Please select a tool",Toast.LENGTH_SHORT).show()
            }else {
                insertIntoDatabase(tool)
                populateRecyclerviewTools()
                binding.workOrderToolSelection.setText("", false)
                tool= Tools("",null,null,null,null,null,null,null,null,null,null)
            }
            //autoCompleteTextView.setText("", false)
        }




    }

    private fun insertIntoDatabase(tools: Tools) {

        val tempFieldReportTools=
            FieldReportTools(UUID.randomUUID().toString(),null,reportId,tools.ToolsID,null,null,null)

        fieldReportToolsVM.insert( tempFieldReportTools)




    }
    private fun populateDropDown(){
        val toolsAdapter = ArrayAdapterDropDownTools(requireContext(),toolsList)
        val autoCompleteTextView = binding.workOrderToolSelection
        autoCompleteTextView.setAdapter(toolsAdapter)
        //autoCompleteTextView.isFocusableInTouchMode = false

        autoCompleteTextView.setOnItemClickListener { parent, _, position, _ ->
            val selectedTool = parent.getItemAtPosition(position) as Tools
            // `selectedTool` is the selected item
            Log.d("toolsListDrop","$selectedTool")
            tool=selectedTool
            Log.d("toolsListDrop2","$tool")
        }
    }

    private fun populateRecyclerviewTools(){
        Log.d("reportIDinTools","$reportId")
        fieldReportToolsVM.getTollsByReportID(reportId!!).observe(viewLifecycleOwner){
            if(it.isNotEmpty()){
                customToolsList =it as ArrayList<FieldReportToolsCustomData>
                Log.d("customToolsList","$customToolsList")
                fieldToolsAdapter.setData(customToolsList)
            }

        }
    }


}