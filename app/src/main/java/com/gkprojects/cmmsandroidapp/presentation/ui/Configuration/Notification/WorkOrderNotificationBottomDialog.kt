package com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.Notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkprojects.cmmsandroidapp.data.local.dto.ReminderSettingsGeneric
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.SettingsRepository
import com.gkprojects.cmmsandroidapp.databinding.DialogFragmentNotificationWorkOrderBinding

import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SettingsViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SettingsViewModelFactory
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.serialization.json.Json

class WorkOrderNotificationBottomDialog :
    BaseReminderBottomSheet<ReminderSettingsGeneric>() {

    private lateinit var binding: DialogFragmentNotificationWorkOrderBinding
    private lateinit var viewModel: SettingsViewModel

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = DialogFragmentNotificationWorkOrderBinding.inflate(inflater, container, false)
        val context = requireContext()
        val repositorySettings = SettingsRepository.getInstance(context)
        val factorySettingsVM = SettingsViewModelFactory(repositorySettings)
        viewModel= ViewModelProvider(this,factorySettingsVM)[SettingsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getRemindersOnWorkOrders()
        // 1️⃣ Set layout manager FIRST
        binding.recyclerviewNotification.layoutManager =
            LinearLayoutManager(requireContext())

        // 2️⃣ Now attach the reusable adapter
        initReminderAdapter(binding.recyclerviewNotification)


        viewModel.settingsData.observe(viewLifecycleOwner) { list ->
            val json = list.firstOrNull()?.SettingsValue
            val settings = json?.let { decodeSettings(it) } ?: return@observe
            onSettingsLoaded(settings)
        }
//        val recyclerview =binding.recyclerviewNotification
//        recyclerview.layoutManager= LinearLayoutManager(requireContext())

        // Listen for picker result
        parentFragmentManager.setFragmentResultListener("reminder_picker_result", this) { _, bundle ->
            val value = bundle.getInt("value")
            val unit = bundle.getString("unit")

            reminders.add("$value $unit before")
            adapter.notifyDataSetChanged()
        }

        binding.saveBottomSheetBtn.setOnClickListener {
            saveSettings(encodeSettings())
        }
        binding.closeBottomSheetDialog.setOnClickListener {
            dismiss()
        }
    }


    override fun decodeSettings(json: String) =
        Json.decodeFromString<ReminderSettingsGeneric>(json)

    override fun encodeSettings() =
        ReminderSettingsGeneric(
            reminders = reminders.joinToString(","),
            insert = binding.switchInsert.isChecked,
            update = binding.switchUpdate.isChecked,
            delete = binding.switchDelete.isChecked
        )



    override fun onSettingsLoaded(settings: ReminderSettingsGeneric) {
        reminders.clear()
        //reminders.addAll(settings.reminders.split(","))
        reminders.addAll(
            settings.reminders
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
        )

        binding.switchInsert.isChecked = settings.insert
        binding.switchUpdate.isChecked = settings.update
        binding.switchDelete.isChecked = settings.delete

        adapter.notifyDataSetChanged()
    }

    override fun saveSettings(settings: ReminderSettingsGeneric) {
        viewModel.saveWorkOrdersReminders(settings)
    }


}
