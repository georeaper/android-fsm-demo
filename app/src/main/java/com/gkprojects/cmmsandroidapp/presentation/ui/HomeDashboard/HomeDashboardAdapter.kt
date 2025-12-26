package com.gkprojects.cmmsandroidapp.presentation.ui.HomeDashboard

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.presentation.adapter.CustomerAdapter
import com.gkprojects.cmmsandroidapp.data.local.dto.HomeRecyclerViewData
import com.gkprojects.cmmsandroidapp.R

class HomeDashboardAdapter(private val context: Context, private var list:ArrayList<HomeRecyclerViewData>): RecyclerView.Adapter<HomeDashboardAdapter.MyViewholder>()
{
    private var onClickListener: CustomerAdapter.OnClickListener? = null


    class MyViewholder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var workorderID :TextView =itemView.findViewById(R.id.tv_homeLeft_recyclerview)
        var customerName : TextView =itemView.findViewById(R.id.rvChildLeftMain)
        var startDate :TextView = itemView.findViewById(R.id.tv_homeRight_recyclerview)
        var indicator :View =itemView.findViewById(R.id.viewHomeIndicator)
        var userID :TextView=itemView.findViewById(R.id.rvChildRightMain)
        var titleRight :TextView=itemView.findViewById(R.id.titleHomeRvRight)
        var titleLeft :TextView=itemView.findViewById(R.id.titleHomeRvLeft)


    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int ): MyViewholder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(R.layout.child_recyclerview_overview_home,parent,false)
        return MyViewholder(itemView)
    }
    fun setData(workOrderlist:ArrayList<HomeRecyclerViewData>)
    {
        this.list=workOrderlist
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val currentItem = list[position]
        holder.customerName.text=currentItem.MainLeft.toString()
        holder.workorderID.text=currentItem.topLeft.toString()
        holder.startDate.text=currentItem.topRight.toString()
        holder.userID.text=currentItem.MaintRight.toString()
        holder.titleRight.text= "User "
        holder.titleLeft.text="Customer Name"

        if(currentItem.MaintRight==null){
            holder.indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.AppColor))
        }else{
            holder.indicator.setBackgroundColor(ContextCompat.getColor(context,R.color.AppColorText))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}