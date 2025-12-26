package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentSelectCustomerName

import com.gkprojects.cmmsandroidapp.R

class EquipmentAdapter(private val context: Context, private var equipmentList:List<EquipmentSelectCustomerName>):RecyclerView.Adapter<EquipmentAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: OnLongClickListener? = null




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.list_equipment,parent,false)
        return MyViewHolder(itemView)

    }

    override fun getItemCount(): Int {
        return equipmentList.size
    }
    fun setData(equipmentList:ArrayList<EquipmentSelectCustomerName>)
    {
        this.equipmentList=equipmentList
        notifyDataSetChanged()
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    fun setOnLongClickListener(listener: OnLongClickListener) {
        this.onLongClickListener = listener
    }






    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: EquipmentSelectCustomerName)
    }
    interface OnLongClickListener {
        fun onLongClick(position: Int, model: EquipmentSelectCustomerName)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val currentItem = equipmentList[position]

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem )
            }
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.onLongClick(position, currentItem)
            true // return true to consume the event
        }
            holder.sn.text = currentItem.SerialNumber.toString()
            holder.model.text=currentItem.Model.toString()
            holder.installationDate.text=currentItem.InstallationDate.toString()
            //holder.customerName.text=currentItem.CustomerName.toString()


    }
    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val sn: TextView = itemView.findViewById(R.id.serialNumberEquipmentList)
        val model :TextView =itemView.findViewById(R.id.modelEquipmentList)
        val installationDate :TextView=itemView.findViewById(R.id.installationDateEquipmentList)
        //val customerName :TextView =itemView.findViewById(R.id.customerNameEquipmentList)


    }
}