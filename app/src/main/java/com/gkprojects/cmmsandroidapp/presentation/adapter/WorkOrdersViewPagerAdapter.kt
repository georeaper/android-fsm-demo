package com.gkprojects.cmmsandroidapp.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.CustomerInfoFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.EquipmentListFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.SparePartListFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.ToolsListFragment


class WorkOrdersViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val reportId: String?
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return if (reportId.isNullOrEmpty()) {
            1 // Only show "General Info"
        } else {
            4 // Show all tabs
        }
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position) {
            0 -> CustomerInfoFragment()
            1 -> EquipmentListFragment()
            2 -> ToolsListFragment()
            3 -> SparePartListFragment()
            else -> Fragment()
        }

        return fragment
    }
}

