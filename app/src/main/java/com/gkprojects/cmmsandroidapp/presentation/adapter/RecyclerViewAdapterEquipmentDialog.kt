package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentListInCases
import com.gkprojects.cmmsandroidapp.R

class RecyclerViewAdapterEquipmentDialog(private var list :ArrayList<EquipmentListInCases>):RecyclerView.Adapter<RecyclerViewAdapterEquipmentDialog.ViewHolderEquipmentDialog>() {
    private var onClickListener: OnClickListener? = null

    class ViewHolderEquipmentDialog(itemView : View):RecyclerView.ViewHolder(itemView) {
        val sn : TextView =itemView.findViewById(R.id.dialog_equipment_searchView_row_tv_sn)
        val model : TextView=itemView.findViewById(R.id.dialog_equipment_searchView_row_tv_model)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEquipmentDialog {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.dialog_equipment_searchview_row,parent,false)
        return ViewHolderEquipmentDialog(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolderEquipmentDialog, position: Int) {
        val currentItem = list[position]
        holder.sn.text=currentItem.SerialNumber
        holder.model.text=currentItem.Model
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem )
            }
        }
    }
    fun filterList(filterList: ArrayList<EquipmentListInCases>) {

        // below line is to add our filtered
        // list in our course array list.
        list = filterList
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: EquipmentListInCases)
    }
}
