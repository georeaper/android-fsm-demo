package com.gkprojects.cmmsandroidapp.presentation.ui.Inventory

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.data.local.entity.Inventory
import com.gkprojects.cmmsandroidapp.databinding.DialogInventoryInsertBinding
import java.util.UUID

class InventoryInsertDialog(
    private val onConfirm: (Inventory) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val binding = DialogInventoryInsertBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext(), R.style.FullScreenDialog)
        dialog.setContentView(binding.root)

        // SAVE button
        binding.dialogInventorySaveButton.setOnClickListener {

            // Read values only WHEN user clicks save
            val title = binding.dialogInventoryInsertEditTextPartNumber.text.toString()
            val description = binding.dialogInventoryInsertEditTextDescription.text.toString()
            val valueStr = binding.dialogInventoryInsertEditTextValue.text.toString()
            val qtyStr = binding.dialogInventoryInsertEditTextQuantity.text.toString()

            val value = valueStr.toDoubleOrNull()
            val qty = qtyStr.toLongOrNull()

            var hasError = false

            if (value == null) {
                binding.dialogInventoryInsertEditTextValue.error = "Enter a valid number"
                hasError = true
            }
            if (qty == null) {
                binding.dialogInventoryInsertEditTextQuantity.error = "Enter a valid quantity"
                hasError = true
            }

            if (hasError) return@setOnClickListener

            // Create Inventory ONLY if valid
            val inventory = Inventory(
                UUID.randomUUID().toString(),
                null, null, null, null,
                null, null, null, null, null
            ).apply {
                Title = title
                Description = description
                Value = value
                Quantity = qty
            }

            onConfirm(inventory)
            dismiss()
        }

        // CLOSE button
        binding.closeInventoryDialog.setOnClickListener {
            dismiss()
        }

        return dialog
    }
}
