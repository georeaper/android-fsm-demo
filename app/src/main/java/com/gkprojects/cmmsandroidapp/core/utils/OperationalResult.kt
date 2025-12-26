package com.gkprojects.cmmsandroidapp.core.utils

sealed class OperationResult<out T> {
    data class Success<out T>(val data: T? = null) : OperationResult<T>()
    data class Error(val message: String) : OperationResult<Nothing>()
}
