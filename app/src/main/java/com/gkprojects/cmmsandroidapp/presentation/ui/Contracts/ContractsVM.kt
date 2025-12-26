package com.gkprojects.cmmsandroidapp.presentation.ui.Contracts

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.core.utils.FilterResult
import com.gkprojects.cmmsandroidapp.core.utils.OperationResult

import com.gkprojects.cmmsandroidapp.data.local.entity.Contracts
import com.gkprojects.cmmsandroidapp.data.local.dto.ContractsCustomerName
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoContracts
import kotlinx.coroutines.launch


class ContractsVM (private val repository: RepoContracts): ViewModel(){
    private val hasCredentials :Boolean = true

    private val _insertSuccess = MutableLiveData<Boolean>()
    val insertSuccess: LiveData<Boolean> get() = _insertSuccess

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> get() = _deleteSuccess
    // LiveData for loaded settings
    private val _contractsData = MutableLiveData<List<Contracts>>()
    val contractsData: LiveData<List<Contracts>> get() = _contractsData

    private val _singleContractData = MutableLiveData<Contracts>()
    val singleContractData: LiveData<Contracts> get() = _singleContractData

    private val _customerData = MutableLiveData<List<CustomerSelect>>()
    val customerData: LiveData<List<CustomerSelect>> get() = _customerData

    val customerNameListData: LiveData<List<CustomerSelect>> get() = _customerNameListData
    private val _customerNameListData = MutableLiveData<List<CustomerSelect>>()

    // Add a LiveData getter for the full list
    val filteredList: LiveData<List<ContractsCustomerName>> get() = _customerNameContractList
    private val _filteredList = MutableLiveData<List<ContractsCustomerName>>()


    val customerNameContractList :LiveData<List<ContractsCustomerName>> get()= _customerNameContractList
    private val _customerNameContractList=MutableLiveData<List<ContractsCustomerName>>()
    // LiveData for error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

     fun insert( contract: Contracts)
    {
        viewModelScope.launch {
            try {
                val result = repository.insert(contract)
                _insertSuccess.value = result > 0
            } catch (e: Exception) {
                _insertSuccess.value = false
                _error.value = e.message // Handle error if needed
            }
        }


    }

     fun getAllContractData()
    {
        viewModelScope.launch {
            try {
                val result = repository.getAllContractData()
                _contractsData.value = result // Update LiveData with the fetched data
            } catch (e: Exception) {
                _error.value = e.message // Handle error if needed
            }
        }

    }
     fun deleteContract(contract: Contracts){
        viewModelScope.launch {
            try {
                val deletion = repository.delete(contract)
                _deleteSuccess.value= deletion>0
            }catch (e:Exception){
                _deleteSuccess.value=false
                _error.value=e.message

            }
        }

    }
    fun delete(id: String,onResult: (OperationResult<Unit>) -> Unit){
        viewModelScope.launch {
            try {
                val contract =repository.getContractsById(id)
                if (contract==null){
                    onResult(OperationResult.Error("Contract not found"))
                    return@launch
                }
                    if (hasCredentials){
                        repository.delete(contract)
                        onResult(OperationResult.Success())
                    }else{
                        onResult(OperationResult.Error("You don’t have permission to delete this item"))
                    }




            }catch (e:SQLiteConstraintException)
            {
                throw e
            }
        }

    }
     fun updateContract(contract: Contracts){

        viewModelScope.launch {
            try {
                val update = repository.updateContractData(contract)
                _updateSuccess.value= update>0
            }catch (e:Exception){
                _updateSuccess.value=false
                _error.value=e.message

            }
        }

    }
     fun getCustomerId() {
       // return RepoCases.getCustomerIdData(context)
        viewModelScope.launch {
            try {
                val result = repository.getCustomerIdData()
                _customerData.value = result // Update LiveData with the fetched data
            } catch (e: Exception) {
                _error.value = e.message // Handle error if needed
            }
        }

    }

     fun getCustomerName(){
        viewModelScope.launch {
            try {
                val result = repository.getCustomerIdData()
                _customerNameListData.value = result // Update LiveData with the fetched data
            } catch (e: Exception) {
                _error.value = e.message // Handle error if needed
            }
        }

    }
    fun getContractCustomerName(){
        viewModelScope.launch {
            try {
                val result = repository.getListContracts()
                _customerNameContractList.value = result // Update LiveData with the fetched data

            } catch (e: Exception) {
                _error.value = e.message // Handle error if needed
            }
        }

    }
     fun getContractByID(id :String){
        viewModelScope.launch {
            try {
                val result = repository.getContractsById(id)
                _singleContractData.value = result // Update LiveData with the fetched data
            } catch (e: Exception) {
                _error.value = e.message // Handle error if needed
            }
        }

    }
    fun applyFilters(filter: FilterResult) {
        val allContracts = _customerNameContractList.value ?: return
           // _contractsData.value

        var filtered = allContracts

        // Filter by Customers
        val selectedCustomers = filter.values["Customers"] as? Set<String>
        if (!selectedCustomers.isNullOrEmpty()) {
            filtered = filtered.filter { it.CustomerName in selectedCustomers }
        }
        val type =filter.values["Contract types"] as? Set<String>
        if (!type.isNullOrEmpty()){
            filtered=filtered.filter { it.ContractType in type }
        }

        // Filter by Status (true / false / null)
        val status = filter.values["Active"] as? Boolean?
        if (status != null) {
            filtered = filtered.filter { it.ContractStatus == status } // assuming Active:Boolean
        }

        // Filter by Date Range (if exists)
        val from = filter.values["Date_from"] as? String
        val to = filter.values["Date_to"] as? String
        if (from != null && to != null) {
            filtered = filtered.filter { contract ->
                val contractDate = contract.DateStart // adjust field name
                contractDate!! >= from && contractDate <= to
            }
        }

        _filteredList.value = filtered
    }


}