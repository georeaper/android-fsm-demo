package com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.MasterData

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.presentation.adapter.CustomizedFieldUniversalRVA
import com.gkprojects.cmmsandroidapp.data.local.entity.Manufacturer
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.ManufacturerRepository
import com.gkprojects.cmmsandroidapp.databinding.FragmentManufacturerListBinding
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.ManufacturerViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.ManufacturerViewModelFactory
import java.util.UUID


class ManufacturerListFragment : Fragment() {
    private lateinit var binding: FragmentManufacturerListBinding
    private var manufacturerList = ArrayList<Manufacturer>()
    private lateinit var rvAdapter : CustomizedFieldUniversalRVA<Manufacturer>
    private lateinit var recyclerview : RecyclerView

    private lateinit var viewModel: ManufacturerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val context = requireContext()
        val repository = ManufacturerRepository.getInstance(context)
        val factory = ManufacturerViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ManufacturerViewModel::class.java]
        binding= FragmentManufacturerListBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview=binding.recyclerView
        rvAdapter = CustomizedFieldUniversalRVA(
            items = arrayListOf()
        ) { item, itemBinding, position ->

            val setting = item
            val color = try {
                Color.parseColor(setting.Style?.takeIf { it.isNotBlank() } ?: "#00000000") // Transparent fallback
            } catch (e: IllegalArgumentException) {
                Color.TRANSPARENT
            }
            itemBinding.customizedUniversalColorView.setBackgroundColor(color)

            itemBinding.customizedUniversalColorView.setBackgroundColor(color)
            itemBinding.customizedUniversalTextView.text = setting.Name
            itemBinding.customizedUniversalDeleteButton.setOnClickListener {
                viewModel.deleteManufacturer(setting)
                rvAdapter.removeItem(position)
            }
        }

        recyclerview.layoutManager= LinearLayoutManager(requireContext())
        recyclerview.adapter=rvAdapter
        //ViewModel call class
        //Must be Initialized at the top
        viewModel.manufacturerData.observe(viewLifecycleOwner, Observer { data->
            Log.d("LoggedData","$data")
            rvAdapter.setData(data as ArrayList<Manufacturer>)
        })
        viewModel.error.observe(viewLifecycleOwner) {errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                Log.d("LoggedData","$it")
            }
        }
        loadData()
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

        binding.addButton.setOnClickListener {
            val value=binding.inputText.text.toString()
            val inputData= Manufacturer(UUID.randomUUID().toString(),null,value,null,null,null,null)
            rvAdapter.addItem(inputData)
            viewModel.insertManufacturer(inputData)
        }



    }

    private fun loadData() {
        viewModel.loadManufacturer()
    }

}


