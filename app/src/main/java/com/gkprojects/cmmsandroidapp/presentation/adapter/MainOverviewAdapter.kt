package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.gkprojects.cmmsandroidapp.data.local.dto.OverviewMainData
import com.gkprojects.cmmsandroidapp.R
import com.google.android.material.textview.MaterialTextView

class MainOverviewAdapter(context: Context, private var list:ArrayList<OverviewMainData>) : RecyclerView.Adapter<MainOverviewAdapter.MyViewHolder>(){
    class MyViewHolder(itemView :View): RecyclerView.ViewHolder(itemView) {
        val title : MaterialTextView=itemView.findViewById(R.id.tv_title_home)
        val dateStart : MaterialTextView=itemView.findViewById(R.id.tv_date_start)
        val customerName : MaterialTextView=itemView.findViewById(R.id.tv_customerName)
        val priority : MaterialTextView=itemView.findViewById(R.id.tv_priority)
        val ticketId : MaterialTextView=itemView.findViewById(R.id.tv_ticketId)



    }
    fun setData(tempList: ArrayList<OverviewMainData>) {
        this.list=tempList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_home_row,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = list[position]
        holder.title.text= buildString {
            append("Title: ")
            append(currentItem.Title)
        }
        holder.customerName.text= buildString {
            append("Customer Name: ")
            append(currentItem.CustomerName)
        }
        holder.dateStart.text=buildString {
            append("Reported: ")
            append(currentItem.DateStart)
        }
        holder.priority.text=currentItem.Urgency

        holder.ticketId.text=buildString {
            append("CaseID: ")
            append(currentItem.TicketNumber)
        }
    }
}