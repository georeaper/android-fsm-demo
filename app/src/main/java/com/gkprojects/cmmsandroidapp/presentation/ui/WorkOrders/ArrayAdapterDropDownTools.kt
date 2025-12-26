package com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.gkprojects.cmmsandroidapp.data.local.entity.Tools
import com.gkprojects.cmmsandroidapp.databinding.DropdownAdapterToolsWorkOrdersBinding
import java.util.Locale

class ArrayAdapterDropDownTools(
    context: Context,

    private val toolsList: List<Tools>) :
    ArrayAdapter<Tools>(
        context,
        android.R.layout.simple_dropdown_item_1line,
        toolsList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        val tv = v.findViewById<TextView>(android.R.id.text1)
        val eq = toolsList[position]

        tv.text = "${eq.Model} - ${eq.SerialNumber}"
        return v

    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrBlank()) {
                    toolsList
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    toolsList.filter { tool ->
                        tool.Title?.toLowerCase(Locale.ROOT)?.contains(filterPattern) == true ||
                                tool.Description?.toLowerCase(Locale.ROOT)?.contains(filterPattern) == true
                                //tool.AnotherField?.toLowerCase()?.contains(filterPattern) == true
                        // Add more fields as needed
                    }
                }

                return FilterResults().apply { values = filteredList }
            }
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return if (resultValue is Tools) {
                    "${resultValue.Title} - ${resultValue.CalibrationDate}"
                    // Modify this string to include the fields you want
                } else {
                    super.convertResultToString(resultValue)
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                addAll(results?.values as List<Tools>)
                notifyDataSetChanged()

                // Log the filtered values
                Log.d("Filter", "Filtered values: ${results.values}")
            }
        }
    }

}

