package com.gkprojects.cmmsandroidapp.data.local.dto

data class HomeRecyclerViewData(
    var topLeft: String,
    var topRight: String,
    var MainLeft :String,
    var MaintRight :String
)

data class OverviewMainData(
    val Urgency :String? ,
    val CustomerName :String? ,
    val DateStart :String?,
    val DateEnd :String? ,
    val Title :String?,
    val Description :String?,
    val TicketNumber :String?,
    val UserID :String?,
    val EquipmentID :String?,
    val TicketID :String?,
    val CustomerID :String?
)
