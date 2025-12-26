package com.gkprojects.cmmsandroidapp.presentation.adapter

import android.R
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportCheckForm
import com.gkprojects.cmmsandroidapp.databinding.DialogChecklistWorkorderPerEquipmentRowBinding


class AdapterRecyclerViewChecklist(private var list: ArrayList<FieldReportCheckForm>):RecyclerView.Adapter<AdapterRecyclerViewChecklist.MyViewHolder>() {


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding= DialogChecklistWorkorderPerEquipmentRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val checkForm = list[position]
        holder.bind(checkForm, holder.itemView.context)
    }
    fun setData(newList: ArrayList<FieldReportCheckForm>) {
        list = newList
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }
    fun getData(): ArrayList<FieldReportCheckForm> {
        return list
    }
    inner class MyViewHolder(private val binding: DialogChecklistWorkorderPerEquipmentRowBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(checkForms : FieldReportCheckForm, context: Context) {
            binding.dialogChecklistWorkorderPerEquipmentRowQuestion.text = checkForms.Description
            binding.dialogChecklistWorkorderPerEquipmentRowAnswer.setText(checkForms.ValueMeasured)
            binding.dialogChecklistWorkorderPerEquipmentRowExpected.text = checkForms.ValueExpected
            val results = arrayOf("PASS", "FAIL", "N/A", "NONE")
            val adapter = ArrayAdapter(context, R.layout.simple_dropdown_item_1line, results)
            binding.dialogChecklistWorkorderPerEquipmentRowResult.setAdapter(adapter)
            binding.dialogChecklistWorkorderPerEquipmentRowResult.textSize = 12f

            binding.dialogChecklistWorkorderPerEquipmentRowResult.setOnItemClickListener { parent, view, position, id ->
                val selectedItem = parent.getItemAtPosition(position) as String
                checkForms.Result = selectedItem
            }
            binding.dialogChecklistWorkorderPerEquipmentRowAnswer.addTextChangedListener(object :
                TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    checkForms.ValueMeasured = s.toString()
                }



            })
        }
    }
}