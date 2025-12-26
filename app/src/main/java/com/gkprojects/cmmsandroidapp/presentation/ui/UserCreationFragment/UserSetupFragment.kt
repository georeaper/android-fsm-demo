package com.gkprojects.cmmsandroidapp.presentation.ui.UserCreationFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.gkprojects.cmmsandroidapp.data.local.entity.Users
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedViewModel
import com.gkprojects.cmmsandroidapp.databinding.FragmentUserSetupBinding


class UserSetupFragment : Fragment() {
    private lateinit var binding : FragmentUserSetupBinding
    private lateinit var user : Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserSetupBinding.inflate(inflater, container, false)
        return binding.root
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_user_setup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedViewModel: SharedViewModel by activityViewModels()
        sharedViewModel.user.observe(viewLifecycleOwner, Observer {
            user=it
            binding.prefixTCUserInformation.setText(it.TechnicalCasePrefix.toString())
            binding.prefixWOUserInformation.setText(it.ReportPrefix.toString())
            binding.WorkOrderNumberUserInformation.setText(it.LastReportNumber.toString())
            binding.TechnicalCaseNumberUserInformation.setText(it.LastTCNumber.toString())
        })

        binding.prefixTCUserInformation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                UsersSingletons.predefinedUser.TechnicalCasePrefix = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.prefixWOUserInformation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                UsersSingletons.predefinedUser.ReportPrefix = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.WorkOrderNumberUserInformation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                UsersSingletons.predefinedUser.LastReportNumber = s.toString().toIntOrNull()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.TechnicalCaseNumberUserInformation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                UsersSingletons.predefinedUser.LastTCNumber = s.toString().toIntOrNull()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


    }


}