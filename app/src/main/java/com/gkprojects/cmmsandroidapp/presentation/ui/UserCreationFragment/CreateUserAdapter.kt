package com.gkprojects.cmmsandroidapp.presentation.ui.UserCreationFragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CreateUserAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UserInformationFragment()
            1 -> UserSetupFragment()
            2 -> UserFinalizeFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}