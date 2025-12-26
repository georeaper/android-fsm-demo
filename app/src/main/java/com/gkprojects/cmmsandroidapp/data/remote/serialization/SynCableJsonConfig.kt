package com.gkprojects.cmmsandroidapp.data.remote.serialization

import com.gkprojects.cmmsandroidapp.data.remote.dto.CategoryAsset
import com.gkprojects.cmmsandroidapp.data.remote.dto.CheckForms
import com.gkprojects.cmmsandroidapp.data.remote.dto.ContractEquipments
import com.gkprojects.cmmsandroidapp.data.remote.dto.Contracts
import com.gkprojects.cmmsandroidapp.data.remote.dto.Customers
import com.gkprojects.cmmsandroidapp.data.remote.dto.Departments
import com.gkprojects.cmmsandroidapp.data.remote.dto.Equipments
import com.gkprojects.cmmsandroidapp.data.remote.dto.FieldReportCheckForm
import com.gkprojects.cmmsandroidapp.data.remote.dto.FieldReportEquipment
import com.gkprojects.cmmsandroidapp.data.remote.dto.FieldReportInventory
import com.gkprojects.cmmsandroidapp.data.remote.dto.FieldReportTools
import com.gkprojects.cmmsandroidapp.data.remote.dto.FieldReports
import com.gkprojects.cmmsandroidapp.data.remote.dto.Inventory
import com.gkprojects.cmmsandroidapp.data.remote.dto.Maintenances
import com.gkprojects.cmmsandroidapp.data.remote.dto.Manufacturer
import com.gkprojects.cmmsandroidapp.data.remote.dto.ModelAsset
import com.gkprojects.cmmsandroidapp.data.remote.dto.Settings
import com.gkprojects.cmmsandroidapp.data.remote.dto.SynCable
import com.gkprojects.cmmsandroidapp.data.remote.dto.TicketHistory
import com.gkprojects.cmmsandroidapp.data.remote.dto.Tickets
import com.gkprojects.cmmsandroidapp.data.remote.dto.Tools
import com.gkprojects.cmmsandroidapp.data.remote.dto.Users
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val synCableModule = SerializersModule {
    polymorphic(SynCable::class) {
        subclass(Customers::class)
        subclass(Users::class)
        subclass(Equipments::class)
        subclass(CategoryAsset::class)
        subclass(CheckForms::class)
        subclass(ContractEquipments::class)
        subclass(Contracts::class)
        subclass(Departments::class)
        subclass(FieldReports::class)
        subclass(FieldReportTools::class)
        subclass(FieldReportInventory::class)
        subclass(FieldReportCheckForm::class)
        subclass(FieldReportEquipment::class)
        subclass(Inventory::class)
        subclass(Maintenances::class)
        subclass(Manufacturer::class)
        subclass(ModelAsset::class)
        subclass(Settings::class)
        subclass(TicketHistory::class)
        subclass(Tickets::class)
        subclass(Tools::class)
        // Later you can add other types here too
    }
}

val json = Json {
    serializersModule = synCableModule
    //classDiscriminator = "type" // will include "type": "Customer"/"Users"/...
    prettyPrint = true
    encodeDefaults = true // Optional: includes default values
    ignoreUnknownKeys = true
}