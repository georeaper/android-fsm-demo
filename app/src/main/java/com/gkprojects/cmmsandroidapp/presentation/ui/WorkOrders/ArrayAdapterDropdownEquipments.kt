package com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders

import android.widget.ArrayAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import com.gkprojects.cmmsandroidapp.data.local.entity.Equipments


class ArrayAdapterDropdownEquipments(
    context: Context,
    private var items: MutableList<Equipments>
) : ArrayAdapter<Equipments>(
    context,
    android.R.layout.simple_dropdown_item_1line,
    items
) {
    var selectedEquipment: Equipments? = null
        private set

    /** Optional callback when selection changes */
    var onEquipmentSelected: ((Equipments) -> Unit)? = null

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)
        val tv = v.findViewById<TextView>(android.R.id.text1)
        val eq = items[position]

        tv.text = "${eq.Model} - ${eq.SerialNumber}"
        return v
    }
    private var filteredList: MutableList<Equipments> = ArrayList(items)

    override fun getCount(): Int = filteredList.size

    override fun getItem(position: Int): Equipments = filteredList[position]

    /**
     * Updates the adapter's data and refreshes the dropdown.
     */
    fun updateData(newItems: List<Equipments>) {
        items.clear()
        items.addAll(newItems)

        filteredList.clear()
        filteredList.addAll(newItems)

        notifyDataSetChanged()
    }


    /** Call this when a selection is made in the fragment */
    fun selectItemAt(position: Int) {
        if (position in items.indices) {
            selectedEquipment = items[position]
            onEquipmentSelected?.invoke(selectedEquipment!!)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val results = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    results.values = items
                    results.count = items.size
                } else {
                    val query = constraint.toString().trim().lowercase()
                    val matches = items.filter {
                        it.Model!!.lowercase().contains(query) ||
                                it.SerialNumber!!.lowercase().contains(query)
                    }
                    results.values = matches
                    results.count = matches.size
                }
                return results
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList.clear()
                if (results?.values is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    filteredList.addAll(results.values as List<Equipments>)
                }
                notifyDataSetChanged()
            }
        }
    }
}
