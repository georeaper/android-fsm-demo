package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.databinding.CustomizedFieldRowUniversalBinding

//class CustomizedFieldUniversalRVA <T>(
//    private var items: ArrayList<T>,
//    private val layoutId: Int,
//    private val bind: (item: T, view: View) -> Unit
//) : RecyclerView.Adapter<CustomizedFieldUniversalRVA<T>.ViewHolder>() {
//
//    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        fun bindItem(item: T) {
//            bind(item, itemView)
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bindItem(items[position])
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    /**
//     * Updates the adapter's data set and refreshes the RecyclerView.
//     */
//    fun setData(newItems: ArrayList<T>) {
//        // Replace the existing items with new data
//        items.clear()
//        items.addAll(newItems)
//        // Notify the adapter that the data set has changed
//        notifyDataSetChanged()
//    }
//
//    /**
//     * Adds a single item to the list and notifies the adapter.
//     */
//    fun addItem(item: T) {
//        items.add(item)
//        notifyItemInserted(items.size - 1)
//    }
//
//    /**
//     * Removes a single item from the list and notifies the adapter.
//     */
//    fun removeItem(position: Int) {
//        if (position >= 0 && position < items.size) {
//            items.removeAt(position)
//            notifyItemRemoved(position)
//            // Optionally notify the item range that has shifted
//            notifyItemRangeChanged(position, items.size)
//        }
//    }
//    fun getItemAtPosition(position: Int): T? {
//        return if (position in 0 until items.size) {
//            items[position]
//        } else {
//            null // Return null if the position is out of bounds
//        }
//    }
//}
class CustomizedFieldUniversalRVA<T>(
    private val items: ArrayList<T>,
    private val bind: (item: T, binding: CustomizedFieldRowUniversalBinding, position: Int) -> Unit
) : RecyclerView.Adapter<CustomizedFieldUniversalRVA<T>.ViewHolder>() {

    inner class ViewHolder(val binding: CustomizedFieldRowUniversalBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CustomizedFieldRowUniversalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        bind(items[position], holder.binding, position)
    }

    override fun getItemCount(): Int = items.size

    fun setData(newItems: ArrayList<T>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addItem(item: T) {
        items.add(item)
        notifyItemInserted(items.size - 1)
    }

    fun getItemAtPosition(position: Int): T? {
        return if (position in items.indices) items[position] else null
    }
}