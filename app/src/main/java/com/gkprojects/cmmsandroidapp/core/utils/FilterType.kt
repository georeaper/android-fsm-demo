package com.gkprojects.cmmsandroidapp.core.utils

// FilterType.kt
sealed class FilterType(val title: String) {
    data class ListFilter(val name: String, val options: List<String>) : FilterType(name)
    data class StatusFilter(val name: String) : FilterType(name)
    data class DateRangeFilter(val name: String) : FilterType(name)
}