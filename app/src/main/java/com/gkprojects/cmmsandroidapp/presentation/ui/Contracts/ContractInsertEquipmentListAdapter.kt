package com.gkprojects.cmmsandroidapp.presentation.ui.Contracts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.DetailedContract
import com.gkprojects.cmmsandroidapp.R

class ContractInsertEquipmentListAdapter(private var list : ArrayList<DetailedContract>) : RecyclerView.Adapter<ContractInsertEquipmentListAdapter.MyViewholder>(){
    private var onClickListener: OnClickListener? = null
    private var onLongClickListener : OnLongClickListener?=null

    class MyViewholder(itemView : View):RecyclerView.ViewHolder(itemView) {
        val serialNumber : TextView=itemView.findViewById(R.id.contract_insert_equipmentList_tvSerialNumber)
        val model : TextView=itemView.findViewById(R.id.contract_insert_equipmentList_tvModel)
        val visits : TextView=itemView.findViewById(R.id.contract_insert_equipmentList_tvVisits)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.contract_insert_equipment_list_row,parent,false)
        return MyViewholder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface OnLongClickListener{
        fun onLongClick(position: Int,model: DetailedContract)
    }

    interface OnClickListener {
        fun onDeleteItem(position: Int, model: DetailedContract)
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    fun setOnLongClickListener(listener: OnLongClickListener) {
        this.onLongClickListener = listener
    }


    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val item = list[position]
        holder.serialNumber.text = item.serialNumber
        holder.model.text = item.model
        holder.visits.text = item.Visits.toString()
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.onLongClick(position,item)
            true
        }


    }
    fun removeItemAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    fun setData(contractList: ArrayList<DetailedContract>)
    {
        this.list=contractList
        notifyDataSetChanged()
    }


}