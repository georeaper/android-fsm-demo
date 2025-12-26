package com.gkprojects.cmmsandroidapp.presentation.ui.UserCreationFragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.UsersVM
import com.gkprojects.cmmsandroidapp.R
import com.google.android.material.button.MaterialButton

class CreateUserDialogFragment : DialogFragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var nextButton: MaterialButton
    private lateinit var finishButton: MaterialButton
    private lateinit var usersVM: UsersVM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.dialog_create_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersVM=ViewModelProvider(this)[UsersVM::class.java]

        viewPager = view.findViewById(R.id.viewPager)
        nextButton = view.findViewById(R.id.nextButton)
        finishButton = view.findViewById(R.id.finishButton)
        finishButton.visibility=View.GONE
        val adapter = CreateUserAdapter(this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateNavigationButtons(position)
            }
        })

        nextButton.setOnClickListener {
            finishButton.visibility=View.VISIBLE
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.currentItem += 1
                //updateNavigationButtons(viewPager.currentItem)
            }
        }

        finishButton.setOnClickListener {
            updateFinilizedUser()
            // Handle finalization of installation (e.g., save choices)
            dismiss()
        }

        updateNavigationButtons(0) // Update buttons for initial page
    }

    @SuppressLint("SuspiciousIndentation")
    private fun updateFinilizedUser() {
        val updatedUser= UsersSingletons.predefinedUser

            usersVM.updateUser(requireContext(),updatedUser)



    }

    private fun updateNavigationButtons(currentPage: Int) {
        // Get the item count of the adapter
        val itemCount = viewPager.adapter?.itemCount ?: 0

        // Check if we are on the last page
        if (currentPage < itemCount - 1) {
            nextButton.visibility = View.VISIBLE
            finishButton.visibility = View.GONE
        } else {
            nextButton.visibility = View.GONE
            finishButton.visibility = View.VISIBLE
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val window = (dialog).window
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
        return dialog
    }
}