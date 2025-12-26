package com.gkprojects.cmmsandroidapp.presentation.ui.Inventory

import ConfirmDeleteDialog
import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gkprojects.cmmsandroidapp.data.local.entity.Inventory
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.core.utils.FilterType
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult
import com.gkprojects.cmmsandroidapp.core.utils.UiOperationMessages
import com.gkprojects.cmmsandroidapp.databinding.DialogInventoryInsertBinding
import com.gkprojects.cmmsandroidapp.databinding.FragmentInventoryBinding
import com.gkprojects.cmmsandroidapp.presentation.common.ModalFilterWindow
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import java.util.Locale
import java.util.UUID


class InventoryFragment : Fragment() {
    private lateinit var binding: FragmentInventoryBinding
    private lateinit var inventoryVM : InventoryVM
    private lateinit var adapterInventory : AdapterInventoryRecyclerView
    private var inventoryList =ArrayList<Inventory>()

    private var lastFilterValues: Map<String, Any?> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        val activity =requireActivity()

        val drawerLayout = activity.findViewById<DrawerLayout>(R.id.DrawLayout)
        val navView: NavigationView = activity.findViewById(R.id.navView)
        val toolbar: MaterialToolbar = activity.findViewById(R.id.main_toolbar)

        val drawable = ContextCompat.getDrawable(activity, R.drawable.menu_svgrepo_com)
        toolbar.navigationIcon = drawable

        val toggle = ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInventoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //search visibility set to GONE
        binding.filterIcon.visibility=View.GONE

        inventoryVM=ViewModelProvider(this)[InventoryVM ::class.java]

        try {

            inventoryVM.getAllInventory(requireContext()).observe(viewLifecycleOwner, Observer{
                Log.d("inventory","$it")
                val tempList : ArrayList<Inventory> = it as ArrayList<Inventory>
                Log.d("inventory2","$tempList")
                inventoryList=tempList
                Log.d("inventory3","$inventoryList")

                adapterInventory.setData(inventoryList)
            })
        }catch (e :Exception){
            Log.d("inventoryExceptionCatch","$e")
        }

        adapterInventory = AdapterInventoryRecyclerView(inventoryList)
        binding.inventoryRecyclerview.apply {
            adapter=adapterInventory
            setHasFixedSize(true)
            layoutManager= LinearLayoutManager(this.context)
        }
        adapterInventory.setOnClickListener(object : AdapterInventoryRecyclerView.OnClickListener {


            override fun onClick(position: Int, model: Inventory) {
                openDialogDisplayTools(model)
            }

        })

        adapterInventory.setOnLongClickListener(object :AdapterInventoryRecyclerView.OnLongClickListener{
            override fun onLongClick(position: Int, model: Inventory) {
                ConfirmDeleteDialog(
                    message = "Delete ${model.Title} - ${model.Description}?",
                    onConfirm = {
                        inventoryVM.deleteInventory(requireContext(),model,
                            ){
                            result->
                            UiOperationMessages.handleResult(
                                context =requireContext(),
                                result = result,
                                rootView = binding.root,
                                successMessage = "Inventory deleted successfully"
                            )
                            if (result is OperationResult.Success){
                                adapterInventory.removeItemAt(position)

                            }
                        }

                    }
                    ).show(childFragmentManager, "ConfirmDeleteInventory")

            }

        })

        binding.inventoryFloatingBtn.setOnClickListener {
            openDialogInsertInventory()
        }
        val searchBar = binding.searchEditTextInventory
        binding.filterIcon.setOnClickListener {
            filterDialog()
        }
        val searchIcon=binding.searchIcon

        searchIcon.setOnClickListener {
            searchBar.text.clear()
        }
        binding.filterIcon.setOnClickListener {
            showFilterModal()
        }

        searchBar.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchBar.hint=""
                if (s?.isNotEmpty() == true) {

                    filterList(s.toString().lowercase(Locale.ROOT))
                    searchIcon.setImageResource(R.drawable.search_cancel_clear)
                }
                else{

                    searchIcon.setImageResource(R.drawable.search_icon)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())

            }

        })




    }

    private fun filterDialog() {

    }

    private fun openDialogInsertInventory() {

        InventoryInsertDialog{ it ->
            insertInventoryIntoDatabase(it)
            inventoryList.add(it)
            adapterInventory.setData(inventoryList)
        }.show(parentFragmentManager, "InventoryInsertDialog")
//        val dialogBinding = DialogInventoryInsertBinding.inflate(LayoutInflater.from(requireContext()))
//
//
//        val dialog = AlertDialog.Builder(requireContext())
//            .setView(dialogBinding.root)
//            .setPositiveButton("OK", null) // set listener later so we can override close behavior
//            .setNegativeButton("Cancel", null)
//            .create()
//
//        dialog.setOnShowListener {
//            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
//            button.setOnClickListener {
//                // 1. Get strings
//                val title = dialogBinding.dialogInventoryInsertEditTextPartNumber.text.toString()
//                val description = dialogBinding.dialogInventoryInsertEditTextDescription.text.toString()
//                val valueStr = dialogBinding.dialogInventoryInsertEditTextValue.text.toString()
//                val qtyStr = dialogBinding.dialogInventoryInsertEditTextQuantity.text.toString()
//
//                // 2. Convert safely
//                val value = valueStr.toDoubleOrNull()
//                val qty = qtyStr.toLongOrNull()
//
//                // 3. Validate
//                var hasError = false
//
//                if (value == null) {
//                    dialogBinding.dialogInventoryInsertEditTextValue.error = "Enter a valid number"
//                    hasError = true
//                }
//
//                if (qty == null) {
//                    dialogBinding.dialogInventoryInsertEditTextQuantity.error = "Enter a valid quantity"
//                    hasError = true
//                }
//
//                if (hasError) return@setOnClickListener // DON'T close dialog
//
//                // 4. Create Inventory object only AFTER validation
//                val inventory = Inventory(
//                    UUID.randomUUID().toString(),
//                    null, null, null, null, null, null, null, null, null
//                )
//
//                inventory.Title = title
//                inventory.Description = description
//                inventory.Value = value
//                inventory.Quantity = qty
//
//                // 5. Insert safely
//                try {
//                    insertInventoryIntoDatabase(inventory)
//                    inventoryList.add(inventory)
//                    adapterInventory.setData(inventoryList)
//                } catch (e: Exception) {
//                    Log.d("inventoryInsertError", "$e")
//                }
//
//                dialog.dismiss() // manually close AFTER success
//            }
//        }
//
//        dialog.show()
//
//
//        // Set the dimensions of the dialog
//        val width = ViewGroup.LayoutParams.MATCH_PARENT
//        val height = ViewGroup.LayoutParams.WRAP_CONTENT
//        dialog.window?.setLayout(width, height)
    }

    fun openDialogDisplayTools(inventory: Inventory) {
        val dialogBinding = DialogInventoryInsertBinding.inflate(LayoutInflater.from(requireContext()))

        // Set the fields in the dialog to the values from the selected Tools object
        dialogBinding.dialogInventoryInsertEditTextPartNumber.setText(inventory.Title)
        dialogBinding.dialogInventoryInsertEditTextDescription.setText(inventory.Description)
        dialogBinding.dialogInventoryInsertEditTextQuantity.setText(inventory.Quantity.toString())
        dialogBinding.dialogInventoryInsertEditTextValue.setText(inventory.Value.toString())


        // Disable the EditTexts so the user can't edit the values
        dialogBinding.dialogInventoryInsertEditTextPartNumber.isEnabled = false
       dialogBinding.dialogInventoryInsertEditTextDescription.isEnabled = false
//        dialogBinding.dialogInventoryInsertEditTextQuantity.isEnabled=false
//        dialogBinding.dialogInventoryInsertEditTextValue.isEnabled=false


        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setPositiveButton("OK"){
                    _, _ ->
                inventory.Quantity = dialogBinding.dialogInventoryInsertEditTextQuantity.text.toString().toLong()
                inventory.Value = dialogBinding.dialogInventoryInsertEditTextValue.text.toString().toDouble()

                try{
                    inventoryVM.updateInventory(requireContext(),inventory)
                }catch (e:Exception){
                    Log.d("toolsUpdateCatchError","$e")
                }

            }
            .create()

        dialog.show()

        // Set the dimensions of the dialog
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
    }

    private fun insertInventoryIntoDatabase(inventory: Inventory){
        inventoryVM.insertInventory(requireContext(),inventory)

    }
    private fun filterList(query:String){
        if (query!=null){
            val filteredList= ArrayList<Inventory>()
            for (i in inventoryList){
                if (i.Title!!.lowercase(Locale.ROOT).contains(query)||i.Description!!.lowercase(Locale.ROOT).contains(query))
                    filteredList.add(i)
                Log.d("filteredInventory", filteredList.toString())
            }
            if (filteredList.isEmpty() ){
                adapterInventory.setData(filteredList)
                Toast.makeText(context,"Empty List", Toast.LENGTH_SHORT).show()

            }else{
                adapterInventory.setData(filteredList)
            }
        }

    }

    private fun showFilterModal() {
        val filters = listOf(
            FilterType.ListFilter("Customers", listOf("Alpha Co", "Beta Ltd", "Gamma Inc")),
            FilterType.StatusFilter("Active"),
            FilterType.DateRangeFilter("Created Date"),
            FilterType.ListFilter("Inventory", listOf("Inve1", "Inventory2", "In3"))
        )

        val filterModal = ModalFilterWindow(filters, { result ->
//            val selectedCustomers = result.values["Customers"] as? Set<String> ?: emptySet()
//            val activeStatus = result.values["Active"] as? Boolean?
//            val dateFrom = result.values["Created Date_from"] as? String
//            val dateTo = result.values["Created Date_to"] as? String

            lastFilterValues=result.values

            // Example:
            // viewModel.applyFilters(selectedCustomers, activeStatus, dateFrom, dateTo)
        }, lastFilterValues )



        filterModal.show(parentFragmentManager, "CustomerFilterModal")
    }



}