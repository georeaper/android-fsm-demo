package com.gkprojects.cmmsandroidapp.presentation.ui.Contracts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.ContractsCustomerName
import com.gkprojects.cmmsandroidapp.R

class ContractAdapter(private var contractList :ArrayList<ContractsCustomerName> ) :RecyclerView.Adapter<ContractAdapter.MyViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var onLongClickListener : OnLongClickListener?=null

    class MyViewHolder(itemView :View) :RecyclerView.ViewHolder( itemView) {
        val title = itemView.findViewById<TextView>(R.id.titleContractList)
        val customerName = itemView.findViewById<TextView>(R.id.customerNameContractList)
        val startDate = itemView.findViewById<TextView>(R.id.dateStartContractList)
        val contractType = itemView.findViewById<TextView>(R.id.contractType)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.list_contract,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return contractList.size
    }
    fun setData(contractList: ArrayList<ContractsCustomerName>)
    {
        this.contractList=contractList
        notifyDataSetChanged()
    }
    fun removeItemAt(position: Int) {
        if (position >= 0 && position < contractList.size) {
            contractList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    fun setOnLongClickListener(listener: OnLongClickListener) {
        this.onLongClickListener = listener
    }
    interface OnClickListener {
        fun onClick(position: Int, model: ContractsCustomerName)
    }
    interface OnLongClickListener{
        fun onLongClick(position: Int,model: ContractsCustomerName)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = contractList[position]

        holder.customerName.text=currentItem.CustomerName
        holder.title.text=currentItem.Title.toString()
        holder.contractType.text=currentItem.ContractType
        holder.startDate.text=currentItem.DateStart

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, currentItem )
            }
        }
        holder.itemView.setOnLongClickListener{
                onLongClickListener!!.onLongClick(position,currentItem)
                true
            }

    }
}