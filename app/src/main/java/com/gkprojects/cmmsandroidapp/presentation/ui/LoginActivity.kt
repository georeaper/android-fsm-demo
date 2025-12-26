package com.gkprojects.cmmsandroidapp.presentation.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gkprojects.cmmsandroidapp.data.remote.client.ApiClient

import com.gkprojects.cmmsandroidapp.data.remote.api.AuthLoginService
import com.gkprojects.cmmsandroidapp.data.remote.api.LoginRequest
import com.gkprojects.cmmsandroidapp.data.remote.dto.LoginResponse
import com.gkprojects.cmmsandroidapp.data.remote.datasource.TokenManager
import java.util.UUID
import com.gkprojects.cmmsandroidapp.presentation.ui.LoginPage as LoginPage1
import at.favre.lib.crypto.bcrypt.BCrypt
import com.gkprojects.cmmsandroidapp.MainActivity

class LoginActivity: AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var useRemoteDBState = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.init(this)
        sharedPreferences = getSharedPreferences("SettingsApp", Context.MODE_PRIVATE)
        initializeIdDevice()
        setContent {

           LoginScreen(this@LoginActivity,useRemoteDBState.value)

        }
    }
    private fun initializeIdDevice(){
        sharedPreferences = getSharedPreferences("SettingsApp", Context.MODE_PRIVATE)

        if (!sharedPreferences.contains("device_id")) {
            val editor = sharedPreferences.edit()
            val uniqueID = UUID.randomUUID().toString()
            editor.putString("device_id", uniqueID)
            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        sharedPreferences = getSharedPreferences("SettingsApp", Context.MODE_PRIVATE)
        useRemoteDBState.value = useRemoteDB()
    }
    private fun useRemoteDB(): Boolean {
        return sharedPreferences.getBoolean("useRemoteDB", false)
    }

}



@Composable
fun LoginScreen(context: Context, useRemoteDB: Boolean) {



    Box(modifier = Modifier.fillMaxSize()) {
        if (useRemoteDB) {
            LoginPage1({ username, password ->
                Log.d("ApiSentData", "$username $password")
                loginUser(username, password) { success, user ->
                    if (success && user != null) {
                        val intent = Intent(context, MainActivity::class.java)
                        intent.putExtra("username", user)
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "Login failed. Try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            }, Modifier.align(Alignment.Center))
        } else {
            Button(onClick = {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            }, modifier = Modifier.align(Alignment.Center)) {
                Text("Start Application")
            }
        }

        Button(
            onClick = {
                val intent = Intent(context, AppSettingsActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
        ) {
            Icon(Icons.Default.Settings, contentDescription = "Settings")
        }
    }


}


@Composable
fun LoginPage(onLogin: (String, String) -> Unit, modifier: Modifier = Modifier) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Login", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Column {
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                singleLine = true
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onLogin(username, password) }, Modifier.align(Alignment.End)) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}
fun loginUser(username: String, password: String, onResult: (Boolean, String?) -> Unit) {
    val authService = ApiClient().retrofit.create(AuthLoginService::class.java)
    val hashedPassword = hashPassword(password)
    val loginRequest = LoginRequest(username, password )

    authService.login(loginRequest).enqueue(object : retrofit2.Callback<LoginResponse> {
        override fun onResponse(call: retrofit2.Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
            if (response.isSuccessful) {
                val jwtResponse = response.body()
                jwtResponse?.let {
                    TokenManager.saveToken(it.token)       // Save token
                    TokenManager.saveUsername(it.username) // Save username
                    Log.d("loginTest", "Login successful! User: ${it.username}")
                    onResult(true, it.username) // Notify success
                    return
                }
            }
            Log.d("loginTest", "Login failed: ${response.errorBody()?.string()}")
            onResult(false, null) // Notify failure
        }

        override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
            Log.d("loginTest", "Network error: ${t.message}")
            onResult(false, null) // Notify failure
        }
    })
}
fun hashPassword(password: String): String {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray())
}