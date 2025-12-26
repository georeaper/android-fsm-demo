package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.graphics.Color

import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.Notification
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.domain.usecase.NotificationService

class NotificationAdapter(private val notifications: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.notification_item, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val notificationService = NotificationService.getInstance(context)

            // ✅ Only mark as seen if it's not already
            if (!notification.seen) {
                notification.seen = true
                notificationService.markAsSeen(notification.id)
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int = notifications.size

    class NotificationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val badgeView = itemView.findViewById<View>(R.id.badgeView)
        private val title = itemView.findViewById<TextView>(R.id.titleTextView)
        private val description = itemView.findViewById<TextView>(R.id.descriptionTextView)
        private val time = itemView.findViewById<TextView>(R.id.timeTextView)

        fun bind(notification: Notification) {
            title.text = notification.title
            description.text = notification.description
            time.text = notification.timeStamp

            // Change color based on seen/unseen
            val bg = badgeView.background.mutate() as GradientDrawable

            if (notification.seen) {
                bg.setColor(Color.GRAY) // seen color
                bg.setStroke(1, Color.LTGRAY)
            } else {
                bg.setColor(Color.RED)  // unseen color
                bg.setStroke(2, Color.WHITE)
            }
        }
    }
}