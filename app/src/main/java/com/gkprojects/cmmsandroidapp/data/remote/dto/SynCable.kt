package com.gkprojects.cmmsandroidapp.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface SynCable

@Serializable
@SerialName("Customer")
data class Customers(
    val CustomerID: String,
    val RemoteID: Double?, // ← JSON gave you 101.0, so use Double if needed
    val Name: String?,
    val Phone: String?,
    val Email: String?,
    val Address: String?,
    val ZipCode: String?,
    val City: String?,
    val Notes: String?,
    val Description: String?,
    val CustomerStatus: Boolean?,
    val LastModified: String?,
    val DateCreated: String?,
    val Version: String?
) : SynCable

@Serializable
@SerialName("Users")
data class Users(
    var UserID :String,
    var RemoteID :Int?=null,
    var Name :String?=null,
    var Description :String?=null,
    var Email :String?=null,
    var Phone :String?=null,
    val Signature: ByteArray? = null,
    var ReportPrefix :String?=null,
    var TechnicalCasePrefix :String?=null,
    var LastReportNumber :Int?=null,
    var LastTCNumber :Int?=null,
    var LastModified :String?=null,
    var DateCreated :String?=null,
    var Version :String?=null): SynCable

@Serializable
@SerialName("Equipment")
data class Equipments(
    var EquipmentID :String ,
    var RemoteID :Int? =null,
    var Name :String? =null,
    var SerialNumber :String? =null,
    var Model :String?=null ,
    var Manufacturer :String? =null,
    var Notes :String?=null ,
    var Description :String?=null ,
    var EquipmentVersion :String?=null ,
    var EquipmentCategory :String? =null,
    var Warranty :String? =null,
    var EquipmentStatus :Boolean?=null,
    var InstallationDate :String? =null,
    var LastModified :String? =null,
    var DateCreated :String? =null,
    var Version :String? =null,
    var CustomerID :String? =null): SynCable

@Serializable
@SerialName("CategoryAsset")
data class CategoryAsset  (
    var CategoryAssetID: String ,
    var RemoteID: Int? =null,
    var Name: String?=null,
    var Style: String?=null,
    var LastModified: String?=null,
    var DateCreated: String?=null,
    var Version: String?=null ): SynCable

@Serializable
@SerialName("CheckForms")
data class CheckForms(
    var CheckFormID: String ,
    var RemoteID: Int?=null,
    var MaintenancesID: String?=null,
    var Description: String?=null,
    var ValueExpected: String?=null,
    var ValueType: String?=null, //checkbox, Textview, Edittext, etc
    var LastModified: String?=null,
    var DateCreated: String?=null,
    var Version: String? =null ): SynCable

@Serializable
@SerialName("ContractEquipments")
data class ContractEquipments(
    var ContractEquipmentID: String ,
    var RemoteID: Int?=null,
    var Value: Double?=null,
    var Visits: Double?=null,
    var ContractID: String?=null,
    var EquipmentID: String?=null,
    var LastModified: String?=null,
    var DateCreated: String?=null,
    var Version: String?=null ): SynCable

@Serializable
@SerialName("Contracts")
data class Contracts(
    var ContractID: String ,
    var RemoteID: Int?=null,
    var Title: String?=null,
    var DateStart: String?=null,
    var DateEnd: String?=null,
    var Value: Double?=null,
    var Notes: String?=null,
    var Description: String?=null,
    var ContractType: String?=null,
    var ContractStatus: Boolean?=null,
    var ContactName: String?=null,
    var LastModified: String?=null,
    var DateCreated: String?=null,
    var Version: String?=null,
    var CustomerID: String?=null ): SynCable

@Serializable
@SerialName("Departments")
data class Departments(
    var DeparmentID: String ,
    var RemoteID: Int?,
    var Name: String?,
    var Phone: String?,
    var Email: String?,
    var ContactPerson: String?,
    var Notes: String?,
    var Description: String?,
    var DepartmentStatus: Boolean?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?,
    var CustomerID: String?
): SynCable

@Serializable
@SerialName("FieldReportCheckForm")
data class FieldReportCheckForm(
    var FieldReportCheckFormID: String ,
    var RemoteID: Int?,
    var FieldReportEquipmentID: String?,
    var Description: String?,
    var ValueExpected: String?,
    var ValueMeasured: String?,
    var Result: String?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?
): SynCable
@Serializable
@SerialName("FieldReportEquipment")
data class FieldReportEquipment(
    var FieldReportEquipmentID: String ,
    var RemoteID: Int?,
    var CompletedStatus: Boolean?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?,
    var FieldReportID: String?,
    var EquipmentID: String?,
    var MaintenanceID: String?
): SynCable
@Serializable
@SerialName("FieldReportInventory")
data class FieldReportInventory(
    var FieldReportInventoryID: String ,
    var RemoteID: Int?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?,
    var FieldReportID: String?,
    var InventoryID: String?
): SynCable
@Serializable
@SerialName("FieldReports")
data class FieldReports(
    var FieldReportID: String,
    var RemoteID: Int?,
    var ReportNumber: String?,
    var Description: String?,
    var StartDate: String?,
    var EndDate: String?,
    var Title: String?,
    var Department: String?,
    var ClientName: String?,
    var ReportStatus: String?,
    var ClientSignature: ByteArray?,
    var Value: Double?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?,
    var CustomerID: String?,
    var ContractID: String?,
    var UserID: String?,
    var CaseID: String? = null
): SynCable
@Serializable
@SerialName("FieldReportTools")
data class FieldReportTools(
    var FieldReportToolsID: String ,
    var RemoteID: Int?,
    var FieldReportID:String?,
    var ToolsID: String?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?
): SynCable
@Serializable
@SerialName("Inventory")
data class Inventory(
    var InventoryID: String ,
    var RemoteID: Int?,
    var Title: String?,
    var Description: String?,
    var Quantity: Long?,
    var Value: Double?,
    var Type: String?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?
): SynCable
@Serializable
@SerialName("Maintenances")
data class Maintenances(
    var MaintenanceID: String ,
    var RemoteID: Int?,
    var Name: String?,
    var Description: String?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?
): SynCable
@Serializable
@SerialName("Manufacturer")
data class Manufacturer(
    var ManufacturerID: String ,
    var RemoteID: Int?,
    var Name: String?,
    var Style: String?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?
): SynCable
@Serializable
@SerialName("ModelAsset")
data class ModelAsset(
    var ModelID: String ,
    var RemoteID: Int?,
    var Name: String?,
    var Style: String?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?
): SynCable
@Serializable
@SerialName("Settings")
data class Settings(
    var SettingsID: String ,
    var RemoteID: Int?,
    var SettingsKey: String?,
    var SettingsValue: String?,
    var SettingsStyle: String?,
    var SettingsDescription : String ? ,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?
): SynCable
@Serializable
@SerialName("Tasks")
data class Tasks(
    var TaskID: String ,
    var Title: String?,
    var Description: String?,
    var Status: String?, // E.g., "Pending", "In Progress", "Completed"
    var Priority: String?, // E.g., "Low", "Medium", "High"
    var DateStart: String?,
    var DateDue: String?,
    var DateCompleted: String?,
    var LastModified: String?,
    var DateCreated: String?,
    var TicketID: String?, // Links task to a ticket
    var UserID: String? // Assign task to a user
): SynCable
@Serializable
@SerialName("TicketHistory")
data class TicketHistory(
    var TicketHistoryID: String ,
    var RemoteID: Int?,
    var Title: String?,
    var TicketNumber: String?,
    var Description: String?,
    var Notes: String?,
    var Urgency: String?,
    var Active: Boolean?,
    var DateStart: String?,
    var DateEnd: String?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?,
    var UserID: String?,
    var CustomerID: String?,
    var EquipmentID: String?
): SynCable
@Serializable
@SerialName("Tickets")
data class Tickets(
    var TicketID: String ,
    var RemoteID: Int?,
    var Title: String?,
    var TicketNumber: String?,
    var Description: String?,
    var Notes: String?,
    var Urgency: String?,
    var Active: Boolean?,
    var DateStart: String?,
    var DateEnd: String?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?,
    var UserID: String?,
    var CustomerID: String?,
    var EquipmentID: String?
): SynCable
@Serializable
@SerialName("Tools")
data class Tools(
    var ToolsID: String ,
    var RemoteID: Int?,
    var Title: String?,
    var Description: String?,
    var Model: String?,
    var Manufacturer: String?,
    var SerialNumber: String?,
    var CalibrationDate: String?,
    var LastModified: String?,
    var DateCreated: String?,
    var Version: String?
): SynCable
