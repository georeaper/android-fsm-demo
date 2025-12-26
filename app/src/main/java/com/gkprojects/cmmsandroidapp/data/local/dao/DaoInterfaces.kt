package com.gkprojects.cmmsandroidapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportInventoryCustomData
import com.gkprojects.cmmsandroidapp.data.local.dto.FieldReportToolsCustomData
import com.gkprojects.cmmsandroidapp.data.local.dto.ContractsCustomerName
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomCheckListWithEquipmentData
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomDisplayDatFieldReportEquipments
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomWorkOrderPDFDATA
import com.gkprojects.cmmsandroidapp.data.local.dto.CustomerSelect
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerContractsDataClass
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerEquipmentDataClass
import com.gkprojects.cmmsandroidapp.data.local.dto.DashboardCustomerTechnicalCasesDataClass
import com.gkprojects.cmmsandroidapp.data.local.dto.DetailedContract
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentListInCases
import com.gkprojects.cmmsandroidapp.data.local.dto.EquipmentSelectCustomerName
import com.gkprojects.cmmsandroidapp.data.local.dto.OverviewMainData
import com.gkprojects.cmmsandroidapp.data.local.dto.TicketCalendar
import com.gkprojects.cmmsandroidapp.data.local.dto.TicketCustomerName
import com.gkprojects.cmmsandroidapp.data.local.dto.WorkOrdersList
import com.gkprojects.cmmsandroidapp.data.local.entity.CheckForms
import com.gkprojects.cmmsandroidapp.data.local.entity.ContractEquipments
import com.gkprojects.cmmsandroidapp.data.local.entity.Contracts
import com.gkprojects.cmmsandroidapp.data.local.entity.Customer
import com.gkprojects.cmmsandroidapp.data.local.entity.Departments
import com.gkprojects.cmmsandroidapp.data.local.entity.Equipments
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportCheckForm
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReportEquipment
import com.gkprojects.cmmsandroidapp.data.local.entity.FieldReports
import com.gkprojects.cmmsandroidapp.data.local.entity.Maintenances
import com.gkprojects.cmmsandroidapp.data.local.entity.Tickets
import com.gkprojects.cmmsandroidapp.data.local.entity.Users


@Dao
interface CustomerDao {

    @Query("Select * from Customer")
    fun getAllCustomer(): LiveData<List<Customer>>

    @Query("SELECT * FROM Customer Where CustomerID = :id")
    fun getCustomerByID(id :String):LiveData<Customer>
    @Query("Select Equipments.EquipmentID, Equipments.SerialNumber, Equipments.Model ,Equipments.InstallationDate " +
            "From Equipments " +
            "where Equipments.CustomerID = :id")
    fun getDashboardEquipmentsByID(id:String):LiveData<List<DashboardCustomerEquipmentDataClass>>

    @Query("Select Contracts.ContractID,Contracts.Title , " +
            "Contracts.ContractStatus, Contracts.DateEnd ,Contracts.ContractType " +
            "From Contracts Where Contracts.CustomerID = :id ")
    fun getDashboardContractsByID(id :String):LiveData<List<DashboardCustomerContractsDataClass>>

    @Query("Select Tickets.TicketID , Tickets.Title, Tickets.Urgency , Tickets.DateStart , Tickets.DateEnd " +
            "From Tickets Where Tickets.CustomerID = :id ")
    fun getDashboardTechnicalCaseByID(id :String):LiveData<List<DashboardCustomerTechnicalCasesDataClass>>

    @Insert
    suspend fun addCustomer(customer: Customer)

    @Update
    suspend fun updateCustomer(customer: Customer)
    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("SELECT * FROM Customer")
    suspend fun getAllCustomerList(): List<Customer> // <--- For sync

    @Query("SELECT * FROM Customer WHERE CustomerID = :id LIMIT 1")
    suspend fun getCustomerByIDNow(id: String): Customer? // <--- For sync


}
@Dao
interface ContractsDao {

    @Query("Select * from Contracts")
    suspend fun getAllContracts(): List<Contracts>

    @Query("Select * from Contracts where Contracts.ContractID= :id")
    suspend fun getContractsById(id:String): Contracts

    @Query ("Select CustomerID,Name as CustomerName from Customer")
    suspend fun getCustomerID(): List<CustomerSelect>

    @Query("SELECT Contracts.CustomerID, Customer.Name AS CustomerName, Contracts.ContractID, " +
            "Contracts.Title, Contracts.DateStart, Contracts.DateEnd, Contracts.Value, " +
            "Contracts.Notes, Contracts.Description, Contracts.ContractType, " +
            "Contracts.ContractStatus, Contracts.ContactName " +
            "FROM Contracts LEFT JOIN Customer ON Contracts.CustomerID = Customer.CustomerID ")
    suspend fun getContractsCustomerNames(): List<ContractsCustomerName>


    @Insert
    suspend fun addContracts(contracts: Contracts): Long

    @Update
    suspend fun updateContracts(contracts: Contracts) :Int
    @Delete
    suspend fun deleteContracts(contracts: Contracts) :Int

    @Query("SELECT * FROM Contracts")
    suspend fun getAllContractsList(): List<Contracts> // <--- For sync

    @Query("SELECT * FROM Contracts WHERE ContractID = :id LIMIT 1")
    suspend fun getContractsByIDNow(id: String): Contracts? // <--- For sync

}
@Dao
interface ContractEquipmentsDao {

    @Query("Select * from ContractEquipments")
    suspend fun getAllContractEquipment(): List<ContractEquipments>

    @Query ("Select * from ContractEquipments Where ContractEquipments.ContractID = :id")
    suspend fun getContractEquipmentByID(id :String):List<ContractEquipments>
    @Query ("Select * from ContractEquipments Where ContractEquipments.ContractEquipmentID = :id")
    suspend fun getContractEquipmentByEquipmentID(id :String): ContractEquipments

    @Query("SELECT COUNT(*) FROM ContractEquipments WHERE ContractID = :contractID AND EquipmentID = :equipmentID")
    suspend fun count(contractID: String, equipmentID: String): Int

    @Insert
    suspend fun addContractEquipments(contractEquipments: ContractEquipments) :Long

    @Query("SELECT ContractEquipments.ContractID, " +
            "Equipments.EquipmentID as equipmentID, Equipments.SerialNumber as serialNumber, Equipments.Model as model, " +
            "ContractEquipments.ContractEquipmentID, " +
            "ContractEquipments.Value " +
            ", ContractEquipments.Visits " +
            ", ContractEquipments.LastModified ," +
            " ContractEquipments.DateCreated, " +
            "ContractEquipments.Version " +
            "FROM ContractEquipments " +
            "LEFT JOIN Equipments ON ContractEquipments.EquipmentID = Equipments.EquipmentID " +
            "WHERE ContractEquipments.ContractID = :id ")
    suspend fun getDetailedContractByID(id: String):List<DetailedContract>
    @Update
    suspend fun updateContractEquipments(contractEquipments: ContractEquipments) :Int
    @Delete
    suspend fun deleteContractEquipments(contractEquipments: ContractEquipments) :Int

    @Query("SELECT * FROM ContractEquipments")
    suspend fun getAllContractEquipmentsList(): List<ContractEquipments> // <--- For sync

    @Query("SELECT * FROM ContractEquipments WHERE ContractEquipmentID = :id LIMIT 1")
    suspend fun getContractEquipmentByIDNow(id: String): ContractEquipments? // <--- For sync

}
@Dao
interface DepartmentsDao {

    @Query("Select * from Departments")
    fun getAllDepartments(): LiveData<List<Departments>>
    @Query ("Select CustomerID,Name as CustomerName from Customer")
    fun getCustomerID():LiveData<List<CustomerSelect>>
    @Insert
    fun addDepartments(departments: Departments)

    @Update
    fun updateDepartments(departments: Departments)
    @Delete
    fun deleteDepartments(departments: Departments)

    @Query("SELECT * FROM Departments")
    suspend fun getAllDepartmentsList(): List<Departments> // <--- For sync

    @Query("SELECT * FROM Departments WHERE DeparmentID = :id LIMIT 1")
    suspend fun getDepartmentsByIDNow(id: String): Departments? // <--- For sync


}
@Dao
interface EquipmentsDao{
    @Query("Select * from Equipments")
    fun getAllEquipments(): LiveData<List<Equipments>>
    @Query("Select * from Equipments where CustomerID = :customerId")
    suspend fun getAllDataEquipmentsByCustomerID(customerId : String): List<Equipments>


    @Query
    ("Select CustomerID,Name as CustomerName from Customer")
    suspend fun getCustomerID():List<CustomerSelect>
    @Query
    ("Select Equipments.CustomerID,Customer.Name AS CustomerName,Equipments.EquipmentID,Equipments.Name,Equipments.SerialNumber,Equipments.EquipmentStatus," +
     "Equipments.Model,Equipments.Manufacturer,Equipments.InstallationDate,Equipments.EquipmentCategory,Equipments.EquipmentVersion,Equipments.Warranty,Equipments.Description "+
     "From Equipments LEFT JOIN Customer ON Equipments.CustomerID = Customer.CustomerID"
    )
     suspend fun getCustomerName():List<EquipmentSelectCustomerName>

    @Query(" Select EquipmentID,SerialNumber,Model,CustomerID " +
            " From Equipments " +
            " Where CustomerID= :Customerid ")
    suspend fun selectEquipmentByCustomerID(Customerid : String) : List<EquipmentListInCases>

    @Query("Select * FROM Equipments WHERE EquipmentID= :id")
     suspend fun SelectRecordById(id :String) : Equipments

     @Query("Select * from Tickets Where EquipmentID= :id")
     suspend fun getTicketsByEquipmentId(id : String) : List<Tickets>

    @Insert
    suspend fun addEquipments(equipments: Equipments)
    @Update
    suspend fun updateEquipments(equipments: Equipments)
    @Delete
    suspend fun delete(equipments: Equipments)
    @Query("SELECT * FROM Equipments")
    suspend fun getAllEquipmentsList(): List<Equipments> // <--- For sync

    @Query("SELECT * FROM Equipments WHERE EquipmentID = :id LIMIT 1")
    suspend fun getEquipmentsByIDNow(id: String): Equipments? // <--- For sync

}
@Dao
interface FieldReportEquipmentDao {

    @Query("Select * from FieldReportEquipment")
    fun getAllFieldReportEquipment(): LiveData<List<FieldReportEquipment>>

    @Query("Select * from FieldReportEquipment where FieldReportEquipmentID= :id ")
    fun getFieldReportEquipmentByID(id :String):LiveData<FieldReportEquipment>

    @Query (" Select Equipments.EquipmentID as EquipmentID, " +
            " Equipments.Model as Model, " +
            " Equipments.SerialNumber as SerialNumber, " +
            " FieldReportEquipment.CompletedStatus as CompletedStatus, " +
            " FieldReportEquipment.FieldReportEquipmentID as idFieldReportEquipment " +
            " From FieldReportEquipment " +
            " LEFT JOIN Equipments " +
            " Where FieldReportEquipment.EquipmentID=Equipments.EquipmentID AND " +
            " FieldReportEquipment.FieldReportID = :id ")

    fun getFieldReportEquipmentByFieldReportID(id :String ):LiveData<List<CustomDisplayDatFieldReportEquipments>>

    @Query("UPDATE FieldReportEquipment SET CompletedStatus = :value WHERE FieldReportEquipmentID = :id")
    suspend fun updateCompletedStatus(value : Int, id :String)

    @Insert
    suspend fun addFieldReportEquipment(fieldReportEquipment: FieldReportEquipment)

    @Update
    suspend fun updateFieldReportEquipment(fieldReportEquipment: FieldReportEquipment)
    @Delete
    suspend fun deleteFieldReportEquipment(fieldReportEquipment: FieldReportEquipment)

    @Query("SELECT * FROM FieldReportEquipment")
    suspend fun getAllFieldReportEquipmentList(): List<FieldReportEquipment> // <--- For sync

    @Query("SELECT * FROM FieldReportEquipment WHERE FieldReportEquipmentID = :id LIMIT 1")
    suspend fun getFieldReportEquipmentByIDNow(id: String): FieldReportEquipment? // <--- For sync

    @Query("Select * from FieldReportEquipment where FieldReportID= :id")
    fun getAllFieldReportEquipmentByReportID(id: String): LiveData<List<FieldReportEquipment>>

}

@Dao
interface FieldReportsDao {

    @Query("Select * from FieldReports")
    fun getAllFieldReports(): LiveData<List<FieldReports>>

    @Query("Select * from FieldReports Where FieldReportID= :id")
    fun getReportsByID(id :String) : LiveData<FieldReports>

    @Query("SELECT FieldReports.FieldReportID AS workOrderID " +
            ", FieldReports.ReportNumber AS reportNumber " +
            ",FieldReports.StartDate AS dateOpened " +
            ",FieldReports.EndDate AS dateClosed " +
            ",FieldReports.Title AS title " +
            ",Customer.Name AS customerName " +
            ",FieldReports.ReportStatus AS status " +
            " FROM FieldReports LEFT JOIN Customer ON FieldReports.CustomerID = Customer.CustomerID")
    fun getCustomerName():LiveData<List<WorkOrdersList>>

    @Query ("Select CustomerID,Name as CustomerName from Customer")
    fun getCustomerID():LiveData<List<CustomerSelect>>
    @Insert
    suspend fun addFieldReports(fieldReports: FieldReports)

    @Update
    suspend fun updateFieldReports(fieldReports: FieldReports)
    @Delete
    suspend fun deleteFieldReports(fieldReports: FieldReports)

    @Query("Select Customer.Name as customerName, " +
            "FieldReports.ClientName as signeName, " +
            "FieldReports.Department as departmentWorkOrder, " +
            "FieldReports.Title as reportTitle, " +
            "FieldReports.Description as detailedReport , " +
            "FieldReports.StartDate as startDate, " +
            "FieldReports.EndDate as endDate, " +
            "FieldReports.ReportNumber as reportNumber, " +
            "Users.Name as usersName " +
            "from Customer " +
            "Left join FieldReports " +
            "Left join Users " +
            "Where Customer.CustomerID = FieldReports.CustomerID and " +
            "FieldReports.UserID = Users.UserID and FieldReports.FieldReportID =:id")
    fun printDetails(id :String):LiveData<CustomWorkOrderPDFDATA>

    @Query("Select FieldReportEquipment.EquipmentID as EquipmentId , " +
            "FieldReportEquipment.FieldReportEquipmentID as FieldEquipmentId , " +
            "FieldReportEquipment.FieldReportID as ReportId , " +
            "FieldReportCheckForm.FieldReportCheckFormID as FieldCheckListId, " +
            "FieldReportCheckForm.Description as FieldCheckListDescription, " +
            "FieldReportCheckForm.ValueMeasured as FieldCheckListMeasure , " +
            "FieldReportCheckForm.ValueExpected as FieldCheckListLimit , " +
            "FieldReportCheckForm.Result as FieldCheckListResult , " +
            "Equipments.Manufacturer as EquipmentManufacturer, " +
            "Equipments.Model as EquipmentModel, " +
            "Equipments.SerialNumber as EquipmentSerialNumber, " +
            "Equipments.EquipmentCategory as EquipmentCategory " +
            "from FieldReportEquipment " +
            "left join Equipments on Equipments.EquipmentID = FieldReportEquipment.EquipmentID " +
            "left join FieldReportCheckForm on FieldReportCheckForm.FieldReportEquipmentID = FieldReportEquipment.FieldReportEquipmentID " +
            "where  FieldReportEquipment.FieldReportID = :reportId "
    )
    fun printEquipmentWithCheckList(reportId :String):LiveData<List<CustomCheckListWithEquipmentData>>

    @Query("select Inventory.Description as description, " +
            "Inventory.Title as title, " +
            "FieldReportInventory.FieldReportID as fieldReportID, " +
            "FieldReportInventory.FieldReportInventoryID as fieldReportInventoryID, " +
            "FieldReportInventory.InventoryID as inventoryID " +
            "from FieldReportInventory " +
            "left join Inventory on Inventory.InventoryID =FieldReportInventory.InventoryID " +
            "where FieldReportInventory.FieldReportID = :id ")
    fun printInventoryDataByReportID(id :String):LiveData<List<FieldReportInventoryCustomData>>
    @Query("Select " +
            "Tools.Title as toolsTitle , " +
            " Tools.SerialNumber as toolsSerialNumber , " +
            "Tools.CalibrationDate as toolsCalDate, " +
            "FieldReportTools.ToolsID as toolsID, " +
            "FieldReportTools.FieldReportID as fieldReportID, " +
            "FieldReportTools.FieldReportToolsID as fieldReportToolsID " +
            "from FieldReportTools " +
            "left join Tools on Tools.ToolsID =FieldReportTools.ToolsID " +
            "where  FieldReportTools.FieldReportID= :id ")
    fun printToolsByReportID(id :String) :LiveData<List<FieldReportToolsCustomData>>

    @Query("SELECT * FROM FieldReports")
    suspend fun getAllFieldReportsList(): List<FieldReports> // <--- For sync

    @Query("SELECT * FROM FieldReports WHERE FieldReportID = :id LIMIT 1")
    suspend fun getFieldReportsByIDNow(id: String): FieldReports? // <--- For sync

    @Query("UPDATE Users SET LastReportNumber = :number WHERE UserID = :id")
    suspend fun increaseLastReportNumber(number : Int ,id : String)

    @Query("SELECT * FROM Users WHERE UserID = :id ")
    suspend fun getUserByID(id:String): Users
}


@Dao
interface MaintenancesDao {

    @Query("Select * from Maintenances")
    fun getAllMaintenances(): LiveData<List<Maintenances>>

    @Insert
    fun addMaintenances(maintenances: Maintenances)

    @Update
    fun updateMaintenances(maintenances: Maintenances)
    @Delete
    fun deleteMaintenances(maintenances: Maintenances)

    @Query("SELECT * FROM Maintenances")
    suspend fun getAllMaintenancesList(): List<Maintenances> // <--- For sync

    @Query("SELECT * FROM Maintenances WHERE MaintenanceID = :id LIMIT 1")
    suspend fun getMaintenancesByIDNow(id: String): Maintenances? // <--- For sync

}
@Dao
interface TicketsDao {

    @Query("Select * from Tickets")
    fun getAllTickets(): LiveData<List<Tickets>>

    @Query("Select * from Tickets where Tickets.TicketID= :id")
    fun getTicketsById(id: String): LiveData<Tickets>

    @Query ("Select CustomerID,Name as CustomerName from Customer")
    fun getCustomerID():LiveData<List<CustomerSelect>>

    @Query("Select Tickets.TicketID,Tickets.Title,Tickets.Active,Tickets.DateStart,Tickets.Urgency,Customer.Name AS CustomerName,Tickets.UserID, Equipments.SerialNumber AS SerialNumber , " +
            "Tickets.CustomerID,Tickets.EquipmentID "+
            "From Tickets " +
            "Left JOIN Customer ON Tickets.CustomerID = Customer.CustomerID " +
            "Left JOIN Equipments ON Tickets.EquipmentID = Equipments.EquipmentID "

    )
    fun getCustomerName():LiveData<List<TicketCustomerName>>
    @Query("Select Tickets.TicketID,Tickets.Title,Tickets.Active,Tickets.DateStart,Tickets.Urgency,Customer.Name AS CustomerName,Tickets.UserID, Equipments.SerialNumber AS SerialNumber , " +
            "Tickets.CustomerID,Tickets.EquipmentID ,Equipments.Model AS Model, Equipments.Manufacturer AS Manufacturer "+
            "From Tickets " +
            "Left JOIN Customer ON Tickets.CustomerID = Customer.CustomerID " +
            "Left JOIN Equipments ON Tickets.EquipmentID = Equipments.EquipmentID "

    )
    fun getTicketInformationCalendar():LiveData<List<TicketCalendar>>



    @Query
        ("Select Tickets.Urgency , Customer.Name as CustomerName ,Tickets.DateStart ," +
            "Tickets.DateEnd,Tickets.Title,Tickets.Description,"+
            "Tickets.TicketNumber,"+
            "Tickets.UserID ,Tickets.EquipmentID , Tickets.TicketID ,Tickets.CustomerID "+
            "From Tickets LEFT JOIN Customer ON Tickets.CustomerID= Customer.CustomerID " )
    fun getDateForOverview():LiveData<List<OverviewMainData>>
    @Insert
    fun addTickets(tickets: Tickets)

    @Update
    fun updateTickets(tickets: Tickets)
    @Delete
    fun deleteTickets(tickets: Tickets)

    @Query("SELECT * FROM Tickets")
    suspend fun getAllTicketsList(): List<Tickets> // <--- For sync

    @Query("SELECT * FROM Tickets WHERE TicketID = :id LIMIT 1")
    suspend fun getTicketsByIDNow(id: String): Tickets? // <--- For sync

}
@Dao
interface UsersDao {

    @Query("Select * from Users")
    fun getAllUsers(): LiveData<List<Users>>

    @Query("SELECT * FROM Users LIMIT 1")
    fun getFirstUser(): LiveData<Users>

    @Query("UPDATE Users SET LastReportNumber = :number WHERE UserID = :id")
    fun increaseLastReportNumber(number : Int ,id : String)

    @Query("SELECT * FROM Users WHERE UserID = :id ")
    suspend fun getUserByID(id:String): Users

    @Insert
    fun addUsers(users: Users)

    @Update
    fun updateUsers(users: Users)
    @Delete
    fun deleteUsers(users: Users)

    @Query("SELECT * FROM Users")
    suspend fun getAllUsersList(): List<Users> // <--- For sync

    @Query("SELECT * FROM Users WHERE UserID = :id LIMIT 1")
    suspend fun getUsersByIDNow(id: String): Users? // <--- For sync

}


@Dao
interface FieldReportCheckFormsDao{
    @Insert
    suspend fun insertFieldReportCheckForms(fieldReportCheckForm: FieldReportCheckForm)

    @Update
    fun updateFieldReportCheckForms(fieldReportCheckForm: FieldReportCheckForm)

    @Delete
    fun deleteFieldReportCheckForms(fieldReportCheckForm: FieldReportCheckForm)

    @Query(" Select * From FieldReportCheckFOrm where FieldReportEquipmentID= :id ")
    fun selectFieldReportCheckFormsByFieldReportEquipmentID(id :String) : LiveData<List<FieldReportCheckForm>>

    @Query("SELECT * FROM FieldReportCheckForm")
    suspend fun getAllFieldReportCheckFormList(): List<FieldReportCheckForm> // <--- For sync

    @Query("SELECT * FROM FieldReportCheckForm WHERE FieldReportCheckFormID = :id LIMIT 1")
    suspend fun getFieldReportCheckFormByIDNow(id: String): FieldReportCheckForm? // <--- For sync

}
@Dao
interface CheckFormsDao{
    @Insert
    fun addCheckFormsFields(checkForms: CheckForms)

    @Update
    fun updateCheckFormsFields(checkForms: CheckForms)

    @Delete
    fun deleteCheckFormsFields(checkForms : CheckForms)

    @Query ("Select * from CheckForms where MaintenancesID = :id")
    fun getCheckFormsFieldsByMaintenanceID(id :String) :LiveData<List<CheckForms>>
    @Query("SELECT * FROM CheckForms")
    suspend fun getAllCheckFormsList(): List<CheckForms> // <--- For sync

    @Query("SELECT * FROM CheckForms WHERE CheckFormID = :id LIMIT 1")
    suspend fun getCheckFormsByIDNow(id: String): CheckForms? // <--- For sync

}
