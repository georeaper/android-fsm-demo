package com.gkprojects.cmmsandroidapp.presentation.ui.Contracts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.entity.ContractEquipments
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect
import com.gkprojects.cmmsandroidapp.data.local.dto.DetailedContract
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepositoryContractEquipment
import kotlinx.coroutines.launch

class ContractEquipmentViewModel (private val repository: RepositoryContractEquipment): ViewModel(){
    private val _insertSuccess = MutableLiveData<Boolean>()
    val insertSuccess: LiveData<Boolean> get() = _insertSuccess

    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> get() = _updateSuccess

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> get() = _deleteSuccess

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _customerNameListData = MutableLiveData<List<CustomerSelect>>()
    val customerNameListData: LiveData<List<CustomerSelect>> get() = _customerNameListData

    private val _contractDetailData = MutableLiveData<List<DetailedContract>>()
    val contractDetailData : LiveData<List<DetailedContract>> get()= _contractDetailData

    private val _equipmentContractData = MutableLiveData<List<ContractEquipments>>()
    val equipmentContractData : LiveData<List<ContractEquipments>> get()= _equipmentContractData

    private val _singleEquipmentContractData = MutableLiveData<ContractEquipments>()
    val singleEquipmentContractData : LiveData<ContractEquipments> get()= _singleEquipmentContractData

     fun getContractEquipmentByID( id: String) {
        viewModelScope.launch {
            try {
                val result =repository.getContractEquipmentsById(id)
                _equipmentContractData.value=result

            }catch (e:Exception){
                _insertSuccess.value = false
                _error.value = e.message
            }
        }

    }
    fun getDetailedContractByID( id: String) {
        viewModelScope.launch {
            try {
                val result=repository.getDetailedContractByID(id)
                _contractDetailData.value=result

            }catch (e:Exception){
                _insertSuccess.value = false
                _error.value = e.message
            }
        }
       // return RepoContracts.getDetailedContractByID(context,id)
    }
    fun getContractEquipmentByContractEquipmentID(id: String) {
        viewModelScope.launch {
            try {
                val result = repository.getContractEquipmentByContractEquipmentID(id)
                _singleEquipmentContractData.value=result
            }catch (e:Exception){
                _insertSuccess.value = false
                _error.value = e.message
            }
        }
        //return RepoContracts.getContractEquipmentByContractEquipmentID(context,id)
    }
     fun deleteContractEquipment( contractEquipments: ContractEquipments){
        viewModelScope.launch {
            try {
                val deletion=repository.deleteContractEquipment(contractEquipments)
                _deleteSuccess.value= deletion>0
            }catch (e:Exception){
                _deleteSuccess.value = false
                _error.value = e.message
            }
        }
    }
     fun insertContractEquipment( contractEquipments: ContractEquipments){
        viewModelScope.launch {
            try {
                val result=repository.insertContractEquipmentIfNotExists(contractEquipments)
                _insertSuccess.value = result > 0
            }catch (e:Exception){
                _insertSuccess.value = false
                _error.value = e.message
            }
        }
        //RepoContracts.insertContractEquipmentIfNotExists(context,contractEquipments)
    }
}