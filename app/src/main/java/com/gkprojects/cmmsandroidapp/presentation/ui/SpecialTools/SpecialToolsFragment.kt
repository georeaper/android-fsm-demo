package com.gkprojects.cmmsandroidapp.presentation.ui.SpecialTools

import ConfirmDeleteDialog
import android.app.AlertDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkprojects.cmmsandroidapp.data.local.entity.Tools
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult
import com.gkprojects.cmmsandroidapp.core.utils.UiOperationMessages
import com.gkprojects.cmmsandroidapp.databinding.DialogToolsInsertBinding
import com.gkprojects.cmmsandroidapp.databinding.FragmentSpecialToolsBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.Locale
import java.util.UUID


class SpecialToolsFragment : Fragment() {
    private lateinit var binding :FragmentSpecialToolsBinding
    private lateinit var adapterTools : AdapterSpecialToolsRecyclerView
    private var toolsList =ArrayList<Tools>()
    private lateinit var specialToolsVM : SpecialToolsVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSpecialToolsBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //search visibility set to GONE
        binding.filterIcon.visibility=View.GONE

        specialToolsVM= ViewModelProvider(this)[SpecialToolsVM::class.java]

        adapterTools = AdapterSpecialToolsRecyclerView(toolsList)

        binding.specialToolsFloatingBtn.setOnClickListener {
            openDialogInsertTools()
        }

        binding.specialToolsRecyclerview.apply {
            adapter=adapterTools
            setHasFixedSize(true)
            layoutManager= LinearLayoutManager(this.context)
        }
        adapterTools.setOnLongClickListener(object :AdapterSpecialToolsRecyclerView.OnLongClickListener{
            override fun onLongClick(position: Int, model: Tools) {
                //openDialogDisplayTools(model)
                ConfirmDeleteDialog(
                    message = "Delete ${model.Title} - ${model.SerialNumber}?",
                    onConfirm = {
                        specialToolsVM.delete(
                            id = model.ToolsID,
                            context = requireContext(),
                        ) { result ->
                            UiOperationMessages.handleResult(
                                context = requireContext(),
                                result = result,
                                rootView = binding.root,
                                successMessage = "Special tools deleted successfully"
                            )
                            if (result is OperationResult.Success){
                                adapterTools.removeItemAt(position)
//                                    val updatedList = ArrayList(contractList) // make a copy
//                                    updatedList.remove(model) // remove the deleted item
//                                    contractAdapter.setData(updatedList)



                            }
                        }
                    }
                ).show(childFragmentManager, "confirmDeleteEquipment")
            }

        })

        try {
            specialToolsVM.getTools(requireContext()).observe(viewLifecycleOwner, Observer {
                Log.d("specialTools","$it")
                val tempList : ArrayList<Tools> = it as ArrayList<Tools>
                Log.d("specialTools2","$tempList")
                toolsList=tempList
                Log.d("specialTools3","$toolsList")

                adapterTools.setData(toolsList)
            })
        }catch (e :Exception){
            Log.d("toolsExceptionCatch","$e")
        }
        val searchBar = binding.searchEditTextSpecialTools
        binding.filterIcon.setOnClickListener {
            //Calling filter dialog here
        }
        val searchIcon=binding.searchIcon

        searchIcon.setOnClickListener {
            searchBar.text.clear()
        }

        searchBar.addTextChangedListener(object:TextWatcher{
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

            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())

            }

        })


    }



    private fun openDialogInsertTools() {
        ToolsInsertDialog { tools ->
            insertToolsIntoDatabase(tools)
            toolsList.add(tools)
            adapterTools.setData(toolsList)
        }.show(parentFragmentManager, "ToolsInsertDialog")

    }

     fun openDialogDisplayTools(tools: Tools) {
        val dialogBinding = DialogToolsInsertBinding.inflate(LayoutInflater.from(requireContext()))

        // Set the fields in the dialog to the values from the selected Tools object
        dialogBinding.dialogToolsInsertEditTextTitle.setText(tools.Title)
        dialogBinding.dialogToolsInsertEditTextSerialNumber.setText(tools.SerialNumber)
        dialogBinding.dialogToolsInsertEditTextModel.setText(tools.Model)
        dialogBinding.dialogToolsInsertEditTextManufacturer.setText(tools.Manufacturer)
        dialogBinding.dialogToolsInsertEditTextDescription.setText(tools.Description)
        dialogBinding.calibrationDateEditText.setText(tools.CalibrationDate)

        // Disable the EditTexts so the user can't edit the values
        dialogBinding.dialogToolsInsertEditTextTitle.isEnabled = false
        dialogBinding.dialogToolsInsertEditTextSerialNumber.isEnabled = false
        dialogBinding.dialogToolsInsertEditTextModel.isEnabled = false
        dialogBinding.dialogToolsInsertEditTextManufacturer.isEnabled = false
        dialogBinding.dialogToolsInsertEditTextDescription.isEnabled = false
        dialogBinding.calibrationDateEditText.isEnabled = false

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialog.show()

        // Set the dimensions of the dialog
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
    }

    private fun insertToolsIntoDatabase(tools : Tools){
        specialToolsVM.insertTools(requireContext(),tools)

    }
    private fun filterList(query:String){
        if (query!=null){
            val filteredList= ArrayList<Tools>()
            for (i in toolsList){
                if (i.Title!!.lowercase(Locale.ROOT).contains(query)||i.Description!!.lowercase(Locale.ROOT).contains(query))
                    filteredList.add(i)
                Log.d("filteredInventory", filteredList.toString())
            }
            if (filteredList.isEmpty() ){
                adapterTools.setData(filteredList)
                Toast.makeText(context,"Empty List", Toast.LENGTH_SHORT).show()

            }else{
                adapterTools.setData(filteredList)
            }
        }

    }
}