package com.gkprojects.cmmsandroidapp.presentation.adapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.gkprojects.cmmsandroidapp.data.local.entity.Maintenances
import com.gkprojects.cmmsandroidapp.databinding.DropdownAdapterSparePartsWorkOrdersBinding
import java.util.Locale

class DropDownAdapter(context: Context, resource: Int, private val maintenanceList: List<Maintenances>) :
    ArrayAdapter<Maintenances>(context, resource, maintenanceList) {
    private lateinit var binding: DropdownAdapterSparePartsWorkOrdersBinding
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        binding = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            DropdownAdapterSparePartsWorkOrdersBinding.inflate(inflater, parent, false)
        } else {
            DropdownAdapterSparePartsWorkOrdersBinding.bind(convertView)
        }

        val maintenance = maintenanceList[position]

        binding.dropdownAdapterSparePartsTitle .text = maintenance.Name
        binding.dropdownAdapterSparePartsWorkOrdersDescription.text = maintenance.Description
        // ... set other fields as needed

        return binding.root
    }
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrBlank()) {
                    maintenanceList
                } else {
                    val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim()
                    maintenanceList.filter { maintenance ->
                        maintenance.Name?.toLowerCase(Locale.ROOT)?.contains(filterPattern) == true ||
                                maintenance.Description?.toLowerCase(Locale.ROOT)?.contains(filterPattern) == true
                        //tool.AnotherField?.toLowerCase()?.contains(filterPattern) == true
                        // Add more fields as needed
                    }
                }

                return FilterResults().apply { values = filteredList }
            }
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return if (resultValue is Maintenances) {
                    "${resultValue.Name} - ${resultValue.Description}"
                    // Modify this string to include the fields you want
                } else {
                    super.convertResultToString(resultValue)
                }
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                clear()
                addAll(results?.values as List<Maintenances>)
                notifyDataSetChanged()

                // Log the filtered values
                Log.d("Filter", "Filtered values: ${results.values}")
            }
        }
    }
}