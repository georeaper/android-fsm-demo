package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.gkprojects.cmmsandroidapp.R

class FilterCustomerAdapter(
    context: Context,
    private val items: List<FilterCustomer>
) : ArrayAdapter<FilterCustomer>(context, 0, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val item = getItem(position)!!
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.dropdown_item_with_checkbox, parent, false
        )

        val checkbox = view.findViewById<CheckBox>(R.id.checkbox)
        val title = view.findViewById<TextView>(R.id.title)
        val subtitle = view.findViewById<TextView>(R.id.subtitle)

        title.text = item.name
        subtitle.text = item.address
        checkbox.isChecked = item.isSelected

        view.setOnClickListener {
            item.isSelected = !item.isSelected
            checkbox.isChecked = item.isSelected
        }

        return view
    }
}

data class FilterCustomer(
    val id: String,
    val name: String,
    val address: String,
    var isSelected: Boolean = false
)