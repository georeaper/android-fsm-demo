package com.gkprojects.cmmsandroidapp.presentation.ui

import android.R
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.presentation.adapter.CustomizedFieldUniversalRVA
import com.gkprojects.cmmsandroidapp.data.local.entity.Settings
import com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.MasterData.ColorItem
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SettingsViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SettingsViewModelFactory
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.SettingsRepository
import com.gkprojects.cmmsandroidapp.databinding.CustomizedFieldRowUniversalBinding
import com.gkprojects.cmmsandroidapp.databinding.GenericSettingsFragmentBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.util.UUID

class GenericSettingsFragment : Fragment() {
    private lateinit var binding: GenericSettingsFragmentBinding
    private lateinit var bindingRecyclerViewItems: CustomizedFieldRowUniversalBinding
    private lateinit var viewModel: SettingsViewModel
    private lateinit var rvAdapter: CustomizedFieldUniversalRVA<Settings>
    private lateinit var dropDownAutoComplete: MaterialAutoCompleteTextView
    private lateinit var recyclerview: RecyclerView

    private val colorList = listOf(
        ColorItem("Light Red", "#FF5733"), ColorItem("Light Green", "#33FF57"),
        ColorItem("Purple Blue", "#5733FF"), ColorItem("Light Orange", "#FFC300"),
        ColorItem("Red", "#E74C3C"), ColorItem("Orange", "#F39C12"), ColorItem("Blue", "#3498DB"),
        ColorItem("White", "#FFFFFF"), ColorItem("Black", "#000000"), ColorItem("Grey", "#808080")
    )

    private val args by lazy {
        requireArguments()
    }

    private val settingKey by lazy { args.getString("settingKey") ?: "Unknown" }
    private val settingTitle by lazy { args.getString("settingTitle") ?: settingKey }
    private val settingDescription by lazy { args.getString("settingDescription") ?: "$settingKey Values" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val context = requireContext()
        val repository = SettingsRepository.getInstance(context)
        val factory = SettingsViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]
        binding = GenericSettingsFragmentBinding.inflate(inflater, container, false)
        bindingRecyclerViewItems= CustomizedFieldRowUniversalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupDropdown()
        setupRecyclerView()
        setupObservers()
        setupAddButton()
        loadData()
    }

    private fun setupDropdown() {
        val colorNames = colorList.map { it.name }
        dropDownAutoComplete = binding.colorAutocomplete
        dropDownAutoComplete.setAdapter(
            ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, colorNames)
        )
    }

    private fun setupRecyclerView() {
        recyclerview = binding.recyclerView

        rvAdapter = CustomizedFieldUniversalRVA(
            items = arrayListOf()
        ) { item, itemBinding, position ->
            val setting = item as Settings
            val color = try {
                Color.parseColor(setting.SettingsStyle?.takeIf { it.isNotBlank() } ?: "#00000000") // Transparent fallback
            } catch (e: IllegalArgumentException) {
                Color.TRANSPARENT
            }
            itemBinding.customizedUniversalColorView.setBackgroundColor(color)
            itemBinding.customizedUniversalTextView.text = setting.SettingsValue
            //itemBinding.customizedUniversalColorView.setBackgroundColor(Color.parseColor(setting.SettingsStyle))
            itemBinding.customizedUniversalDeleteButton.setOnClickListener {
                viewModel.deleteSettings(setting)
                rvAdapter.removeItem(position)
            }
        }

        recyclerview.layoutManager = LinearLayoutManager(requireContext())
        recyclerview.adapter = rvAdapter
    }

    private fun setupObservers() {
        viewModel.settingsData.observe(viewLifecycleOwner) { settings ->
            rvAdapter.setData(ArrayList(settings))
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let { Toast.makeText(context, "Error: $it", Toast.LENGTH_SHORT).show() }
        }

        viewModel.insertSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Insert successful!", Toast.LENGTH_SHORT).show()
                loadData()
            } else {
                Toast.makeText(context, "Insert failed.", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.deleteSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Delete successful!", Toast.LENGTH_SHORT).show()
                loadData()
            }
        }
    }

    private fun setupAddButton() {
        binding.addButton.setOnClickListener {
            val name = binding.inputText.text.toString()
            val selectedColorName = dropDownAutoComplete.text.toString()
            val selectedHexCode = colorList.find { it.name == selectedColorName }?.hex ?: "#FFFFFF"
            if (name.isNotBlank()) {
                val setting = Settings(
                    UUID.randomUUID().toString(), null, settingKey, name, selectedHexCode,
                    settingDescription, null, null, null
                )
                rvAdapter.addItem(setting)
                viewModel.insertSettings(setting)
                binding.inputText.text?.clear()
                dropDownAutoComplete.text = null
            }
        }
    }

    private fun loadData() {
        viewModel.loadSettingsByKey(settingKey)
    }
}
