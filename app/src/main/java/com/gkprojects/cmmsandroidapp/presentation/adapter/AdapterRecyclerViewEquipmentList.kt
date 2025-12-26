package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomDisplayDatFieldReportEquipments
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.databinding.ListWorkOrderEquipmentsBinding
import com.gkprojects.cmmsandroidapp.databinding.WorkOrdersEquipmentListRowBinding

class AdapterRecyclerViewEquipmentList(private var equipmentList : ArrayList<CustomDisplayDatFieldReportEquipments>):RecyclerView.Adapter<AdapterRecyclerViewEquipmentList.MyViewHolder>() {

    var listener: OnItemClickListener? = null

    inner class MyViewHolder(private val binding :ListWorkOrderEquipmentsBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(equipmentsCheckOut : CustomDisplayDatFieldReportEquipments){
            binding.workOrderEquipmentName.text=equipmentsCheckOut.Model + " " + equipmentsCheckOut.SerialNumber
            //binding.workOrderEquipmentListRowModel.text=equipmentsCheckOut.Model

            if (equipmentsCheckOut.CompletedStatus!!) {
                binding.workOrderEquipmentStatus.setTextColor(
                    ResourcesCompat.getColor(binding.root.resources, R.color.completed, null)
                )
                binding.workOrderEquipmentStatus.text="Completed"
            } else {
                binding.workOrderEquipmentStatus.setTextColor(
                    ResourcesCompat.getColor(binding.root.resources, R.color.pending, null)
                )
                binding.workOrderEquipmentStatus.text="Pending"

            }
            binding.root.setOnClickListener {
                listener?.onItemClick(equipmentsCheckOut)
            }

        }
    }
    interface OnItemClickListener {
        fun onItemClick(equipment: CustomDisplayDatFieldReportEquipments)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setData(equipmentCheckOut: ArrayList<CustomDisplayDatFieldReportEquipments>)
    {
        this.equipmentList=equipmentCheckOut
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding= ListWorkOrderEquipmentsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val equipment = equipmentList[position]
        holder.bind(equipment)

    }
}