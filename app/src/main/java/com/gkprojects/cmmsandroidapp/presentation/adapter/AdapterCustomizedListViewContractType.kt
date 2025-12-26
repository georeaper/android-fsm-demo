package com.gkprojects.cmmsandroidapp.presentation.adapter


import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.R

data class CustomizedField(val text: String, val color: Int)

class CustomizedFieldsAdapter(private val data: MutableList<CustomizedField>) : RecyclerView.Adapter<CustomizedFieldsAdapter.ViewHolder>() {

    private var onDelete: (CustomizedField) -> Unit = {}

    fun setOnDeleteAction(action: (CustomizedField) -> Unit) {
        onDelete = action
    }


    class ViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.row_contracttype_customizedfields, parent, false)) {
        private var mTextView: TextView = itemView.findViewById(R.id.row_contracttype_customizedfields_textview)
        private var mDeleteButton: Button = itemView.findViewById(R.id.row_contracttype_customizedfields_button)
        private var mColor : View =itemView.findViewById(R.id.row_contracttype_customizedfields_view)

        fun bind(item: CustomizedField, onDelete: (CustomizedField) -> Unit) {
            mTextView.text = item.text
            val background = mColor.background as? GradientDrawable
            background?.setColor(item.color)
            mDeleteButton.setOnClickListener { onDelete(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater, parent)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item, onDelete)
    }

    override fun getItemCount(): Int = data.size

    fun addItem(item: CustomizedField) {
        if (!data.contains(item)) {
            data.add(item)
            notifyItemInserted(data.size - 1)
        }
    }

    fun removeItem(item: CustomizedField) {
        val index = data.indexOf(item)
        if (index != -1) {
            data.removeAt(index)
            notifyItemRemoved(index)
        }
    }
    fun updateData(newData: List<CustomizedField>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}