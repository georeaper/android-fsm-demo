package com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.core.utils.DateUtils
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData
import com.gkprojects.cmmsandroidapp.databinding.ListWorkOrderToolsBinding
import com.gkprojects.cmmsandroidapp.databinding.WorkOrderToolsRowBinding

class AdapterFieldReportRecyclerViewTools(
    private var fieldReportToolsList : ArrayList<FieldReportToolsCustomData>):
    RecyclerView.Adapter<AdapterFieldReportRecyclerViewTools.FieldReportToolsViewHolder>() {
    inner class FieldReportToolsViewHolder(private val binding : ListWorkOrderToolsBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(customData: FieldReportToolsCustomData){
            binding.workOrderToolName.text = customData.toolsTitle
            var calDate =DateUtils.normalize(customData.toolsCalDate)
            binding.workOrderToolCalDate.text = calDate!!.substringBefore(" ")

        }
    }
    fun getData(): ArrayList<FieldReportToolsCustomData> {
        return fieldReportToolsList
    }
    fun setData(newList: ArrayList<FieldReportToolsCustomData>) {
        fieldReportToolsList = newList
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldReportToolsViewHolder {
        val binding =ListWorkOrderToolsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FieldReportToolsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return fieldReportToolsList.size
    }

    override fun onBindViewHolder(holder: FieldReportToolsViewHolder, position: Int) {
        val fieldReportTools =fieldReportToolsList[position]
        holder.bind(fieldReportTools)
    }
}