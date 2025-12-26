package com.gkprojects.cmmsandroidapp.presentation.common


import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gkprojects.cmmsandroidapp.R
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect

import com.gkprojects.cmmsandroidapp.presentation.adapter.RvAdapterFindCustomers
import java.util.Locale

class CustomerSelectionDialog (
    private val context: Context,
    private val customers: ArrayList<CustomerSelect>,
    private val onCustomerSelect: (CustomerSelect) -> Unit
) {
    fun show() {
        val builder = AlertDialog.Builder(context)
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_searchable_spinner, null)
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded_background_customer_selection)

        val closeBtn=view.findViewById<ImageButton>(R.id.close_customer_selection_btn)
        closeBtn.setOnClickListener {
            dialog.dismiss()
        }
        val searchCustomerEditText=view.findViewById<EditText>(R.id.searchCustomerFind)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_searchable_TextView)
        val adapterCustomer = RvAdapterFindCustomers(context, customers)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = adapterCustomer
        }
        adapterCustomer.setOnClickListener (object : RvAdapterFindCustomers.OnClickListener {
            override fun onClick(position: Int, model: CustomerSelect) {
                onCustomerSelect(model)
                dialog.dismiss()
            }
        })
        searchCustomerEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().lowercase(Locale.getDefault())
                val filteredList = customers.filter { it.CustomerName?.lowercase(Locale.getDefault())?.contains(query) == true }
                adapterCustomer.setData(ArrayList(filteredList)) // assuming your adapter has setData()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
       
    }
}