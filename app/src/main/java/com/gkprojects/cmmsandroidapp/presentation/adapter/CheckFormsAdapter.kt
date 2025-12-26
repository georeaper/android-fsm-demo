package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.entity.Maintenances
import com.gkprojects.cmmsandroidapp.R

class CheckFormsAdapter(private var items: ArrayList<Maintenances>) :
    RecyclerView.Adapter<CheckFormsAdapter.ViewHolder>() {
    private var onClickListener: OnClickListener? = null


    interface OnClickListener {
        fun onClick(position: Int, model: Maintenances)
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val textView1: TextView = view.findViewById(R.id.textView1)
        val textView2: TextView = view.findViewById(R.id.textView2)

        init {
            view.setOnClickListener(this)

        }


        override fun onClick(v: View?) {
            onClickListener?.onClick(adapterPosition, items[adapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.check_forms_row, parent, false)
        return ViewHolder(view)
    }
    fun setData(checkFormlist:ArrayList<Maintenances>)
    {
        this.items=checkFormlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView1.text = item.Name
        holder.textView2.text = item.Description
    }

    override fun getItemCount() = items.size
}
