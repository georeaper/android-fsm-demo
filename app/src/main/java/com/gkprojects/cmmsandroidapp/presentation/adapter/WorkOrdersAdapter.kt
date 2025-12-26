package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentSelectCustomerName
import com.gkprojects.cmmsandroidapp.data.local.dto.WorkOrdersList
import com.gkprojects.cmmsandroidapp.databinding.ListWorkordersBinding
import com.gkprojects.cmmsandroidapp.presentation.adapter.EquipmentAdapter.OnLongClickListener


class WorkOrdersAdapter(private var workOrderList : MutableList<WorkOrdersList>): RecyclerView.Adapter<WorkOrdersAdapter.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: OnLongClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    fun setOnLongClickListener(listener: OnLongClickListener) {
        this.onLongClickListener = listener
    }

    interface OnLongClickListener {
        fun onLongClick(position: Int, model: WorkOrdersList)
    }
    interface OnClickListener {
        fun onClick(position: Int, model: WorkOrdersList)
    }
    class MyViewHolder(private val binding: ListWorkordersBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(workOrder: WorkOrdersList) {

            binding.workorderCustomerName.text=workOrder.customerName.toString()
            binding.workorderDate.text=workOrder.dateOpened.toString()
            binding.workorderNumber.text=workOrder.reportNumber.toString()
            //binding.workOrderListTitle.text=workOrder.title.toString()
        }
    }
    fun setData(templist:ArrayList<WorkOrdersList>)
    {
        this.workOrderList=templist
        notifyDataSetChanged()
    }
    fun removeItemAt(position: Int) {
        if (position in workOrderList.indices) {
            workOrderList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, workOrderList.size - position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ListWorkordersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return workOrderList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(workOrderList[position])

        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, workOrderList[position])
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.onLongClick(position, workOrderList[position])
            true // return true to consume the event
        }
    }
}