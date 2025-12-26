package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.entity.Tickets
import com.gkprojects.cmmsandroidapp.R
import com.google.android.material.textview.MaterialTextView

class CasesListEquipmentInsertAdapter(private var list : ArrayList<Tickets>): RecyclerView.Adapter<CasesListEquipmentInsertAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null

    interface OnClickListener {
        fun onClick(position: Int, model: Tickets)
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.equipment_insert_cases_list,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem =list[position]
        holder.id.text=currentItem.TicketID.toString()
        holder.urgency.text=currentItem.Urgency.toString()
        holder.title.text=currentItem.Title.toString()
        //holder.view.text=currentItem.TicketID.toString()
    }
    fun setData(casesList:ArrayList<Tickets>)
    {
        this.list=casesList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val id=itemView.findViewById<MaterialTextView>(R.id.equipmentInsert_casesList_MaterialTextView_ID)
        val urgency=itemView.findViewById<MaterialTextView>(R.id.equipmentInsert_casesList_MaterialTextView_Urgency)
        val title=itemView.findViewById<MaterialTextView>(R.id.equipmentInsert_casesList_MaterialTextView_Title)
        val view=itemView.findViewById<View>(R.id.equipmentInsert_casesList_View)

    }

}


