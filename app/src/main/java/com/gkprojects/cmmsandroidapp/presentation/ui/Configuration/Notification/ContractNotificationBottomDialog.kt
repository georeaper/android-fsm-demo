package com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.Notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkprojects.cmmsandroidapp.data.local.dto.ContractRemindersSettings
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.SettingsRepository
import com.gkprojects.cmmsandroidapp.databinding.DialogFragmentNotificationContractBinding
import com.gkprojects.cmmsandroidapp.presentation.adapter.notification.ContractReminderAdapter
import com.gkprojects.cmmsandroidapp.presentation.common.ReminderPickerDialogFragment
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SettingsViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SettingsViewModelFactory
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.serialization.json.Json


class ContractNotificationBottomDialog : BottomSheetDialogFragment() {

    private var _binding: DialogFragmentNotificationContractBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: ContractReminderAdapter
    private val reminders = mutableListOf<String>()

    private lateinit var settingsViewmodel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogFragmentNotificationContractBinding.inflate(inflater, container, false)
        val context = requireContext()
        val repositorySettings = SettingsRepository.getInstance(context)
        val factorySettingsVM = SettingsViewModelFactory(repositorySettings)
        settingsViewmodel= ViewModelProvider(this,factorySettingsVM)[SettingsViewModel::class.java]
        return binding.root
    }
    override fun onStart() {
        super.onStart()

        val bottomSheet = dialog?.findViewById<View>(R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true

            // Optional: make it full screen
            val layoutParams = it.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            it.layoutParams = layoutParams
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsViewmodel.getRemindersOnContracts()
        val recyclerview =binding.contractRecyclerviewNotification
        recyclerview.layoutManager= LinearLayoutManager(requireContext())


        // Setup adapter
        adapter = ContractReminderAdapter(
            reminders = reminders,
            onAddClicked = {
                openReminderPickerDialog()
            },
            onDelete = { selected ->
                //reminders.add(selected)
                // later: edit reminder if needed
                Log.d("debugSelected",selected)
                reminders.remove(selected)

                settingsViewmodel.addOrUpdateContractReminders(
                    ContractRemindersSettings(
                        reminders.joinToString(","),
                        binding.contractSwitchInsert.isChecked,
                        binding.contractSwitchUpdate.isChecked,
                        binding.contractSwitchDelete.isChecked
                    )
                )
                adapter.notifyDataSetChanged()
            }
        )

        recyclerview.adapter = adapter
        setupObservers()
        Log.d("reminders",reminders.toString())

       

        // Listen for picker result
        parentFragmentManager.setFragmentResultListener("reminder_picker_result", this) { _, bundle ->
            val value = bundle.getInt("value")
            val unit = bundle.getString("unit")

            reminders.add("$value $unit before")
            adapter.notifyDataSetChanged()
        }

        binding.closeBottomSheetDialog.setOnClickListener {
            dismiss()
        }
        binding.saveBottomSheetContractBtn.setOnClickListener {
            val settingsValue = ContractRemindersSettings(
                reminders = reminders.joinToString(","),
                insert = binding.contractSwitchInsert.isChecked,
                update = binding.contractSwitchUpdate.isChecked,
                delete = binding.contractSwitchDelete.isChecked
            )

            settingsViewmodel.addOrUpdateContractReminders(settingsValue)

        }
    }

    private fun openReminderPickerDialog() {
        ReminderPickerDialogFragment().show(parentFragmentManager, "ReminderPicker")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
    private fun setupObservers() {
        val json = Json {
            prettyPrint = true
            encodeDefaults = true
            ignoreUnknownKeys = true
        }
        settingsViewmodel.settingsData.observe(viewLifecycleOwner) { list ->

            val item = list.firstOrNull()   // <-- prevents crash

            val jsonString = item?.SettingsValue

            if (jsonString != null) {
                val setting = try {
                    json.decodeFromString<ContractRemindersSettings>(jsonString)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                setting?.let { it ->
                    reminders.clear()
                    //reminders.addAll(it.reminders.split(","))
                    reminders.addAll(
                        it.reminders
                            .split(",")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }
                    )

                    binding.contractSwitchInsert.isChecked = it.insert
                    binding.contractSwitchUpdate.isChecked = it.update
                    binding.contractSwitchDelete.isChecked = it.delete
                }

            } else {
                // No settings found — clear reminders
                reminders.clear()
            }

            adapter.notifyDataSetChanged()
        }

    }


}
