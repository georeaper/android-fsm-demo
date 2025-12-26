package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect

import com.gkprojects.cmmsandroidapp.R


class RvAdapterFindCustomers(private val context: Context, private var customerList:ArrayList<CustomerSelect>): RecyclerView.Adapter<RvAdapterFindCustomers.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        //val rv_id = itemView.findViewById<TextView>(R.id.list_tv_customer_id)
        val rv_customerName = itemView.findViewById<TextView>(R.id.list_tv_customer_name)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.list_of_rv_customer_find,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=customerList[position]
        //holder.rv_id.text = currentItem.CustomerID.toString()
        holder.rv_customerName.text = currentItem.CustomerName

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem )
            }
        }

    }
    fun filterList(filterlist: ArrayList<CustomerSelect>) {
        // below line is to add our filtered
        // list in our course array list.
        customerList = filterlist
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged()
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    fun setData(newList: ArrayList<CustomerSelect>) {
        this.customerList = newList
        notifyDataSetChanged()
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: CustomerSelect)
    }
}