package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.gkprojects.cmmsandroidapp.data.local.entity.Tickets
import com.gkprojects.cmmsandroidapp.R

class CasesAdapter(private var casesList : ArrayList<Tickets>): RecyclerView.Adapter<CasesAdapter.MyViewholder>() {
    private var onClickListener: OnClickListener? = null
    private var onLongClickListener : OnLongClickListener?= null

    class MyViewholder(itemView : View):RecyclerView.ViewHolder(itemView) {
        val title: TextView=itemView.findViewById(R.id.titleCasesList)
        val startDate :TextView=itemView.findViewById(R.id.dateStartedCases)
        val priority :TextView=itemView.findViewById(R.id.priorityCases)
        val caseNumber :TextView=itemView.findViewById(R.id.caseNumberCases)


    }
    private var settingsMap: Map<String, String> = emptyMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.list_technical_cases,parent,false)
        return MyViewholder(itemView)
    }

    override fun getItemCount(): Int {
        return casesList.size
    }
    fun setData(casesList:ArrayList<Tickets>)
    {
        this.casesList=casesList
        notifyDataSetChanged()
    }
    fun removeItemAt(position: Int){
        casesList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentitem = casesList[position]
        holder.caseNumber.text =  currentitem.TicketNumber.toString()
        holder.priority.text = currentitem.Urgency.toString()
        holder.startDate.text =  currentitem.DateStart.toString()
        holder.title.text=currentitem.Title.toString()

        Log.d("urgencyAdapter",currentitem.Urgency.toString())

        // Apply style based on Urgency matching SettingsValue
        val colorHex = settingsMap[currentitem.Urgency] ?: "#FFFFFF"

        try {

            holder.priority.setTextColor(Color.parseColor(colorHex))
        } catch (e: Exception) {
            Log.w("CasesAdapter", "Invalid color format: $colorHex")
        }

        holder.itemView.setOnLongClickListener {
            onLongClickListener?.onLongClick(position,currentitem)
            true
        }

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentitem )
            }
        }
    }


    fun setSettingsMap(map: Map<String, String>) {
        this.settingsMap = map
        Log.d("urgencyAdapterColor",settingsMap.toString())
        notifyDataSetChanged()
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    fun setOnLongClickListener(listener: OnLongClickListener){
        this.onLongClickListener=listener
    }
    interface  OnLongClickListener{
        fun onLongClick(position: Int, model: Tickets)
    }


    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: Tickets)
    }

}