package com.gkprojects.cmmsandroidapp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.dto.ContractRemindersSettings
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentReminderSettings
import com.gkprojects.cmmsandroidapp.data.local.dto.ReminderSettingsGeneric
import com.gkprojects.cmmsandroidapp.data.local.entity.Settings
import com.gkprojects.cmmsandroidapp.data.local.serialization.SimpleJson
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.SettingsRepository
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer
import java.util.UUID
import com.gkprojects.cmmsandroidapp.data.local.entity.Settings as AppSettings


class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    // LiveData for insert success status
    private val _insertSuccess = MutableLiveData<Boolean>()
    val insertSuccess: LiveData<Boolean> get() = _insertSuccess

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> get() = _deleteSuccess
    // LiveData for loaded settings
    private val _settingsData = MutableLiveData<List<AppSettings>>()
    val settingsData: LiveData<List<AppSettings>> get() = _settingsData

    // LiveData for error messages
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Function to insert settings
    fun insertSettings(settings: AppSettings) {
        viewModelScope.launch {
            try {
                val result = repository.insertSettings(settings)
                _insertSuccess.value = result > 0
            } catch (e: Exception) {
                _insertSuccess.value = false
                _error.value = e.message // Handle error if needed
            }
        }
    }

    fun addReminders(value:String,key: String){
        viewModelScope.launch {
            //val key = "remindersOnContracts"
            val setting =Settings(UUID.randomUUID().toString(),null,key,value,null,null,null,null,null)
            try {
                val result = repository.insertSettings(setting)
                _insertSuccess.value = result > 0
            } catch (e: Exception) {
                _insertSuccess.value = false
                _error.value = e.message // Handle error if needed
            }

        }

    }
    private fun getReminders(key:String){
        viewModelScope.launch {
            try {
                loadSettingsByKey(key)
            }catch (e:Exception){
                throw e
            }
        }

    }
    fun getRemindersOnContracts(){
        val key = "remindersOnContracts"
        getReminders(key)
    }

    fun getRemindersOnTechnicalCases(){
        val key ="remindersOnTechnicalCases"
        getReminders(key)
    }

    fun getRemindersOnEquipments(){
        val key="remindersOnEquipments"
        getReminders(key)
    }
    fun getRemindersOnCustomers(){
        val key="remindersOnCustomers"
        getReminders(key)
    }
    fun getRemindersOnTasks(){
        val key="remindersOnTasks"
        getReminders(key)
    }
    fun getRemindersOnWorkOrders(){
        val key="remindersOnWorkOrders"
        getReminders(key)
    }
    fun getRemindersOnSpecialTools(){
        val key="remindersOnSpecialTools"
        getReminders(key)
    }

    // Function to load settings by key
    fun loadSettingsByKey(key: String) {
        viewModelScope.launch {
            try {
                val result = repository.getSettingsByKey(key)
                _settingsData.value = result // Update LiveData with the fetched data
            } catch (e: Exception) {
                _error.value = e.message // Handle error if needed
            }
        }
    }
    fun deleteSettings(settings: AppSettings){
        viewModelScope.launch {
            try {
                val deletion = repository.deleteSettings(settings)
                _deleteSuccess.value= deletion>0
            }catch (e:Exception){
                _deleteSuccess.value=false
                _error.value=e.message

            }
        }
    }
    fun addOrUpdateContractReminders(settings:ContractRemindersSettings) {
        val key = "remindersOnContracts"
        viewModelScope.launch {
            try {
                // Load current settings
                val currentSettingsEntity = repository.getSettingsByKey(key).firstOrNull()


                if (currentSettingsEntity != null) {
                    val newSetting = SimpleJson.encodeToString(ContractRemindersSettings.serializer() ,settings)
                    val updatedEntity = currentSettingsEntity.copy(SettingsValue = newSetting)
                    repository.updateSettings(updatedEntity)
                    _insertSuccess.value = true
                } else {
                    val newSetting = SimpleJson.encodeToString(ContractRemindersSettings.serializer() ,settings)
                    addReminders(newSetting,key)
                }

                // Refresh LiveData
                loadSettingsByKey(key)

            } catch (e: Exception) {
                _error.value = e.message
                _insertSuccess.value = false
            }
        }
    }

    fun addOrUpdateEquipmentReminders(settings: EquipmentReminderSettings) {
        val key = "remindersOnEquipments"
        viewModelScope.launch {
            try {
                // Load current settings
                val currentSettingsEntity = repository.getSettingsByKey(key).firstOrNull()


                if (currentSettingsEntity != null) {
                    val newSetting = SimpleJson.encodeToString(EquipmentReminderSettings.serializer() ,settings)
                    val updatedEntity = currentSettingsEntity.copy(SettingsValue = newSetting)
                    repository.updateSettings(updatedEntity)
                    _insertSuccess.value = true
                } else {
                    val newSetting = SimpleJson.encodeToString(EquipmentReminderSettings.serializer() ,settings)
                    addReminders(newSetting,key)
                }

                // Refresh LiveData
                loadSettingsByKey(key)

            } catch (e: Exception) {
                _error.value = e.message
                _insertSuccess.value = false
            }
        }

    }

    fun saveTechnicalCasesReminders(settings : ReminderSettingsGeneric){
        addOrUpdateReminders(
            key ="remindersOnTechnicalCases",
            settings =settings,
            serializer = ReminderSettingsGeneric.serializer()
        )
    }


    fun saveCustomerReminders(settings: ReminderSettingsGeneric) {
        addOrUpdateReminders(
            key = "remindersOnCustomers",
            settings = settings,
            serializer = ReminderSettingsGeneric.serializer()
        )
    }
    fun saveSpecialToolsReminders(settings: ReminderSettingsGeneric){
        addOrUpdateReminders(
            key = "remindersOnSpecialTools",
            settings = settings,
            serializer = ReminderSettingsGeneric.serializer()
        )
    }
    fun saveTasksReminders(settings: ReminderSettingsGeneric){
        addOrUpdateReminders(
            key = "remindersOnTasks",
            settings = settings,
            serializer = ReminderSettingsGeneric.serializer()
        )
    }
    fun saveWorkOrdersReminders(settings: ReminderSettingsGeneric){
        addOrUpdateReminders(
            key = "remindersOnWorkOrders",
            settings = settings,
            serializer = ReminderSettingsGeneric.serializer()
        )
    }
    fun <T : ReminderSettingsGeneric> addOrUpdateReminders(
        key: String,
        settings: T,
        serializer: KSerializer<T>
    ) {
        viewModelScope.launch {
            try {
                val currentEntity = repository.getSettingsByKey(key).firstOrNull()
                val json = SimpleJson.encodeToString(serializer, settings)

                if (currentEntity != null) {
                    val updated = currentEntity.copy(SettingsValue = json)
                    repository.updateSettings(updated)
                    _insertSuccess.value = true
                } else {
                    addReminders(json, key)
                }

                // Refresh LiveData
                loadSettingsByKey(key)

            } catch (e: Exception) {
                _error.value = e.message
                _insertSuccess.value = false
            }
        }
    }



}
