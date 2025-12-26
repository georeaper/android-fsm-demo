package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentListCheckOut
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.databinding.WorkOrdersEquipmentListRowBinding

import java.util.ArrayList

class AdapterWorkOrderEquipmentsRecyclerView(private var equipmentList :List<EquipmentListCheckOut>): RecyclerView.Adapter<AdapterWorkOrderEquipmentsRecyclerView.MyViewholder>()  {
    private var onClickListener: OnClickListener? = null

    interface OnClickListener {
        fun onClick(position: Int, model: EquipmentListCheckOut)
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    class MyViewholder(private val binding: WorkOrdersEquipmentListRowBinding) :RecyclerView.ViewHolder(binding.root){
        fun bind(equipmentList : EquipmentListCheckOut){
            binding.workOrderEquipmentListRowSn.text=equipmentList.model
            binding.workOrderEquipmentListRowSn.text=equipmentList.sn
            if (equipmentList.checkOut!!) {
                // If Check is true, set the ImageView to one icon
                binding.workOrderEquipmentListRowIMG.setImageResource(R.drawable.completed_order_svgrepo_com)
            } else {
                // If Check is false, set the ImageView to another icon
                binding.workOrderEquipmentListRowIMG.setImageResource(R.drawable.attention_svgrepo_com)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val binding = WorkOrdersEquipmentListRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewholder(binding)
    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        holder.bind(equipmentList[position])
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, equipmentList[position])
        }
    }
    fun setData(workOrderEquipmentList:ArrayList<EquipmentListCheckOut>)
    {
        this.equipmentList=workOrderEquipmentList
        notifyDataSetChanged()
    }
}