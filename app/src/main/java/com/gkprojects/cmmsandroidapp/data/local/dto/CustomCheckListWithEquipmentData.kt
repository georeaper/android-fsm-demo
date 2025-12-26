package com.gkprojects.cmmsandroidapp.data.local.dto

data class CustomCheckListWithEquipmentData(
    var EquipmentId : String?,
    var FieldEquipmentId : String?,
    var ReportId :String?,
    var FieldCheckListId : String?,

    var FieldCheckListDescription :String? ,
    var FieldCheckListMeasure :String?,
    var FieldCheckListLimit :String? ,
    var FieldCheckListResult :String?,
    var EquipmentManufacturer :String?,
    var EquipmentModel :String? ,
    var EquipmentSerialNumber :String?,
    var EquipmentCategory :String?
)
