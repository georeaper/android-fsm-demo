package com.gkprojects.cmmsandroidapp.data.local.dto

import android.annotation.SuppressLint
import com.gkprojects.cmmsandroidapp.data.local.serialization.BaseReminderSettings
import kotlinx.serialization.Serializable

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class ReminderSettingsGeneric (
    override val reminders: String,  // "3 years before,2 months before,1 hour before"
    override val insert: Boolean = false,
    override val update: Boolean = false,
    override val delete: Boolean = false
): BaseReminderSettings