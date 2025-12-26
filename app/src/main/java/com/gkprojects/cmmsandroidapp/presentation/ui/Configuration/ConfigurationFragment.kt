package com.gkprojects.cmmsandroidapp.presentation.ui.Configuration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.databinding.FragmentConfigurationBinding
import com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.MasterData.CustomizedFieldFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.Notification.NotificationFragment


class ConfigurationFragment : Fragment() {
    private lateinit var binding : FragmentConfigurationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentConfigurationBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


            binding.configurationCheckFormsBtn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout1, CheckFormsFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        binding.configurationTemplatePDFreportBtn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout1, TemplatesFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
        binding.configurationMasterDataBtn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout1, CustomizedFieldFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        binding.notificationBlock.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.frameLayout1, NotificationFragment())
            transaction?.addToBackStack(null)
            transaction?.commit()
        }
    }


}