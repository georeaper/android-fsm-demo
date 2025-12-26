package com.gkprojects.cmmsandroidapp.data.local.dto


data class CustomerSelect( var CustomerID : String?,
                           var CustomerName: String?)

data class CustomDisplayDatFieldReportEquipments(
    var idFieldReportEquipment :String?,
    var EquipmentID :String?,
    var Model :String?,
    var SerialNumber: String?,
    var CompletedStatus : Boolean?
)



data class EquipmentSelectCustomerName (
    var CustomerID :String? ,
    var CustomerName : String? ,
    var EquipmentID : String? ,
    var Name: String?,
    var SerialNumber: String?,
    var EquipmentStatus: Boolean?,
    var Model: String?,
    var Manufacturer: String?,
    var InstallationDate: String?,
    var EquipmentCategory: String?,
    var EquipmentVersion: String?,
    var Warranty : String?   ,
    var Description : String?)

data class ContractsCustomerName(
    var CustomerID :String?,
    var CustomerName : String? ,
    var ContractID :String?,
    var Title :String?,
    var DateStart :String?,
    var DateEnd :String?,
    var Value :Double?,
    var Notes :String?,
    var Description :String?,
    var ContractType :String?,
    var ContractStatus :Boolean?,
    var ContactName :String?,
)

data class TicketCustomerName(
     var TicketID :String?,
     var Title :String?,
     var Active :Boolean?,
     var DateStart: String?,
     var Urgency :String?,
     var CustomerName: String?,
     var UserID :String?,
     var SerialNumber : String?,
     var CustomerID :String?,
     var EquipmentID :String?

)
data class TicketCalendar(
    var TicketID :String?,
    var Title :String?,
    var Active :Boolean?,
    var DateStart: String?,
    var Urgency :String?,
    var CustomerName: String?,
    var UserID :String?,
    var SerialNumber : String?,
    var CustomerID :String?,
    var EquipmentID :String?,
    var Model: String?,
    var Manufacturer: String?
)
data class DashboardCustomerEquipmentDataClass(
    var EquipmentID: String?,
    var SerialNumber: String?,
    var Model: String?,
    var InstallationDate: String?
)
data class DashboardCustomerTechnicalCasesDataClass(
    var TicketID: String?,
    var Title: String?,
    var Urgency: String?,
    var DateStart: String?,
    var DateEnd :String?

)
data class DashboardCustomerWorkOrdersDataClass(
    var FieldReportID : String?,
    var ReportNumber : String?,
    var Title: String?,
    var DateCreated: String?
)
data class DashboardCustomerContractsDataClass(
    var ContractID: String?,
    var Title: String?,
    var ContractStatus: Boolean?,
    var DateEnd: String?,
    var ContractType: String?

)
data class EquipmentListInCases(
    var EquipmentID :String?,
    var SerialNumber :String?,
    var Model :String?,
    var CustomerID : String?
) {
    override fun toString(): String {
        return SerialNumber ?: ""
    }
}
data class DetailedContract(
    var ContractID: String?,
    var equipmentID: String?,
    var serialNumber: String?,
    var model: String?,
    var ContractEquipmentID : String?,
    var Value : Double?,
    var Visits : String?,
    var LastModified : String?,
    var DateCreated: String?,
    var Version : String?

)

data class WorkOrdersList(
    var workOrderID : String?,
    var customerName : String?,
    var reportNumber : String?,
    var title : String?,
    var dateOpened : String?,
    var dateClosed : String?,
    var status : Boolean?

)

