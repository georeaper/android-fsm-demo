package com.gkprojects.cmmsandroidapp.core.utils

sealed class ReminderResult {
    data class Interval(val value: Int, val unit: TimeUnit) : ReminderResult()
}