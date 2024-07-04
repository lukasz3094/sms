package com.example.sms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sms.ui.theme.SMSTheme
import com.example.sms.network.RetrofitClient
import com.example.sms.models.UserModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SMSTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavigationComponent()
                }
            }
        }
    }
}

@Composable
fun NavigationComponent() {
    var showLogin by remember { mutableStateOf(true) }

    if (showLogin) {
        LoginScreen { showLogin = false }
    } else {
        RegisterScreen { showLogin = true }
    }
}

@Composable
fun LoginScreen(onRegisterClick: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = login, onValueChange = { login = it }, label = { Text("Login") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val user = UserModel(0, login, password, null, null, false)
            RetrofitClient.instance.loginUser(user).enqueue(object : Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) =
                    if (response.isSuccessful) {
                        message = "Login successful"
                    } else {
                        message = "Login failed"
                    }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    message = "Login failed: ${t.message}"
                }
            })
        }) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRegisterClick) {
            Text("Register")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(message)
    }
}

@Composable
fun RegisterScreen(onLoginClick: () -> Unit) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = login, onValueChange = { login = it }, label = { Text("Login") })
        Spacer(modifier = Modifier.height(8.dp))
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") }, visualTransformation = PasswordVisualTransformation())
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val user = UserModel(0, login, password, null, null, false)
            RetrofitClient.instance.registerUser(user).enqueue(object : Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    if (response.isSuccessful) {
                        message = "Registration successful"
                    } else {
                        message = "Registration failed"
                    }
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    message = "Registration failed: ${t.message}"
                }
            })
        }) {
            Text("Register")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onLoginClick) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(message)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SMSTheme {
        NavigationComponent()
    }
}
