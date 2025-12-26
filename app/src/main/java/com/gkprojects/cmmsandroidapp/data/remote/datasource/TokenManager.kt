package com.gkprojects.cmmsandroidapp.data.remote.datasource

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object TokenManager {
    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        sharedPreferences = EncryptedSharedPreferences.create(
            "secure_prefs",
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    private fun getPrefs(): SharedPreferences {
        return sharedPreferences ?: throw IllegalStateException("TokenManager not initialized. Call TokenManager.init(context) first.")
    }

    fun saveToken(token: String) {
        getPrefs().edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String? {
        return getPrefs().getString("jwt_token", null)
    }

    fun saveUsername(username: String) {
        getPrefs().edit().putString("username", username).apply()
    }

    fun getUsername(): String? {
        return getPrefs().getString("username", null)
    }
}


