package com.gkprojects.cmmsandroidapp.presentation.common


import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.gkprojects.cmmsandroidapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class ReminderPickerDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = requireActivity().layoutInflater.inflate(
            R.layout.dialog_reminder_picker, null
        )

        val numberInput = view.findViewById<EditText>(R.id.reminder_value_input)
        val unitGroup = view.findViewById<RadioGroup>(R.id.reminder_unit_group)

        return MaterialAlertDialogBuilder(requireContext())
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val value = numberInput.text.toString().toIntOrNull()
                val selectedId = unitGroup.checkedRadioButtonId

                if (value != null && selectedId != -1) {
                    val unitText = view.findViewById<RadioButton>(selectedId).text.toString()

                    parentFragmentManager.setFragmentResult(
                        "reminder_picker_result",
                        Bundle().apply {
                            putInt("value", value)
                            putString("unit", unitText)
                        }
                    )
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }
}
