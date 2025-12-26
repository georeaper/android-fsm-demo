package com.gkprojects.cmmsandroidapp.presentation.ui.Inventory

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.entity.Inventory
import com.gkprojects.cmmsandroidapp.databinding.ListInventoryBinding


class AdapterInventoryRecyclerView(private var inventoryList : ArrayList<Inventory> ) :RecyclerView.Adapter<AdapterInventoryRecyclerView.InventoryViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var onLongClickListener : OnLongClickListener ?=null

    inner class InventoryViewHolder(private var binding: ListInventoryBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(inventory : Inventory){
            binding.partnumberInventorylist.text= inventory.Title.toString()
            binding.descriptionInventoryList.text= inventory.Description.toString()
            binding.quantityInventoryList.text= inventory.Quantity.toString()


        }
    }
    fun getData(): ArrayList<Inventory> {
        return inventoryList
    }
    fun setData(newList: ArrayList<Inventory>) {
        inventoryList = newList
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        val binding=ListInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InventoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return inventoryList.size
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val inventory = inventoryList[position]
        holder.bind(inventory)
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, inventory)
            }
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.onLongClick(position,inventory)
            true
        }
    }
    fun removeItemAt(position: Int){
        this.inventoryList.removeAt(position)
        notifyItemRemoved(position)

    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    fun setOnLongClickListener(listener : OnLongClickListener){
        this.onLongClickListener =listener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: Inventory)
    }
    interface OnLongClickListener{
        fun onLongClick(position: Int, model: Inventory)
    }
}