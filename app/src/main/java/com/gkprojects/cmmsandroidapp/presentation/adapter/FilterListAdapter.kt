package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.R

class FilterListAdapter(
    private val items: List<String>,
    private val selectedItems: MutableSet<String>,
    private val onSelectionChanged: (Set<String>) -> Unit
) : RecyclerView.Adapter<FilterListAdapter.ViewHolder>() {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.tvName)
        val checkbox = view.findViewById<CheckBox>(R.id.cbSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_filter_list_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item
        holder.checkbox.isChecked = selectedItems.contains(item)

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedItems.add(item)
            else selectedItems.remove(item)
            onSelectionChanged(selectedItems)
        }
    }

    override fun getItemCount() = items.size
}
