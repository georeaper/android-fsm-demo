package com.gkprojects.cmmsandroidapp.presentation.common

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.core.utils.FilterResult
import com.gkprojects.cmmsandroidapp.core.utils.FilterType
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar
import androidx.core.view.isVisible

// FilterPopWindow.kt
class ModalFilterWindow(
    private val filters: List<FilterType>,
    private val onApplyFilters: (FilterResult) -> Unit,
    private val initialValues: Map<String, Any?> = emptyMap()
) : BottomSheetDialogFragment() {

    private val selectedValues = mutableMapOf<String, Any?>()
    private val clearActions = mutableListOf<() -> Unit>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.filter_modal_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val filterContainer = view.findViewById<LinearLayout>(R.id.containerFilters)
        val btnApply = view.findViewById<Button>(R.id.btnApplyFilters)
        val tvCancel = view.findViewById<TextView>(R.id.tvCancel)
        val tvClear =view.findViewById<TextView>(R.id.tvClearAll)
        tvCancel.setOnClickListener {
            dismiss()
        }
        tvClear.setOnClickListener {
            selectedValues.clear()

            // Clear all ListFilter selections
            clearActions.forEach { it.invoke() }

            // Clear StatusFilter RadioGroups and DatePickers
            filterContainer.children.forEach { child ->
                val rgStatus = child.findViewById<RadioGroup>(R.id.rgStatus)
                rgStatus?.clearCheck()

                val fromEt = child.findViewById<EditText>(R.id.etDateFrom)
                val toEt = child.findViewById<EditText>(R.id.etDateTo)
                fromEt?.setText("")
                toEt?.setText("")
            }
        }



        // Dynamically add filters based on the given list
        filters.forEach { filter ->
            when (filter) {
                is FilterType.ListFilter -> {
                    setupListFilter(requireContext(), filterContainer, filter.title, filter.options) { selected ->
                        selectedValues[filter.title] = selected
                    }
                }
                is FilterType.StatusFilter -> {
                    setupStatusFilter(requireContext(), filterContainer, filter.title) { status ->
                        selectedValues[filter.title] = status
                    }
                }
                is FilterType.DateRangeFilter -> {
                    setupDateRangeFilter(requireContext(), filterContainer, filter.title) { from, to ->
                        selectedValues["${filter.title}_from"] = from
                        selectedValues["${filter.title}_to"] = to
                    }
                }
            }
        }

        // Apply button listener

        btnApply.setOnClickListener {
            val result = if (selectedValues.isEmpty()) {
                // User cleared everything — return empty filters
                FilterResult(emptyMap())
            } else {
                FilterResult(selectedValues.toMap())
            }

            onApplyFilters(result)
            dismiss()
        }

    }
    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let { sheet ->
            val behavior = com.google.android.material.bottomsheet.BottomSheetBehavior.from(sheet)
            behavior.state = com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = (resources.displayMetrics.heightPixels * 0.95).toInt() // 95% of screen
            sheet.layoutParams.height = (resources.displayMetrics.heightPixels * 0.95).toInt()
        }
    }


    // 🔹 List filter (multi-select)
    private fun setupListFilter(
        context: Context,
        parent: LinearLayout,
        title: String,
        options: List<String>,
        onSelectionChanged: (Set<String>) -> Unit
    ) {
        val view :View= LayoutInflater.from(context).inflate(R.layout.item_filter_list,parent,false)

        val titleTv=view.findViewById<TextView>(R.id.tvTitle)
        titleTv.text=title
        val btnVisibilityToggle: ImageButton=view.findViewById(R.id.btnToggleRecycler)
        val selectedCountTv=view.findViewById<TextView>(R.id.tvSelectedCount)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerFilterList)
        recyclerView.layoutManager = LinearLayoutManager(context)



        //val selectedItems = mutableSetOf<String>()
        val selectedItems = (initialValues[title] as? Set<String>)?.toMutableSet() ?: mutableSetOf()


        fun updateSelectedCount(count :Int){
            if (count>0){
                btnVisibilityToggle.visibility=View.GONE
                selectedCountTv.visibility=View.VISIBLE
                selectedCountTv.text=count.toString()
            }else{
                selectedCountTv.visibility=View.GONE
                btnVisibilityToggle.visibility=View.VISIBLE
            }
        }
        // Add a helper to clear selections

        val adapter = object : RecyclerView.Adapter<ListFilterViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFilterViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)
                return ListFilterViewHolder(view as CheckedTextView)
            }

            override fun getItemCount(): Int = options.size

            override fun onBindViewHolder(holder: ListFilterViewHolder, position: Int) {
                val item = options[position]
                holder.textView.text = item
                //holder.textView.isChecked = item in selectedItems
                holder.textView.isChecked = item in (initialValues[title] as? Set<String> ?: emptySet())
                holder.textView.setOnClickListener {
                    if (selectedItems.contains(item)) selectedItems.remove(item)
                    else selectedItems.add(item)
                    holder.textView.isChecked = !holder.textView.isChecked
                    onSelectionChanged(selectedItems)
                    updateSelectedCount(selectedItems.size)
                }
            }

        }

        recyclerView.adapter = adapter
        btnVisibilityToggle.setOnClickListener {
            if (recyclerView.isVisible) {
                recyclerView.visibility = View.GONE
                btnVisibilityToggle.setImageResource(R.drawable.right_filter_arrow) // collapsed
            } else {
                recyclerView.visibility = View.VISIBLE
                btnVisibilityToggle.setImageResource(R.drawable.down_filter_arrow) // expanded
            }
        }


        updateSelectedCount(0)



        parent.addView(view)
        fun clearListSelection() {
            selectedItems.clear()
            adapter.notifyDataSetChanged()
            updateSelectedCount(0)
        }

// Badge click
        selectedCountTv.setOnClickListener { clearListSelection() }

// Add to global clear actions
        clearActions.add { clearListSelection() }
    }


    // 🔹 Status filter (true / false / null)
    private fun setupStatusFilter(
        context: Context,
        parent: LinearLayout,
        title: String,
        onStatusChanged: (Boolean?) -> Unit
    ) {
        // Inflate your custom layout
        val view = LayoutInflater.from(context).inflate(R.layout.item_filter_status, parent, false)

        // Find views inside it
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val rgStatus = view.findViewById<RadioGroup>(R.id.rgStatus)
        val rbAll = view.findViewById<RadioButton>(R.id.rbAll)
        val rbActive = view.findViewById<RadioButton>(R.id.rbActive)
        val rbInactive = view.findViewById<RadioButton>(R.id.rbInactive)

        // Apply dynamic title (you can reuse the layout for multiple filter types)
        tvTitle.text = title
        val current = initialValues[title] as? Boolean?

// Set initial selection based on current
        when (current) {
            true -> rbActive.isChecked = true
            false -> rbInactive.isChecked = true
            null -> rbAll.isChecked = true
        }

// Handle selection changes
        rgStatus.setOnCheckedChangeListener { _, checkedId ->
            val status: Boolean? = when (checkedId) {
                rbActive.id -> true
                rbInactive.id -> false
                else -> null
            }
            onStatusChanged(status)
        }

        // Add the inflated layout to the parent container
        parent.addView(view)
    }


    // 🔹 Date range filter
    private fun setupDateRangeFilter(
        context: Context,
        parent: LinearLayout,
        title: String,
        onDateChanged: (String?, String?) -> Unit
    ) {
        val view=LayoutInflater.from(context).inflate(R.layout.item_filter_date_range,parent,false)
        val tvTitle=view.findViewById<TextView>(R.id.tvTitle)
        val fromDateEt=view.findViewById<EditText>(R.id.etDateFrom)
        val toDateEt=view.findViewById<EditText>(R.id.etDateTo)
        tvTitle.text=title
        val fromKey = "${title}_from"
        val toKey = "${title}_to"

        var fromDate = initialValues[fromKey] as? String
        var toDate = initialValues[toKey] as? String

        fromDateEt.setText(fromDate?.take(10) ?: "")
        toDateEt.setText(toDate?.take(10) ?: "")



//        var fromDate: String? = null
//        var toDate: String? = null
        fromDateEt.setOnClickListener {
            showDatePicker(context) { date ->
                fromDateEt.setText(date.substring(0,10))
                fromDate = date
            onDateChanged(fromDate, toDate)
            }
        }
        toDateEt.setOnClickListener {
            showDatePicker(context) { date ->
                toDateEt.setText(date.substring(0,10))
                toDate = date
            onDateChanged(fromDate, toDate)
             }
        }

        parent.addView(view)
    }

    private fun showDatePicker(context: Context, onDatePicked: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, day ->
                // Format as dd/MM/yyyy first
                val pickedDate = "%02d/%02d/%04d".format(day, month + 1, year)

                // Normalize to standard format using DateUtils
                val normalizedDate = DateUtils.normalize(pickedDate)

                // Pass normalized date to callback
                normalizedDate?.let { onDatePicked(it) }
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


    private class ListFilterViewHolder(val textView: CheckedTextView) :
        RecyclerView.ViewHolder(textView)
}

