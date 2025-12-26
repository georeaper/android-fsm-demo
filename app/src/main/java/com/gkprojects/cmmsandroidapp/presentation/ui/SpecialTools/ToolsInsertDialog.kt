package com.gkprojects.cmmsandroidapp.presentation.ui.SpecialTools

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.entity.Tools
import com.gkprojects.cmmsandroidapp.databinding.DialogToolsInsertBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Calendar
import java.util.UUID

class ToolsInsertDialog(
    private val onConfirm: (Tools) -> Unit
):DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogToolsInsertBinding.inflate(layoutInflater)

        val picker = MaterialDatePicker.Builder.datePicker().build()

        binding.calibrationDateEditText.setOnClickListener {
            parentFragmentManager.let { fm -> picker.show(fm, picker.toString()) }
        }

        picker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance().apply { timeInMillis = it }
//            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//            val selectedDate = format.format(calendar.time)
            val format = DateUtils.format(calendar.time)
            val selectedDate = DateUtils.normalize(format)
            binding.calibrationDateEditText.setText(selectedDate)
        }
        // --- Full screen dialog ---
        val dialog = Dialog(requireContext(), R.style.FullScreenDialog)
        dialog.setContentView(binding.root)

        // OK Button handling
        dialog.setOnShowListener {
            dialog.findViewById<View>(android.R.id.button1)
        }

        // Handle OK + Cancel using custom buttons (since it's not AlertDialog anymore)
        binding.dialogToolSaveButton.setOnClickListener {
            val tools = Tools(
                UUID.randomUUID().toString(),
                null, null, null, null, null,
                null, null, null, null, null
            )

            tools.Title = binding.dialogToolsInsertEditTextTitle.text.toString()
            tools.SerialNumber = binding.dialogToolsInsertEditTextSerialNumber.text.toString()
            tools.Model = binding.dialogToolsInsertEditTextModel.text.toString()
            tools.Manufacturer = binding.dialogToolsInsertEditTextManufacturer.text.toString()
            tools.Description = binding.dialogToolsInsertEditTextDescription.text.toString()
            tools.CalibrationDate = binding.calibrationDateEditText.text.toString()

            onConfirm(tools)
            dismiss()
        }

        binding.closeToolDialog.setOnClickListener {
            dismiss()
        }

        return dialog
    }
}