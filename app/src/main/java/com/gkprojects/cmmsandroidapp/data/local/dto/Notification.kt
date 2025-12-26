package com.gkprojects.cmmsandroidapp.data.local.dto

data class Notification(
    var id : Int,
    var timeStamp : String,
    var type : String,
    var function :String,
    var title :String,
    var description: String,
    var seen : Boolean
)
