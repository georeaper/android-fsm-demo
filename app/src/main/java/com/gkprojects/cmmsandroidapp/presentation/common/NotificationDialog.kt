package com.gkprojects.cmmsandroidapp.presentation.common

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.presentation.adapter.NotificationAdapter
import com.gkprojects.cmmsandroidapp.domain.usecase.NotificationService
import com.gkprojects.cmmsandroidapp.data.local.dto.Notification
import com.gkprojects.cmmsandroidapp.R
import com.google.android.material.textview.MaterialTextView

class CustomNotificationDialog(
    context: Context,
    private val button: View,
    private val notifications: MutableList<Notification> // Pass notifications as a parameter
) : Dialog(context) {
    private lateinit var notificationService: NotificationService

    init {
        // Inflate the custom dialog layout
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_notification_dialog, null)
        setContentView(view)
        notificationService = NotificationService.getInstance(context)

        // Set the dialog size (optional)
//        val displayMetrics = context.resources.displayMetrics
//        val width = (displayMetrics.widthPixels * 0.8).toInt()  // 80% of screen width
//        val height = (displayMetrics.heightPixels * 0.6).toInt() // 60% of screen height
//        window?.setLayout(width, height)

        window?.setLayout(
            android.view.WindowManager.LayoutParams.MATCH_PARENT,
            android.view.WindowManager.LayoutParams.MATCH_PARENT
        )
        val closeBtn :ImageButton=view.findViewById(R.id.notification_close_btn)
        closeBtn.setOnClickListener {
            dismiss()
        }


        // Set up the RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Use the passed notifications
        val adapter = NotificationAdapter(notifications)
        recyclerView.adapter = adapter

        // Mark all as seen button
        val markAllAsSeenBtn = view.findViewById<MaterialTextView>(R.id.deleteAll)
        markAllAsSeenBtn.setOnClickListener {
            notificationService.markAllAsSeen()
            adapter.notifyDataSetChanged() // Update the list after marking as seen
        }
    }
}


