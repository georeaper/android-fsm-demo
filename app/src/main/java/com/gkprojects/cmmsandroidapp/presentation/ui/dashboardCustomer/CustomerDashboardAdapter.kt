package com.gkprojects.cmmsandroidapp.presentation.ui.dashboardCustomer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerEquipmentDataClass
import com.gkprojects.cmmsandroidapp.R

class CustomerDashboardAdapter(private var dashboardList :ArrayList<DashboardCustomerEquipmentDataClass>):RecyclerView.Adapter<CustomerDashboardAdapter.CustomViewholder>() {
    class CustomViewholder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var sn : TextView=itemView.findViewById(R.id.recyclerviewCustomerDashEquipmentSN)
        var model : TextView=itemView.findViewById(R.id.recyclerviewCustomerDashModel)
        var installationDate : TextView=itemView.findViewById(R.id.recyclerviewCustomerDashInstalation)
        var customIndicator : View =itemView.findViewById(R.id.recyclerviewCustomerDashIndicator)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewholder {
        var itemView= LayoutInflater.from(parent.context).inflate(R.layout.dashboard_customer_list_equipment,parent,false)
        return CustomViewholder(itemView)
    }

    override fun getItemCount(): Int {
        return dashboardList.size
    }

    override fun onBindViewHolder(holder: CustomViewholder, position: Int) {
        var currentItem = dashboardList[position]
        holder.installationDate.text="InstallationDate: " + currentItem.InstallationDate
        holder.model.text="Model: "+ currentItem.Model
        holder.sn.text=currentItem.SerialNumber

    }
    fun setData(dashboardList : ArrayList<DashboardCustomerEquipmentDataClass>){
        this.dashboardList=dashboardList
        notifyDataSetChanged()
    }
}