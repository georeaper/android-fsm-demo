package com.gkprojects.cmmsandroidapp.presentation.ui.UserCreationFragment

import com.gkprojects.cmmsandroidapp.data.local.entity.Users
import java.util.UUID

object UsersSingletons {

var predefinedUser: Users = Users(
    UUID.randomUUID().toString(),
    null,
    "Joe Doe",
    null,
    null,
    null,
    null,
    "JD",
    "JDTC",
    0,
    0,
    null,
    null,
    null
)
    private val observers = mutableListOf<() -> Unit>()

    fun addObserver(observer: () -> Unit) {
        observers.add(observer)
    }

    fun removeObserver(observer: () -> Unit) {
        observers.remove(observer)
    }

    fun notifyObservers() {
        for (observer in observers) {
            observer()
        }
    }

    fun updateUser(user: Users) {
        predefinedUser = user
        notifyObservers()
    }
    
}