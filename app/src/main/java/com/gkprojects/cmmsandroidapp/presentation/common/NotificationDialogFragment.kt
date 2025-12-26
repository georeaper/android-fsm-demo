package com.gkprojects.cmmsandroidapp.presentation.common

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.data.local.dto.Notification
import com.gkprojects.cmmsandroidapp.domain.usecase.NotificationService
import com.gkprojects.cmmsandroidapp.presentation.adapter.NotificationAdapter
import com.google.android.material.textview.MaterialTextView

class NotificationDialogFragment(
    private val notifications: MutableList<Notification>
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use Material3 theme without overlapping status bar
        val dialog = Dialog(requireContext(), R.style.Theme_CMMSandroidApp)
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_notification_dialog, null)
        dialog.setContentView(view)

        dialog.window?.apply {
            setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            // Set background so edges are not transparent
            //setBackgroundDrawable(ColorDrawable(Color.WHITE))

            // Let system handle status bar
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        val closeBtn: ImageButton = view.findViewById(R.id.notification_close_btn)
        closeBtn.setOnClickListener { dismiss() }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = NotificationAdapter(notifications)

        val markAllAsSeenBtn = view.findViewById<MaterialTextView>(R.id.deleteAll)
        markAllAsSeenBtn.setOnClickListener {
            //NotificationService.getInstance(requireContext()).markAllAsSeen()
            NotificationService.getInstance(requireContext()).deleteAllNotifications()
            recyclerView.adapter?.notifyDataSetChanged()
        }

        return dialog
    }
}
