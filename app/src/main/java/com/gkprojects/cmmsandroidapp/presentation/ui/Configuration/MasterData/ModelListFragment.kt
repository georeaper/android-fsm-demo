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
import com.gkprojects.cmmsandroidapp.data.local.entity.ModelAsset
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.ModelRepository
import com.gkprojects.cmmsandroidapp.databinding.FragmentModelListBinding
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.ModelViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.ModelViewModelFactory
import java.util.UUID


class ModelListFragment : Fragment() {
    private lateinit var binding: FragmentModelListBinding
    private var modelList = ArrayList<ModelAsset>()
    private lateinit var rvAdapter : CustomizedFieldUniversalRVA<ModelAsset>
    private lateinit var recyclerview : RecyclerView

    private lateinit var viewModel: ModelViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val context = requireContext()
        val repository = ModelRepository.getInstance(context)
        val factory = ModelViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ModelViewModel::class.java]
        binding= FragmentModelListBinding.inflate(inflater,container,false)
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
            itemBinding.customizedUniversalTextView.text = setting.Name
            itemBinding.customizedUniversalDeleteButton.setOnClickListener {
                //viewModel.deleteSettings(setting)
                viewModel.deleteModelAsset(setting)
                rvAdapter.removeItem(position)
            }
        }

        recyclerview.layoutManager= LinearLayoutManager(requireContext())
        recyclerview.adapter=rvAdapter
        //ViewModel call class
        //Must be Initialized at the top
        viewModel.modelAssetData.observe(viewLifecycleOwner, Observer { data->
            Log.d("LoggedData","$data")
            rvAdapter.setData(data as ArrayList<ModelAsset>)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                Log.d("LoggedData","$it")
            }
        })
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
            val inputData= ModelAsset(UUID.randomUUID().toString(),null,value,null,null,null,null)
            rvAdapter.addItem(inputData)
            viewModel.insertModelAsset(inputData)
        }



    }

    private fun loadData() {
        viewModel.loadAllModelAsset()
    }

}


