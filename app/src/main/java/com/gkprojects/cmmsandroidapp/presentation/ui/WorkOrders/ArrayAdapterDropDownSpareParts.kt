package com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.gkprojects.cmmsandroidapp.data.local.entity.Inventory

import java.util.Locale


class ArrayAdapterDropDownSpareParts(
    context: Context,

    private val inventoryList: List<Inventory>) :
    ArrayAdapter<Inventory>(
        context,
        android.R.layout.simple_dropdown_item_1line,
        inventoryList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        val tv = v.findViewById<TextView>(android.R.id.text1)
        val eq = inventoryList[position]

        tv.text = "${eq.Title} - ${eq.Description}"
        return v
//        binding = if (convertView == null) {
//            val inflater = LayoutInflater.from(context)
//            DropdownAdapterSparePartsWorkOrdersBinding.inflate(inflater, parent, false)
//        } else {
//            DropdownAdapterSparePartsWorkOrdersBinding.bind(convertView)
//        }
//
//        val inventory = inventoryList[position]
//
//        binding.dropdownAdapterSparePartsTitle .text = inventory.Title
//        binding.dropdownAdapterSparePartsWorkOrdersDescription.text = inventory.Description
//        // ... set other fields as needed
//
//        return binding.root
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrBlank()) {
                    inventoryList
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    inventoryList.filter { inventory ->
                        inventory.Title?.toLowerCase(Locale.ROOT)?.contains(filterPattern) == true ||
                                inventory.Description?.toLowerCase(Locale.ROOT)?.contains(filterPattern) == true
                        //tool.AnotherField?.toLowerCase()?.contains(filterPattern) == true
                        // Add more fields as needed
                    }
                }

                return FilterResults().apply { values = filteredList }
            }
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return if (resultValue is Inventory) {
                    "${resultValue.Title} - ${resultValue.Description}"
                    // Modify this string to include the fields you want
                } else {
                    super.convertResultToString(resultValue)
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                addAll(results?.values as List<Inventory>)
                notifyDataSetChanged()

                // Log the filtered values
                Log.d("Filter", "Filtered values: ${results.values}")
            }
        }
    }
}