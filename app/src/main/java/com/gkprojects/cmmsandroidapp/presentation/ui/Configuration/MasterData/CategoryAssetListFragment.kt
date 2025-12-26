package com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.MasterData

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Color
import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.presentation.adapter.CustomizedFieldUniversalRVA
import com.gkprojects.cmmsandroidapp.data.local.entity.CategoryAsset

import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CategoryViewModel

import com.gkprojects.cmmsandroidapp.data.repositoryImpl.CategoryRepository
import com.gkprojects.cmmsandroidapp.databinding.FragmentCategoryAssetListBinding
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CategoryViewModelFactory
import java.util.UUID


class CategoryAssetListFragment : Fragment() {
    private lateinit var binding : FragmentCategoryAssetListBinding
    private var categoryList = ArrayList<CategoryAsset>()
    private lateinit var rvAdapter : CustomizedFieldUniversalRVA<CategoryAsset>
    private lateinit var recyclerview : RecyclerView

    private lateinit var viewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val repository = CategoryRepository.getInstance(context)
        val factory = CategoryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[CategoryViewModel::class.java]
        binding=FragmentCategoryAssetListBinding.inflate(inflater,container,false)
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
//            itemBinding.customizedUniversalColorView.setBackgroundColor(Color.parseColor(setting.Style))
            itemBinding.customizedUniversalDeleteButton.setOnClickListener {
                viewModel.deleteCategories(setting)
                rvAdapter.removeItem(position)
            }
        }

        recyclerview.layoutManager= LinearLayoutManager(requireContext())
        recyclerview.adapter=rvAdapter
        //ViewModel call class
        //Must be Initialized at the top
        viewModel.categoryData.observe(viewLifecycleOwner, Observer { data->
            Log.d("LoggedData","$data")
            rvAdapter.setData(data as ArrayList<CategoryAsset>)
        })
        viewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show()
                Log.d("LoggedData", it)
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
            val inputData= CategoryAsset(UUID.randomUUID().toString(),null,value,null,null,null,null)
            rvAdapter.addItem(inputData)
            viewModel.insertCategory(inputData)
        }



    }

    private fun loadData() {
        viewModel.loadCategories()
    }


}