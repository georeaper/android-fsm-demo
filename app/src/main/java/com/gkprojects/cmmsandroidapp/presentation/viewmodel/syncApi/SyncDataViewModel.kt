package com.gkprojects.cmmsandroidapp.presentation.viewmodel.syncApi

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gkprojects.cmmsandroidapp.data.local.entity.CategoryAsset
import com.gkprojects.cmmsandroidapp.data.local.entity.CheckForms
import com.gkprojects.cmmsandroidapp.data.local.entity.ContractEquipments
import com.gkprojects.cmmsandroidapp.data.local.entity.Customer
import com.gkprojects.cmmsandroidapp.data.local.entity.Equipments
import com.gkprojects.cmmsandroidapp.data.local.entity.Contracts
import com.gkprojects.cmmsandroidapp.data.local.entity.Tools
import com.gkprojects.cmmsandroidapp.data.local.entity.Departments
import com.gkprojects.cmmsandroidapp.data.local.entity.Tickets
import com.gkprojects.cmmsandroidapp.data.local.entity.TicketHistory
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportCheckForm
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportEquipment
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportInventory
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportTools
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReports
import com.gkprojects.cmmsandroidapp.data.local.entity.Inventory
import com.gkprojects.cmmsandroidapp.data.local.entity.Maintenances
import com.gkprojects.cmmsandroidapp.data.local.entity.Manufacturer
import com.gkprojects.cmmsandroidapp.data.local.entity.Tasks
import com.gkprojects.cmmsandroidapp.data.local.entity.Settings
import com.gkprojects.cmmsandroidapp.data.local.entity.ModelAsset
import com.gkprojects.cmmsandroidapp.data.local.entity.Synchronization
import com.gkprojects.cmmsandroidapp.data.local.entity.Users
import com.gkprojects.cmmsandroidapp.data.remote.api.SyncData
import com.gkprojects.cmmsandroidapp.data.remote.api.SyncDataRequest
import com.gkprojects.cmmsandroidapp.data.remote.api.SyncDataResponse
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.CategoryRepository
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.ManufacturerRepository
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.ModelRepository
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoCases
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoCheckForms
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoCheckListItems
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoContracts
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoCustomer
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoDepartments
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoEquipment
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoFieldReportEquipment
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoInventory
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoMaintenances
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoSpecialTools
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoTasks
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoTicketHistory
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoUsers
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrderInventory
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrderTools
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepoWorkOrders
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.RepositoryContractEquipment
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.SettingsRepository
import com.gkprojects.cmmsandroidapp.data.repositoryImpl.SynchronizationRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.util.UUID

class SyncDataViewModel(
    private val syncDataApi: SyncData
) : ViewModel() {

    fun startSync(context: Context) {
        viewModelScope.launch {
            try {
                syncAllData(context)
            } catch (e: Exception) {
                Log.e("SyncError", "Error during sync: ${e.message}")
            }
        }
    }

    private suspend fun syncAllData(context: Context) {
        // Sync Customers
        val customers = RepoCustomer.getAllCustomersForSync(context)
        syncEntity("Customer", customers, context)



        val users = RepoUsers.getAllListForSync(context)
        syncEntity("Users", users, context)

        val inventory = RepoInventory.getAllListForSync(context)
        syncEntity("Inventory", inventory, context)

        val categoryAsset = CategoryRepository.getInstance(context).getAllListForSync()
        syncEntity("CategoryAsset", categoryAsset, context)

        val tools = RepoSpecialTools.getAllListForSync(context)
        syncEntity("Tools", tools, context)

        val maintenances = RepoMaintenances.getAllListForSync(context)
        syncEntity("Maintenances", maintenances, context)

        val manufacturer = ManufacturerRepository.getInstance(context).getAllListForSync()
        syncEntity("Manufacturer", manufacturer, context)

        val modelAsset = ModelRepository.getInstance(context).getAllListForSync()
        syncEntity("ModelAsset", modelAsset, context)

        val checkForms = RepoCheckForms.getAllListForSync(context)
        syncEntity("CheckForms", checkForms, context)

        val settings = SettingsRepository.getInstance(context).getAllListForSync()
        syncEntity("Settings", settings, context)



        val contracts = RepoContracts.getInstance(context).getAllListForSync()
        syncEntity("Contracts", contracts, context)

        val departments = RepoDepartments.getAllListForSync(context)
        syncEntity("ContractEquipments", departments, context)

        val equipments = RepoEquipment.getInstance(context).getAllListForSync()
        syncEntity("Equipment", equipments, context)

        val contractEquipments = RepositoryContractEquipment.getInstance(context).getAllListForSync()
        syncEntity("ContractEquipments", contractEquipments, context)

        //val fieldReports = RepoWorkOrders.getAllListForSync(context)
        val fieldReports = RepoWorkOrders.getInstance(context).getAllListForSync()
        syncEntity("FieldReports", fieldReports, context)

        val fieldReportCheckForm = RepoCheckListItems.getInstance(context).getAllListForSync()
        syncEntity("FieldReportCheckForm", fieldReportCheckForm, context)

        val fieldReportEquipment = RepoFieldReportEquipment.getInstance(context).getAllListForSync()
        syncEntity("FieldReportEquipment", fieldReportEquipment, context)

        val fieldReportInventory = RepoWorkOrderInventory.getInstance(context).getAllListForSync()
        syncEntity("FieldReportInventory", fieldReportInventory, context)

        val fieldReportTools = RepoWorkOrderTools.getInstance(context).getAllListForSync()
        syncEntity("FieldReportTools", fieldReportTools, context)

        val tickets = RepoCases.getAllListForSync(context)
        syncEntity("Tickets", tickets, context)


        val ticketHistory = RepoTicketHistory.getAllListForSync(context)
        syncEntity("TicketHistory", ticketHistory, context)

        val tasks = RepoTasks.getAllListForSync(context)
        syncEntity("Tasks", tasks, context)



        // Add Equipments, WorkOrders if needed (you can call other sync methods for these entities)
    }

    @Suppress("UNCHECKED_CAST")
    private suspend fun <T> syncEntity(entityName: String, dataList: List<T>, context: Context) {
        val request = SyncDataRequest(
            entityName = entityName,
            timestamp = getEntityLastSyncTimestamp(context, entityName),
            data = dataList as List<Any>
        )
        Log.d("SyncRequest", "Request: $request")

        try {
            // Make API call
            val response = syncDataApi.syncData(request)
            Log.d("SyncResponse", "Response: $response")
            handleResponse(response, context)
        } catch (e: Exception) {
            Log.e("SyncError", "Error syncing entity $entityName: ${e.message}")
        }
    }

    private suspend fun handleResponse(response: SyncDataResponse, context: Context) {
        // Handle different entities based on the response
        when (response.entityName) {
            "Customer" -> {
                Log.d("SyncResponseCustomer", "started")

// Ensure response.data is not null or empty
                if (response.data.isEmpty()) {
                    Log.e("SyncResponse", "No customer data received.")
                //    viewModelScope.launch {
                        updateSyncHistory(context, response)
               //     }

                }else{
                    // Log the raw data (to inspect what is being received)
                    Log.d("SyncResponse", "Customers1: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Customer>>() {}.type
                    val customerList: List<Customer> = gson.fromJson(json, type)

// Log the parsed customer list
                    Log.d("SyncResponse", "Customers2: $customerList")

// Insert into the database within a coroutine
    //                viewModelScope.launch {
                        try {
                            // Insert or update customers into the database
                            RepoCustomer.insertOrUpdateCustomers(context, customerList)
                            updateSyncHistory(context, response)

                        } catch (e: Exception) {
                            Log.e("SyncResponse", "Error inserting/updating customers: ${e.message}")
                        }
                    }



         //       }


                //updateDatabase(context, customers,"Customer")
            }
            "Equipment" -> {
                Log.d("SyncResponseEquipment", "started")

// Ensure response.data is not null or empty
                if (response.data.isEmpty()) {
                    Log.e("SyncResponse", "No equipment data received.")

                //    viewModelScope.launch {
                        updateSyncHistory(context, response)
              //      }

                }else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("SyncResponse", "Equipment: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Equipments>>() {}.type
                    val equipmentList: List<Equipments> = gson.fromJson(json, type)

// Log the parsed customer list

                    Log.d("SyncResponse", "Customers2: $equipmentList")
                    val repo = RepoEquipment.getInstance(context)
                    repo.insertOrUpdate(equipmentList)
                    updateSyncHistory(context, response)
           //         viewModelScope.launch {
//                        RepoEquipment.insertOrUpdate(context, equipmentList)
//                        updateSyncHistory(context, response)
                    }

            //    }

            }
            "Users" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncUsers", "No UserS data received.")
                    viewModelScope.launch {
                        updateSyncHistory(context, response)
                    }

                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("SyncUsers", "Users: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Users>>() {}.type
                    val userList: List<Users> = gson.fromJson(json, type)

                    viewModelScope.launch {
                        RepoUsers.insertOrUpdate(context, userList)
                        updateSyncHistory(context, response)
                    }
                }
            }
            "CategoryAsset" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncCategoryAsset", "No CategoryAsset data received.")

                        updateSyncHistory(context, response)


                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("CategoryAsset", "CategoryAsset: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<CategoryAsset>>() {}.type
                    val categoryAssetList: List<CategoryAsset> = gson.fromJson(json, type)


                    val repo = CategoryRepository.getInstance(context)
                        repo.insertOrUpdate(categoryAssetList)
                        updateSyncHistory(context, response)

                }
            }
            "CheckForms" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncCheckForms", "No CheckForms data received.")
                    viewModelScope.launch {
                        updateSyncHistory(context, response)
                    }

                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("CheckForms", "CheckForms: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<CheckForms>>() {}.type
                    val checkFormsList: List<CheckForms> = gson.fromJson(json, type)

                        RepoCheckForms.insertOrUpdate(context, checkFormsList)
                        //repo.insertOrUpdate(categoryAssetList)
                        updateSyncHistory(context, response)

                }
            }
            "ContractEquipments" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncContractEquipments", "No ContractEquipments data received.")

                        updateSyncHistory(context, response)


                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("ContractEquipments", "ContractEquipments: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<ContractEquipments>>() {}.type
                    val contractEquipmentsList: List<ContractEquipments> = gson.fromJson(json, type)

                        val repo= RepositoryContractEquipment.getInstance(context)
                        repo.insertOrUpdate(contractEquipmentsList)
                        //repo.insertOrUpdate(categoryAssetList)
                        updateSyncHistory(context, response)

                }
            }
            "Contracts" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncContracts", "No Contracts data received.")

                        updateSyncHistory(context, response)


                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("Contracts", "Contracts: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Contracts>>() {}.type
                    val contractList: List<Contracts> = gson.fromJson(json, type)

                        val repo= RepoContracts.getInstance(context)
                        repo.insertOrUpdate(contractList)
                        //repo.insertOrUpdate(categoryAssetList)
                        updateSyncHistory(context, response)

                }
            }
            "Departments" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncDepartments", "No Departments data received.")

                        updateSyncHistory(context, response)


                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("Departments", "Departments: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Departments>>() {}.type
                    val departmentsList: List<Departments> = gson.fromJson(json, type)

                        RepoDepartments.insertOrUpdate(context, departmentsList)
                        updateSyncHistory(context, response)

                }
            }
            "FieldReportCheckForm" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncFieldReportCheckForm", "No FieldReportCheckForm data received.")

                        updateSyncHistory(context, response)


                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("FieldReportCheckForm", "FieldReportCheckForm: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<FieldReportCheckForm>>() {}.type
                    val fieldReportCheckFormList: List<FieldReportCheckForm> = gson.fromJson(json, type)

                        RepoCheckListItems.getInstance(context).insertOrUpdate(fieldReportCheckFormList)
                        updateSyncHistory(context, response)

                }
            }
            "FieldReportEquipment" -> {
            //Log.d("SyncResponseUsers", "")
            if (response.data.isEmpty()) {
                Log.e("SyncFieldReportEquipment", "No FieldReportEquipment data received.")

                    updateSyncHistory(context, response)


            } else {
                // Log the raw data (to inspect what is being received)
                Log.d("FieldReportEquipment", "FieldReportEquipment: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                val gson = Gson()
                val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                val type = object : TypeToken<List<FieldReportEquipment>>() {}.type
                val list: List<FieldReportEquipment> = gson.fromJson(json, type)

                    RepoFieldReportEquipment.getInstance(context).insertOrUpdate(list)
                    updateSyncHistory(context, response)

            }
        }
            "FieldReportInventory" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncFieldReportInventory", "No FieldReportInventory data received.")

                        updateSyncHistory(context, response)


                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("FieldReportInventory", "FieldReportInventory: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<FieldReportInventory>>() {}.type
                    val list: List<FieldReportInventory> = gson.fromJson(json, type)

                        RepoWorkOrderInventory.getInstance(context).insertOrUpdate( list)
                        updateSyncHistory(context, response)

                }
            }
            "FieldReports" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncFieldReports", "No FieldReports data received.")

                        updateSyncHistory(context, response)


                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("FieldReports", "FieldReports: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<FieldReports>>() {}.type
                    val list: List<FieldReports> = gson.fromJson(json, type)

                        RepoWorkOrders.getInstance(context).insertOrUpdate( list)
                        updateSyncHistory(context, response)

                }
            }
            "FieldReportTools" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncFieldReportTools", "No FieldReportTools data received.")

                        updateSyncHistory(context, response)


                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("FieldReportTools", "FieldReportTools: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<FieldReportTools>>() {}.type
                    val list: List<FieldReportTools> = gson.fromJson(json, type)

                        RepoWorkOrderTools.getInstance(context).insertOrUpdate(list)
                        updateSyncHistory(context, response)

                }
            }
            "Inventory" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncInventory", "No Inventory data received.")

                        updateSyncHistory(context, response)


                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("Inventory", "Inventory: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Inventory>>() {}.type
                    val list: List<Inventory> = gson.fromJson(json, type)
              //      viewModelScope.launch {
                        RepoInventory.insertOrUpdate(context, list)
                        updateSyncHistory(context, response)
               //     }
                }
            }
            "Maintenances" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncMaintenances", "No Maintenances data received.")
               //     viewModelScope.launch {
                        updateSyncHistory(context, response)
               //     }

                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("Maintenances", "Maintenances: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Maintenances>>() {}.type
                    val list: List<Maintenances> = gson.fromJson(json, type)
               //     viewModelScope.launch {
                        RepoMaintenances.insertOrUpdate(context, list)
                        updateSyncHistory(context, response)
              //      }
                }
            }
            "Manufacturer" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncManufacturer", "No Manufacturer data received.")
                //    viewModelScope.launch {
                        updateSyncHistory(context, response)
              //      }

                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("Manufacturer", "Manufacturer: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Manufacturer>>() {}.type
                    val list: List<Manufacturer> = gson.fromJson(json, type)
              //      viewModelScope.launch {
                        val repo= ManufacturerRepository.getInstance(context)
                        //ManufacturerRepository.insertOrUpdate(context, list)
                        repo.insertOrUpdate(list)
                        updateSyncHistory(context, response)
              //      }
                }
            }
            "ModelAsset" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncModelAsset", "No ModelAsset data received.")
           //         viewModelScope.launch {
                        updateSyncHistory(context, response)
           //         }

                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("ModelAsset", "ModelAsset: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<ModelAsset>>() {}.type
                    val list: List<ModelAsset> = gson.fromJson(json, type)
            //       viewModelScope.launch {
                        val repo= ModelRepository.getInstance(context)
                        //ManufacturerRepository.insertOrUpdate(context, list)
                        repo.insertOrUpdate(list)
                        updateSyncHistory(context, response)
            //        }
                }
            }
            "Settings" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncSettings", "No Settings data received.")
          //          viewModelScope.launch {
                        updateSyncHistory(context, response)
           //         }

                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("Settings", "Settings: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Settings>>() {}.type
                    val list: List<Settings> = gson.fromJson(json, type)
             //       viewModelScope.launch {
                        val repo= SettingsRepository.getInstance(context)
                        //ManufacturerRepository.insertOrUpdate(context, list)
                        repo.insertOrUpdate(list)
                        updateSyncHistory(context, response)
            //        }
                }
            }
            "Tasks" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncTasks", "No Tasks data received.")
           //         viewModelScope.launch {
                        updateSyncHistory(context, response)
           //         }

                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("Tasks", "Tasks: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Tasks>>() {}.type
                    val list: List<Tasks> = gson.fromJson(json, type)
             //       viewModelScope.launch {
                        //val repo= RepoTasks.getInstance(context)
                        RepoTasks.insertOrUpdate(context, list)
                        //repo.insertOrUpdate(list)
                        updateSyncHistory(context, response)
              //      }
                }
            }
            "TicketHistory" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncTicketHistory", "No TicketHistory data received.")
          //          viewModelScope.launch {
                        updateSyncHistory(context, response)
          //          }

                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("TicketHistory", "TicketHistory: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<TicketHistory>>() {}.type
                    val list: List<TicketHistory> = gson.fromJson(json, type)
           //         viewModelScope.launch {
                        //val repo= RepoTicketHistory.getInstance(context)
                        RepoTicketHistory.insertOrUpdate(context, list)
                       // repo.insertOrUpdate(list)
                        updateSyncHistory(context, response)
          //          }
                }
            }
            "Tickets" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncTickets", "No Tickets data received.")
         //           viewModelScope.launch {
                        updateSyncHistory(context, response)
         //           }

                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("Tickets", "Tickets: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Tickets>>() {}.type
                    val list: List<Tickets> = gson.fromJson(json, type)
             //       viewModelScope.launch {
                        //val repo= RepoTicketHistory.getInstance(context)
                        RepoCases.insertOrUpdate(context, list)
                        // repo.insertOrUpdate(list)
                        updateSyncHistory(context, response)
            //        }
                }
            }
            "Tools" -> {
                //Log.d("SyncResponseUsers", "")
                if (response.data.isEmpty()) {
                    Log.e("SyncTools", "No Tools data received.")
            //        viewModelScope.launch {
                        updateSyncHistory(context, response)
            //        }

                } else {
                    // Log the raw data (to inspect what is being received)
                    Log.d("Tools", "Tools: ${response.data}")

// Gson Parsing: Convert to CustomerResponse object
                    val gson = Gson()
                    val json = gson.toJson(response.data) // Convert List<Map<*, *>> to JSON string
                    val type = object : TypeToken<List<Tools>>() {}.type
                    val list: List<Tools> = gson.fromJson(json, type)
             //       viewModelScope.launch {
                        //val repo= RepoTicketHistory.getInstance(context)
                        RepoSpecialTools.insertOrUpdate(context, list)
                        // repo.insertOrUpdate(list)
                        updateSyncHistory(context, response)
           //         }
                }
            }
            // Handle other entities like Equipments, WorkOrders, etc.
        }
    }


    // This has to return the last sync date from the database per Entity
    //But with the current Query 10/5/2025 we are not returning 1 sync date per entity
    private suspend fun getEntityLastSyncTimestamp(context: Context, entityName: String): String {
        val repo = SynchronizationRepository.getInstance(context)
        return try {
            val syncData = repo.getLastSyncDate(entityName)
            // Use Elvis operator to return the default date if LastSyncDate is null or empty
            syncData.LastSyncDate?.takeIf { it.isNotEmpty() } ?: "01/01/2010 00:00:00"
        } catch (e: Exception) {
            Log.e("SyncTimestampError", "Failed to get last sync date: ${e.message}")
            "01/01/2010 00:00:00" // Default start date in case of an error
        }
    }

    private suspend fun updateSyncHistory(context: Context, response: SyncDataResponse) {
        val repo = SynchronizationRepository.getInstance(context)
        viewModelScope.launch {
            val syncRecord = Synchronization(
                SyncID = UUID.randomUUID().toString(), // or use meaningful ID
                Entity = response.entityName,
                Status = "Success",
                Notes = null,
                LastSyncDate = null )// will be set in `insert(...)` automatically

            try {
                repo.insert(syncRecord)
            } catch (e: Exception) {
                Log.e("SyncInsertError", "Failed to insert sync data: ${e.message}")
            }
        }
    }

}

