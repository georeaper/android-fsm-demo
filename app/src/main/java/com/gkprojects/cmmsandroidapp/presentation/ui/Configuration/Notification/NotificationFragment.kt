package com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.Notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gkprojects.cmmsandroidapp.databinding.FragmentNotificationBinding

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.notificationBlockContract.setOnClickListener {
            ContractNotificationBottomDialog()
                .show(childFragmentManager, "ContractNotification")
        }
        binding.notificationBlockEquipment.setOnClickListener {
            EquipmentNotificationBottomDialog()
                .show(childFragmentManager,"EquipmentNotification")
        }
        binding.notificationBlockCustomer.setOnClickListener {
            CustomerNotificationBottomDialog()
                .show(childFragmentManager,"CustomerNotification")
        }
        binding.notificationBlockTechnicalCase.setOnClickListener {
            TechnicalCaseNotificationBottomDialog()
                .show(childFragmentManager,"TechnicalCaseNotification")
        }
        binding.notificationBlockWorkOrder.setOnClickListener {
            WorkOrderNotificationBottomDialog()
                .show(childFragmentManager,"WorkOrderNotification")
        }
        binding.notificationBlockTasks.setOnClickListener {
            TasksNotificationBottomDialog()
                .show(childFragmentManager,"TasksNotification")
        }
        binding.notificationBlockSpecialTools.setOnClickListener {
            SpecialToolsNotificationBottomDialog()
                .show(childFragmentManager,"SpecialToolsNotification")
        }

    }

}