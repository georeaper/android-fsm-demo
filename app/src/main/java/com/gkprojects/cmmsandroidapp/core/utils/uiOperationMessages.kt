package com.gkprojects.cmmsandroidapp.core.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object UiOperationMessages {

    fun <T> handleResult(
        context: Context,
        result: OperationResult<T>,
        rootView: View,
        successMessage: String = "Operation completed successfully"
    ) {
        when (result) {
            is OperationResult.Success -> {
                Toast.makeText(context, successMessage, Toast.LENGTH_SHORT).show()
            }
            is OperationResult.Error -> {
                Snackbar.make(rootView, "Error: ${result.message}", Snackbar.LENGTH_LONG)
                    .setBackgroundTint(0xFFD32F2F.toInt()) // deep red color
                    .setTextColor(0xFFFFFFFF.toInt())      // white text
                    .show()
            }
        }
    }
}