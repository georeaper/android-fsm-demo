package com.gkprojects.cmmsandroidapp.presentation.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class AppSettingsActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences("SettingsApp", Context.MODE_PRIVATE)

        setContent {
            Column(modifier = Modifier.padding(16.dp)) {
                var useRemoteDB by remember { mutableStateOf(sharedPreferences.getBoolean("useRemoteDB", false)) }
                var dbName by remember { mutableStateOf(sharedPreferences.getString("dbName", if (useRemoteDB) "RemoteDB250309a" else "LocalDB250309a") ?: "") }
                var remoteServerLink by remember { mutableStateOf(sharedPreferences.getString("remoteServerLink", "") ?: "") }
                var remoteDBName by remember { mutableStateOf(sharedPreferences.getString("remoteDBName", "") ?: "") }
                var remoteDBPassword by remember { mutableStateOf(sharedPreferences.getString("remoteDBPassword", "") ?: "") }

                Switch(
                    checked = useRemoteDB,
                    onCheckedChange = {
                        useRemoteDB = it
                        dbName = if (it) "RemoteDB250309a" else "LocalDB250309a"
                        sharedPreferences.edit().putBoolean("useRemoteDB", it).putString("dbName", dbName).apply()
                    },
                )
                Text(text = if (useRemoteDB) "Use remote DB" else "Use local DB")


                OutlinedTextField(
                    value = remoteServerLink,
                    onValueChange = {
                        remoteServerLink = it
                        sharedPreferences.edit().putString("remoteServerLink", it).apply()
                    },
                    label = { Text("Remote Server Link") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = useRemoteDB // Enable this field only when useRemoteDB is true
                )

                OutlinedTextField(
                    value = remoteDBName,
                    onValueChange = {
                        remoteDBName = it
                        sharedPreferences.edit().putString("remoteDBName", it).apply()
                    },
                    label = { Text("Remote DB Name") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = useRemoteDB // Enable this field only when useRemoteDB is true
                )

                OutlinedTextField(
                    value = remoteDBPassword,
                    onValueChange = {
                        remoteDBPassword = it
                        sharedPreferences.edit().putString("remoteDBPassword", it).apply()
                    },
                    label = { Text("Remote DB Password") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = useRemoteDB // Enable this field only when useRemoteDB is true
                )

                Button(
                    onClick = { /* Handle check connection click here */ },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text("Check Connection")
                }

                Button(
                    onClick = { /* Handle check local DB click here */ },
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Text("Check Local DB")
                }
            }
        }
    }



}