package com.gkprojects.cmmsandroidapp.presentation.ui.SpecialTools


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.entity.Tools
import com.gkprojects.cmmsandroidapp.databinding.ListSpecialToolsBinding


class AdapterSpecialToolsRecyclerView(private var toolList :ArrayList<Tools>):RecyclerView.Adapter<AdapterSpecialToolsRecyclerView.ToolsViewHolder>() {
    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: OnLongClickListener? = null


    inner class ToolsViewHolder(private val binding: ListSpecialToolsBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(tools : Tools) {
            binding.modelSpecialToolsList.text = tools.Model.toString()
            binding.serialNumberSpecialToolsList.text = tools.SerialNumber.toString()
            binding.calDueDateSpecialToolsList.text = tools.CalibrationDate.toString()
            binding.manufacturerSpecialToolsList.text = tools.Manufacturer.toString()


        }
    }
    fun getData(): ArrayList<Tools> {
        return toolList
    }
    fun setData(newList: ArrayList<Tools>) {
        toolList = newList
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
    fun filterList(text: String) {
        val filteredList = toolList.filter { it.Title!!.contains(text, ignoreCase = true) || it.SerialNumber!!.contains(text, ignoreCase = true) }
        setData(ArrayList(filteredList))
    }
    fun removeItemAt(position: Int){
        toolList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToolsViewHolder {
        val binding= ListSpecialToolsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToolsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return toolList.size
    }

    override fun onBindViewHolder(holder: ToolsViewHolder, position: Int) {
        val tools = toolList[position]
        holder.bind(tools)

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, tools)
            }
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.onLongClick(position, tools)
            true
        }

    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, model: Tools)
    }
    fun setOnLongClickListener(listener:OnLongClickListener){
        this.onLongClickListener=listener
    }
    interface OnLongClickListener{
        fun onLongClick(position: Int, model: Tools)

    }
}