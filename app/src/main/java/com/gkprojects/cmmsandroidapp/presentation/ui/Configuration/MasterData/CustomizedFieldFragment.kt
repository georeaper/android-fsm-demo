package com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.MasterData

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.gkprojects.cmmsandroidapp.presentation.ui.GenericSettingsFragment
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.databinding.FragmentCustomizedFieldBinding


class CustomizedFieldFragment : Fragment() {


    private lateinit var binding: FragmentCustomizedFieldBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?
    ): View {
        binding = FragmentCustomizedFieldBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.masterDataModelBtn.setOnClickListener {
            val fragment = ModelListFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout1, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.masterDataCategoryBtn.setOnClickListener {
            val fragment = CategoryAssetListFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout1, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.masterDataManufacturerBtn.setOnClickListener {
            val fragment = ManufacturerListFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout1, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.masterDataWorkorderTypeBtn.setOnClickListener {
        //binding.settingsWorkOrdersTypes.setOnClickListener {
            val fragment = GenericSettingsFragment().apply {
                arguments = bundleOf(
                    "settingKey" to "WorkOrderType",
                    "settingTitle" to "WorkOrderType",
                    "settingDescription" to "Available departments for work orders"
                )
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout1, fragment)
                .addToBackStack(null)
                .commit()
        }
        binding.masterDataWorkorderDepartmentBtn.setOnClickListener {

            val fragment = GenericSettingsFragment().apply {
                arguments = bundleOf(
                    "settingKey" to "Departments",
                    "settingTitle" to "Departments",
                    "settingDescription" to "Available departments for work orders"
                )
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout1, fragment)
                .addToBackStack(null)
                .commit()
        }

//        binding.masterDataTechincalcasePriorityBtn.setOnClickListener {
//            val fragment = GenericSettingsFragment().apply {
//                arguments = bundleOf(
//                    "settingKey" to "TechnicalCasePriority",
//                    "settingTitle" to "TechnicalCasePriority",
//                    "settingDescription" to "Available departments for work orders"
//                )
//            }
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.frameLayout1, fragment)
//                .addToBackStack(null)
//                .commit()
//        }
//
//        binding.masterDataContractTypesBtn.setOnClickListener {
//            val fragment = GenericSettingsFragment().apply {
//                arguments = bundleOf(
//                    "settingKey" to "ContractType",
//                    "settingTitle" to "ContractType",
//                    "settingDescription" to "Available departments for work orders"
//                )
//            }
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.frameLayout1, fragment)
//                .addToBackStack(null)
//                .commit()
//        }


    }

    private  fun showFragment(fragment : Fragment){
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.frameLayout1, fragment)
        transaction?.addToBackStack(null)
        transaction?.commit()
    }


}
