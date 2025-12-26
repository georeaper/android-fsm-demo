package com.gkprojects.cmmsandroidapp.presentation.ui.UserCreationFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gkprojects.cmmsandroidapp.databinding.FragmentUserFinalizeBinding


class UserFinalizeFragment : Fragment() {
    private lateinit var binding: FragmentUserFinalizeBinding

    private val userObserver = {
        updateUserInformation()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserFinalizeBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        UsersSingletons.addObserver(userObserver)
        updateUserInformation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        UsersSingletons.removeObserver(userObserver)
    }

    private fun updateUserInformation() {
        val user = UsersSingletons.predefinedUser
        binding.descriptionUserInformation.setText(user.Description)
        binding.nameUserInformation.setText(user.Name)
        binding.emailUserInformation.setText(user.Email)
        binding.phoneUserInformation.setText(user.Phone)
        binding.technicalNumberUserInformation.setText(user.LastTCNumber.toString())
        binding.technicalPrefixUserInformation.setText(user.TechnicalCasePrefix)
        binding.reportNumberUserInformation.setText(user.LastReportNumber.toString())
        binding.reportPrefixUserInformation.setText(user.ReportPrefix)
    }


}