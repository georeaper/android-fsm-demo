package com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.MasterData

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.presentation.adapter.CustomizedFieldUniversalRVA
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.SettingsRepository
import com.gkprojects.cmmsandroidapp.databinding.FragmentWorkOrderReportTypeListBinding
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SettingsViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SettingsViewModelFactory
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.util.UUID
import com.gkprojects.cmmsandroidapp.data.local.entity.Settings as AppSettings


class WorkOrderReportTypeListFragment : Fragment() {
   private lateinit var binding : FragmentWorkOrderReportTypeListBinding
   private var reportTypeList = ArrayList<AppSettings>()
   private lateinit var rvAdapter : CustomizedFieldUniversalRVA<AppSettings>
   private lateinit var recyclerview :RecyclerView
   private lateinit var dropDownAutoComplete : MaterialAutoCompleteTextView
   private lateinit var viewModel: SettingsViewModel
   private val settingKey="ReportType"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = requireContext()
        val repository = SettingsRepository.getInstance(context)
        val factory = SettingsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]
        binding=FragmentWorkOrderReportTypeListBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorList = listOf(
            ColorItem("Light Red", "#FF5733"),
            ColorItem("Light Green", "#33FF57"),
            ColorItem("Purple Blue", "#5733FF"),
            ColorItem("Light Orange", "#FFC300"),
            ColorItem("Mint Green", "#DAF7A6"),
            ColorItem("Dark Red", "#900C3F"),
            ColorItem("Dark Purple", "#581845"),
            ColorItem("Dark Green", "#28B463"),
            ColorItem("Blue", "#3498DB"),
            ColorItem("Orange", "#F39C12"),
            ColorItem("Red", "#E74C3C"),
            ColorItem("Purple", "#8E44AD"),
            ColorItem("Teal", "#16A085"),
            ColorItem("Dark Blue", "#2C3E50"),
            ColorItem("Dark Orange", "#D35400"),
            ColorItem("Black", "#000000"),
            ColorItem("White", "#FFFFFF"),
            ColorItem("Grey", "#808080")
        )

        loadData()
        recyclerview=binding.recyclerView
        rvAdapter = CustomizedFieldUniversalRVA(
            items = arrayListOf()
        ) { item, itemBinding, position ->
            val setting = item
            val color = try {
                Color.parseColor(setting.SettingsStyle?.takeIf { it.isNotBlank() } ?: "#00000000") // Transparent fallback
            } catch (e: IllegalArgumentException) {
                Color.TRANSPARENT
            }

            itemBinding.customizedUniversalColorView.setBackgroundColor(color)
            itemBinding.customizedUniversalTextView.text = setting.SettingsValue
//            itemBinding.customizedUniversalColorView.setBackgroundColor(Color.parseColor(setting.SettingsStyle))
            itemBinding.customizedUniversalDeleteButton.setOnClickListener {
                viewModel.deleteSettings(setting)
                rvAdapter.removeItem(position)
            }
        }

//        rvAdapter=CustomizedFieldUniversalRVA(items= reportTypeList,
//            layoutId = R.layout.customized_field_row_universal){
//            item,
//            view->
//            view.findViewById<TextView>(R.id.customizedUniversalTextView).text =item.SettingsValue
//            view.findViewById<View>(R.id.customizedUniversalColorView).setBackgroundColor(Color.parseColor(item.SettingsStyle)) //(Color.parseColor("#FFFFFF"))//
//            view.findViewById<Button>(R.id.customizedUniversalDeleteButton).setOnClickListener {
//                val position = recyclerview.getChildAdapterPosition(view)
//                rvAdapter.removeItem(position)
//                val deleteItem=rvAdapter.getItemAtPosition(position)
//                Log.d("DeletedItem","$deleteItem")
//
//                if (deleteItem != null) {
//                    viewModel.deleteSettings(deleteItem)
//                }
//
//            }
//        }
        recyclerview.layoutManager=LinearLayoutManager(requireContext())
        recyclerview.adapter=rvAdapter
        //ViewModel call class
        //Must be Initialized at the top
        viewModel.settingsData.observe(viewLifecycleOwner, Observer { settings ->
            rvAdapter.setData(settings as ArrayList<AppSettings>)
        })
        // Observe LiveData for errors
        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
            }
        })

        val colorNames = colorList.map { it.name } // Extract only the names

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, colorNames)
        dropDownAutoComplete = binding.colorAutocomplete
        dropDownAutoComplete.setAdapter(adapter)


        // Observe LiveData for insert success
        viewModel.insertSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(context, "Insert successful!", Toast.LENGTH_SHORT).show()
                loadData()
                // Optionally refresh your data from the database here
            } else {
                Toast.makeText(context, "Insert failed.", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel.deleteSuccess.observe(viewLifecycleOwner,Observer{success->
            if (success){
                Toast.makeText(context, "Delete successful!", Toast.LENGTH_SHORT).show()
                loadData()
            }
            else{
                Toast.makeText(context, "Insert failed.", Toast.LENGTH_SHORT).show()
            }
        })
        var selectedHexCode: String? = null
        dropDownAutoComplete.setOnItemClickListener { parent, view, position, id ->
            val selectedColorName = parent.getItemAtPosition(position) as String
            val selectedColor = colorList.find { it.name == selectedColorName }
            selectedColor?.let {
                selectedHexCode = it.hex
            }
        }


        binding.addButton.setOnClickListener {
            val value = binding.inputText.text.toString()
            val hex = selectedHexCode ?: "#FFFFFF" // Default to white if no color is selected
            //val color ="#FFFFFF"// binding.colorAutocomplete.text.toString()
            val inputData= AppSettings(UUID.randomUUID().toString(),null,settingKey,value,hex,"ReportType Values",null,null,null)
            rvAdapter.addItem(inputData)
            viewModel.insertSettings(inputData)
        }




    }
    // WorkOrderReportTypeListFragment.kt
    private fun loadData() {

        viewModel.loadSettingsByKey(settingKey)
    }




}
data class ColorItem(val name: String, val hex: String)

