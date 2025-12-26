package com.gkprojects.cmmsandroidapp.presentation.ui.dashboardCustomer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerContractsDataClass
import com.gkprojects.cmmsandroidapp.R

class CustomerDashboardAdapterContracts(private var contractList :ArrayList<DashboardCustomerContractsDataClass>): RecyclerView.Adapter<CustomerDashboardAdapterContracts.DashboardContractsVH>() {
    class DashboardContractsVH(itemView :View ):RecyclerView.ViewHolder(itemView) {
        var title : TextView =itemView.findViewById(R.id.recyclerviewCustomerDashContractsTitle)
        var type : TextView =itemView.findViewById(R.id.recyclerviewCustomerDashContractsType)
        var dateEnd : TextView =itemView.findViewById(R.id.recyclerviewCustomerDashContractsDateEnd)
        var contractIndicator : View =itemView.findViewById(R.id.recyclerviewCustomerDashIndicatorContracts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardContractsVH {
        val itemView= LayoutInflater.from(parent.context).inflate(R.layout.dashboard_customer_list_contracts,parent,false)
        return DashboardContractsVH(itemView)
    }

    override fun getItemCount(): Int {
        return contractList.size
    }

    override fun onBindViewHolder(holder: DashboardContractsVH, position: Int) {
        var currentItem = contractList[position]
        holder.title.text=currentItem.Title
        holder.type.text="Type: "+ currentItem.ContractType
        holder.dateEnd.text="Date End: " + currentItem.DateEnd
    }
    fun setData(contractList : ArrayList<DashboardCustomerContractsDataClass>){
        this.contractList=contractList
        notifyDataSetChanged()
    }
}