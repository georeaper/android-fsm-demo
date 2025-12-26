package com.gkprojects.cmmsandroidapp.presentation.ui.UserCreationFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels

import com.gkprojects.cmmsandroidapp.data.local.entity.Users
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedViewModel

import com.gkprojects.cmmsandroidapp.databinding.FragmentUserInformationBinding


class UserInformationFragment : Fragment() {
    private lateinit var binding: FragmentUserInformationBinding
    private lateinit var user : Users



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentUserInformationBinding.inflate(inflater,container,false)
        return binding.root
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_user_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedViewModel: SharedViewModel by activityViewModels()
        sharedViewModel.user.observe(viewLifecycleOwner) {
            user = it
            binding.emailUserInformation.setText(user.Email.toString())
            binding.fullnameUserInformation.setText(user.Name.toString())
            binding.descriptionUserInformation.setText(user.Description.toString())
            binding.phoneNumberUserInformation.setText(user.Phone.toString())

            binding.emailUserInformation.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    UsersSingletons.predefinedUser.Email = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            binding.fullnameUserInformation.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    UsersSingletons.predefinedUser.Name = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            binding.descriptionUserInformation.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    UsersSingletons.predefinedUser.Description = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            binding.phoneNumberUserInformation.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    UsersSingletons.predefinedUser.Phone = s.toString()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })


        }

    }


}