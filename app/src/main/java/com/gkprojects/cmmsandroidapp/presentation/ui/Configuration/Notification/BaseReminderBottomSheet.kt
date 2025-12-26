package com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.Notification

import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.serialization.BaseReminderSettings
import com.gkprojects.cmmsandroidapp.presentation.adapter.notification.ReminderListAdapter
import com.gkprojects.cmmsandroidapp.presentation.common.ReminderPickerDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseReminderBottomSheet<T : BaseReminderSettings> :
    BottomSheetDialogFragment() {

    protected val reminders = mutableListOf<String>()
    protected lateinit var adapter: ReminderListAdapter

    abstract fun decodeSettings(json: String): T?
    abstract fun encodeSettings(): T
    abstract fun saveSettings(settings: T)
    abstract fun onSettingsLoaded(settings: T)

    protected fun initReminderAdapter(recycler: RecyclerView) {
        adapter = ReminderListAdapter(
            reminders,
            onDelete = { selected ->
                reminders.remove(selected)
                //saveSettings(encodeSettings())
                adapter.notifyDataSetChanged()
            },
            onAddClicked = { openPicker() }
        )
        recycler.adapter = adapter
    }

    private fun openPicker() {
        ReminderPickerDialogFragment().show(parentFragmentManager, "ReminderPicker")
    }
}
