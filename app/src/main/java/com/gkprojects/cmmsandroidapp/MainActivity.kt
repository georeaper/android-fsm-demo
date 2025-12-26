package com.gkprojects.cmmsandroidapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.compose.runtime.mutableStateOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.gkprojects.cmmsandroidapp.domain.usecase.NotificationService
import com.gkprojects.cmmsandroidapp.data.local.entity.Users
import com.gkprojects.cmmsandroidapp.data.local.dto.Notification
import com.gkprojects.cmmsandroidapp.presentation.ui.Configuration.ConfigurationFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.Customers.CustomerFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.Customers.EditCustomerFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.Equipments.EquipmentFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.Equipments.EquipmentInsertFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.Inventory.InventoryFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.SpecialTools.SpecialToolsFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.UserCreationFragment.CreateUserDialogFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.UserCreationFragment.UsersSingletons
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.CustomerInfoFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.EquipmentListFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.SparePartListFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.ToolsListFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.WorkOrdersFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.WorkOrders.WorkOrdersInsertFragment
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.CustomerVM
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.SharedViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.UsersVM
import com.gkprojects.cmmsandroidapp.data.remote.client.ApiClient
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.syncApi.GenericSyncRepository
import com.gkprojects.cmmsandroidapp.data.remote.api.SyncApi
import com.gkprojects.cmmsandroidapp.data.remote.api.SyncData
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.syncApi.SyncDataViewModel
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.syncApi.SyncDataViewModelFactory
import com.gkprojects.cmmsandroidapp.data.remote.datasource.SyncManager
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.syncApi.SyncVMFactory
import com.gkprojects.cmmsandroidapp.presentation.viewmodel.syncApi.SyncViewModel

import com.gkprojects.cmmsandroidapp.databinding.ActivityMainBinding
import com.gkprojects.cmmsandroidapp.presentation.common.CustomNotificationDialog
import com.gkprojects.cmmsandroidapp.presentation.common.NotificationDialogFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.HomeFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.Settings.SettingsFragment
import com.gkprojects.cmmsandroidapp.presentation.ui.StatisticsFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.badge.ExperimentalBadgeUtils
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker

import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG_HOME = "Home"
        const val TAG_CUSTOMER = "Customer"
        const val TAG_EQUIPMENTS = "Equipments"
        const val TAG_CASES = "Technical Cases"
        const val TAG_CONTRACTS = "Contracts"
        const val TAG_SETTINGS = "Settings"
        const val TAG_WORK_ORDERS = "Work Orders"
        const val TAG_CONTRACT_INSERT = "Edit Contract"
        const val TAG_EQUIPMENT_INSERT = "Edit Equipment"
        const val TAG_CUSTOMER_INSERT = "Edit Customer"
        const val TAG_CUSTOMER_DASHBOARD = "Dashboard Customer"
        const val TAG_CONFIGURATION = "Configuration"
        const val TAG_STATISTICS = "Statistics"
        const val TAG_TOOLS = "Tools"
        const val TAG_INVENTORY = "Inventory"


    }
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var settingsPreferences: SharedPreferences
    private lateinit var binding : ActivityMainBinding

    private var useRemoteDBState = mutableStateOf(false)
    private lateinit var customerVM: CustomerVM
    private lateinit var usersVM: UsersVM
    private lateinit var syncViewModel: SyncViewModel
    private lateinit var syncDataViewModel: SyncDataViewModel
    //private lateinit var synchronizationViewModel: SynchronizationViewmodel

    private lateinit var notificationService: NotificationService

    private lateinit var sharedViewModel : SharedViewModel

    lateinit var toggle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: MaterialToolbar
    lateinit var navigationView: NavigationView

    private var currentFragmentTag = "Home"


    override fun onResume() {
        super.onResume()
        sharedPreferences = getSharedPreferences("SettingsApp", Context.MODE_PRIVATE)
        useRemoteDBState.value = useRemoteDB()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        // Find the notification menu item
        val notificationItem = menu?.findItem(R.id.main_menu_notification)
        val syncItem = menu?.findItem(R.id.main_menu_Sync)

        // Inflate custom layout for the notification item
        val customView = LayoutInflater.from(this).inflate(R.layout.notification_button_layout, null)

        // Set the custom view to the menu item
        notificationItem?.actionView = customView

        // Find the ImageView and TextView in the custom layout
        val notificationIcon = customView.findViewById<ImageView>(R.id.notification_icon)
        val badgeTextView = customView.findViewById<TextView>(R.id.badge_text_view)

        notificationService.unseenCount.observe(this) {
                unseenCount ->
            updateNotificationBadge(unseenCount,badgeTextView)
        }
        // Update badge count dynamically
        syncItem?.setOnMenuItemClickListener {
            showSynchronizationDialog()
            true
        }

        // Handle click on the notification icon
        customView.setOnClickListener {
            // Your action for opening the notification screen
            Toast.makeText(this, "Notification clicked", Toast.LENGTH_SHORT).show()
              // Reference to the button that triggered the dialog

            val notifications = (notificationService.getNotifications() ?: emptyList()).toMutableList()


            NotificationDialogFragment(notifications).show(supportFragmentManager, "notifications")
            //CustomNotificationDialog(this, customView, notifications).show()
        }

        return true
    }


    @OptIn(ExperimentalBadgeUtils::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val username = intent.getStringExtra("username") ?: "Unknown"
        Log.d("MainActivity", "Logged in user: $username")

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = getSharedPreferences("SettingsApp", Context.MODE_PRIVATE)
        settingsPreferences=getSharedPreferences("databaseSettings",Context.MODE_PRIVATE)
        //sharedPreferences.getBoolean("useRemoteDB", false)
        customerVM=ViewModelProvider(this)[CustomerVM::class.java]
        usersVM=ViewModelProvider(this)[UsersVM::class.java]

        notificationService = NotificationService.getInstance(this)

        val apiClient = ApiClient()
        val apiService = apiClient.createService(SyncApi::class.java)
        val apiData = apiClient.createService(SyncData::class.java)

// 2. Create the Repository
        val repository = GenericSyncRepository(apiService)

// 3. Create the factory using the repository
        val factory = SyncVMFactory(repository)

// 4. Instantiate the ViewModel using the factory
        //val viewModel = ViewModelProvider(this, factory)[SyncViewModel::class.java]
        syncViewModel=ViewModelProvider(this, factory)[SyncViewModel::class.java]
        //val syncDataApi = apiClient.createService(SyncApi::class.java) // Assuming this is how you instantiate the API
        val factorySyncData = SyncDataViewModelFactory(apiData)
        syncDataViewModel = ViewModelProvider(this, factorySyncData).get(SyncDataViewModel::class.java)


        //synchronizationViewModel=ViewModelProvider(this)[SynchronizationViewmodel::class.java]

        sharedViewModel=ViewModelProvider(this)[SharedViewModel::class.java]
        drawerLayout = findViewById<DrawerLayout>(R.id.DrawLayout)

        toolbar= binding.mainToolbar
        navigationView=findViewById(R.id.navView)
        val headerView = navigationView.getHeaderView(0) // gets the first header
        Log.d("setup2","HERE HeaderView")

        val userNameTextView = headerView.findViewById<TextView>(R.id.userNameTextView)
        userNameTextView.text = username


        //val navView: NavigationView = findViewById(R.id.navView)
       // navView.setupWithNavController(navController)
//
        toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar ,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        setSupportActionBar(toolbar)
        val logoutButton = findViewById<MaterialButton>(R.id.drawer_logout_button)
        logoutButton.setOnClickListener {
            // Example: clear user session, navigate to login screen
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()

            // Example navigation:
            // startActivity(Intent(this, LoginActivity::class.java))
            // finish()
        }
        val settingsButton = findViewById<ImageButton>(R.id.generalSettings)
        settingsButton.setOnClickListener {
            Toast.makeText(this, "Opening settings...", Toast.LENGTH_SHORT).show()
            replaceFragment(SettingsFragment(), TAG_SETTINGS)
            // Example: open your settings fragment or activity
            // startActivity(Intent(this, SettingsActivity::class.java))
        }
        toolbar.setNavigationOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks(){
                override fun onFragmentResumed(fm: FragmentManager, fragment: Fragment) {
                    if (shouldHideToolbar(fragment)) {
                        toolbar.visibility = View.GONE
                        supportActionBar?.hide()
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    } else {
                        toolbar.visibility = View.VISIBLE
                        supportActionBar?.show()
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    }
                }

            },true
        )


        //Syncrhonization Dialog
        if(useRemoteDB()){
            checkSettings()

            showSynchronizationDialog()
        }
        else{
            //initializeSettingsFromDatabase()
            usersVM.getAllUsers(this).observe(this, Observer {
                Log.d("setup","$it")
                if(it.isEmpty()){

                    val newUser = UsersSingletons.predefinedUser
                    //If this is empty it create a user and showing for addition configuration
                    //the App to run need a user active
                    Log.d("setup1","$it")
                    usersVM.insertUser(this,newUser)

                    sharedViewModel.user.value=newUser

                    showLocalUserDialog(newUser)
                }
                else{
//                    val navigationView = findViewById<NavigationView>(R.id.navView)
                    val headerView = navigationView.getHeaderView(0) // gets the first header
                    Log.d("setup2","HERE HeaderView")

                    val userNameTextView = headerView.findViewById<TextView>(R.id.userNameTextView)
                    userNameTextView.text = it[0].Name

                    sharedViewModel.user.value=it[0]

                    // if it isn't empty it check if the user in the
                    Log.d("setup2","${it[0].Name}")

                }
            })

        }
        //showSynchronizationDialog()


        val startFragment = supportFragmentManager
        val firstTransactionFrag =startFragment.beginTransaction()
        firstTransactionFrag.replace(R.id.frameLayout1, HomeFragment())
        firstTransactionFrag.commit()
        drawerLayout.closeDrawers()




//        testApi()

        navigationView.setNavigationItemSelectedListener {
            it.isChecked=true
            when(it.itemId){
                R.id.home_item -> replaceFragment(HomeFragment(), TAG_HOME)
                R.id.customer_item -> replaceFragment(CustomerFragment(), TAG_CUSTOMER)
                R.id.equipment_item -> replaceFragment(EquipmentFragment(), TAG_EQUIPMENTS)
                R.id.workOrder_item ->replaceFragment(WorkOrdersFragment(), TAG_WORK_ORDERS)
//                R.id.cases_item -> replaceFragment(CasesFragment(), TAG_CASES)
//                R.id.contract_item -> replaceFragment(ContractFragment(), TAG_CONTRACTS)
//                R.id.settings_item -> replaceFragment(SettingsFragment(), TAG_SETTINGS)
                R.id.tools_item ->replaceFragment(SpecialToolsFragment(), TAG_TOOLS)
                R.id.inventory_item ->replaceFragment(InventoryFragment(), TAG_INVENTORY)
                R.id.configuration_item -> replaceFragment(ConfigurationFragment(), TAG_CONFIGURATION)
//                R.id.statistics_item -> replaceFragment(StatisticsFragment(), TAG_STATISTICS)

 }
            true
        }





    }
    private val fragmentsWithoutToolbar = setOf(

        EditCustomerFragment::class,
        EquipmentInsertFragment::class,
        WorkOrdersInsertFragment::class,

        EquipmentListFragment::class,
        SparePartListFragment::class,
        CustomerInfoFragment::class,
        ToolsListFragment::class
    )

    private fun shouldHideToolbar(fragment: Fragment): Boolean {
        return fragment::class in fragmentsWithoutToolbar||
                fragment is MaterialDatePicker<*> // or just DialogFragment if you want broader coverage

    }


    private fun checkSettings() {
        val isInitialized = settingsPreferences.getBoolean("isInitialized", false)
        if (!isInitialized) {
            initializeSettings()
        }
    }
    private fun updateNotificationBadge(count: Int ,notificationBadge : TextView) {
        Log.d("badgeLog","$count")

        if (count > 0) {
            notificationBadge.visibility = View.VISIBLE
            notificationBadge.text = count.toString()
        } else {
            notificationBadge.visibility = View.GONE
        }
    }



    private fun initializeSettings() {
        //Fetch Database Settings From RemoteDB

    }



    private fun showLocalUserDialog(user : Users) {
        val dialog = CreateUserDialogFragment()


        dialog.show(supportFragmentManager, "CreateUserDialog")
    }

    private fun useRemoteDB(): Boolean {
        return sharedPreferences.getBoolean("useRemoteDB", false)
    }
    private fun showSynchronizationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Synchronization")
            .setMessage("Do you want to synchronize data?")
            .setPositiveButton("Yes") { _, _ ->
                //sync()
                syncData(this@MainActivity)
            }
            .setNegativeButton("No", null)
            .show()
    }


    private fun syncData(context: Context){
        Toast.makeText(this,"SyncData starts",Toast.LENGTH_SHORT).show()
        syncDataViewModel.startSync(context)
    }



    private fun replaceFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout1, fragment, tag)
            .commit()
        currentFragmentTag = tag // Keep track of the current fragment
        drawerLayout.closeDrawers()

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }




}


