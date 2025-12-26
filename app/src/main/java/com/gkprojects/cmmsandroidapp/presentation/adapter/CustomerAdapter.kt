package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.entity.Customer
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentSelectCustomerName
import com.gkprojects.cmmsandroidapp.presentation.adapter.EquipmentAdapter.OnLongClickListener

class CustomerAdapter(private val context: Context, private var customerList:ArrayList<Customer>): RecyclerView.Adapter<CustomerAdapter.MyViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: OnLongClickListener? = null





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.list_customers,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    fun setData(customerList:ArrayList<Customer>)
    {
        this.customerList=customerList
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
        fun onClick(position: Int, model: Customer)
    }
    interface OnLongClickListener {
        fun onLongClick(position: Int, model: Customer)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = customerList[position]
        holder.customerName.text = currentItem.Name
        holder.customerPhone.text = currentItem.Phone
        holder.customerAddress.text = currentItem.Address
        holder.customerEmail.text = currentItem.Email
        holder.itemView.setOnClickListener {

            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem )
            }
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.onLongClick(position, currentItem)
            true // return true to consume the event
        }

    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val customerName = itemView.findViewById<TextView>(R.id.tv_customer)
        val customerPhone = itemView.findViewById<TextView>(R.id.phoneNumber_customer)
        val customerAddress = itemView.findViewById<TextView>(R.id.address_customer)
        val customerEmail = itemView.findViewById<TextView>(R.id.email_customer)

    }
}