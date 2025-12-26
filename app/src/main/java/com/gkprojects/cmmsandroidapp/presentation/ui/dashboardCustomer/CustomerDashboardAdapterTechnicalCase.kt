package com.gkprojects.cmmsandroidapp.presentation.ui.dashboardCustomer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerTechnicalCasesDataClass
import com.gkprojects.cmmsandroidapp.R

class CustomerDashboardAdapterTechnicalCase(private var technicalCaseList : ArrayList<DashboardCustomerTechnicalCasesDataClass>): RecyclerView.Adapter<CustomerDashboardAdapterTechnicalCase.DashboardTechnicalCaseVH>() {
    class DashboardTechnicalCaseVH(itemView : View):RecyclerView.ViewHolder(itemView) {
        var title : TextView =itemView.findViewById(R.id.recyclerviewCustomerDashEquipmentSN)
        var dateStart : TextView =itemView.findViewById(R.id.recyclerviewCustomerDashModel)
        var dateEnd : TextView =itemView.findViewById(R.id.recyclerviewCustomerDashInstalation)
        var urgency : View =itemView.findViewById(R.id.recyclerviewCustomerDashIndicator)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardTechnicalCaseVH {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.dashboard_customer_list_equipment,parent,false)
        return DashboardTechnicalCaseVH(itemView)
    }

    override fun getItemCount(): Int {
        return technicalCaseList.size
    }

    override fun onBindViewHolder(holder: DashboardTechnicalCaseVH, position: Int) {
        var currentItem = technicalCaseList[position]
        holder.title.text=currentItem.Title
        holder.dateStart.text=currentItem.DateStart
        holder.dateEnd.text=currentItem.DateEnd
    }
    fun setData(technicalCaseList : ArrayList<DashboardCustomerTechnicalCasesDataClass>){
        this.technicalCaseList=technicalCaseList
        notifyDataSetChanged()
    }
}