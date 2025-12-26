package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.entity.Equipments
import com.gkprojects.cmmsandroidapp.databinding.DialogEquipmentListWorkordersRecyclerviewRowBinding

class AdapterRecyclerViewDialogEquipmentList(private var equipmentList : ArrayList<Equipments>): RecyclerView.Adapter<AdapterRecyclerViewDialogEquipmentList.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null

    inner class MyViewHolder(private val binding: DialogEquipmentListWorkordersRecyclerviewRowBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(equipment : Equipments){
            binding.dialogEquipmentListWorkOrdersRecyclerviewRowModelTV.text=equipment.Model
            binding.dialogEquipmentListWorkOrdersRecyclerviewRowSerialNumberTV.text=equipment.SerialNumber
        }
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    interface OnClickListener {
        fun onClick(position: Int, model: Equipments)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding=DialogEquipmentListWorkordersRecyclerviewRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }
    fun setData(list: ArrayList<Equipments>)
    {
        this.equipmentList=list
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val equipment = equipmentList[position]
        holder.bind(equipment)

        // Set a click listener on the item view
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(position, equipment)
        }
    }


}