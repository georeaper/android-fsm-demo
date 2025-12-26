package com.gkprojects.cmmsandroidapp.presentation.adapter.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.R

class ReminderListAdapter (
    private val reminders: MutableList<String>,
    private val onAddClicked: () -> Unit,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_REMINDER = 1
        private const val TYPE_ADD = 2
    }

    override fun getItemCount(): Int {
        return if (reminders.size < 5) reminders.size + 1 else reminders.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == reminders.size && reminders.size < 5) {
            TYPE_ADD
        } else {
            TYPE_REMINDER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_ADD -> {
                val view = inflater.inflate(R.layout.item_contract_add_reminder, parent, false)
                AddReminderViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_contract_reminder, parent, false)
                ReminderViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is AddReminderViewHolder) {
            holder.itemView.setOnClickListener { onAddClicked() }

        } else if (holder is ReminderViewHolder) {
            val reminder = reminders[position]
            holder.bind(reminder)
            holder.itemView.findViewById<ImageView>(R.id.iconDelete).setOnClickListener {
                onDelete(reminder)
            }

        }
    }
}
class AddReminderViewHolder(view: View) : RecyclerView.ViewHolder(view)

class ReminderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textView: TextView = view.findViewById(R.id.textReminderValue)

    fun bind(text: String) {
        textView.text = text
    }
}
