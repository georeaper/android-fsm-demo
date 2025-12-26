package com.gkprojects.cmmsandroidapp.presentation.ui.Settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.MasterData.CustomizedFieldFragment
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.databinding.FragmentSettingsBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView


class SettingsFragment : Fragment() {

    private lateinit var binding : FragmentSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentSettingsBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        var activity =requireActivity()

        var drawerLayout = activity.findViewById<DrawerLayout>(R.id.DrawLayout)
        val navView: NavigationView = activity.findViewById(R.id.navView)
        val toolbar: MaterialToolbar = activity.findViewById(R.id.main_toolbar)

        //toolbar.title="Settings"
        val drawable = ContextCompat.getDrawable(activity, R.drawable.menu_svgrepo_com)
        toolbar.navigationIcon = drawable


        var toggle = ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val customizedFields = binding.settingsTextviewCustomizedFields
        customizedFields.setOnClickListener {
            val fragmentManager =parentFragmentManager
            val fragmentTransaction=fragmentManager.beginTransaction()
            val fragment = CustomizedFieldFragment()

            fragmentTransaction.replace(R.id.frameLayout1,fragment)
            fragmentTransaction.addToBackStack("settingsNavigation")
            fragmentTransaction.commit()
        }

        binding.settingsNotifications.setOnClickListener{
            val fragmentManager =parentFragmentManager
            val fragmentTransaction=fragmentManager.beginTransaction()
            val fragment = NotificationSettings()

            fragmentTransaction.replace(R.id.frameLayout1,fragment)
            fragmentTransaction.addToBackStack("settingsNavigation")
            fragmentTransaction.commit()

        }
        binding.settingsProfile.setOnClickListener {
            val fragmentManager =parentFragmentManager
            val fragmentTransaction=fragmentManager.beginTransaction()
            val fragment = ProfileSettings()

            fragmentTransaction.replace(R.id.frameLayout1,fragment)
            fragmentTransaction.addToBackStack("settingsNavigation")
            fragmentTransaction.commit()
        }
        binding.settingsSyncing.setOnClickListener {
            val fragmentManager =parentFragmentManager
            val fragmentTransaction=fragmentManager.beginTransaction()
            val fragment = SyncingSettings()

            fragmentTransaction.replace(R.id.frameLayout1,fragment)
            fragmentTransaction.addToBackStack("settingsNavigation")
            fragmentTransaction.commit()
        }
        binding.settingsPreferences.setOnClickListener {
            val fragmentManager =parentFragmentManager
            val fragmentTransaction=fragmentManager.beginTransaction()
            val fragment = PreferencesSettings()

            fragmentTransaction.replace(R.id.frameLayout1,fragment)
            fragmentTransaction.addToBackStack("settingsNavigation")
            fragmentTransaction.commit()
        }
    }


}