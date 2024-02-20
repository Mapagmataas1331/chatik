package com.example.chatik.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chatik.api.RetrofitClient
import com.example.chatik.api.model.LoginRequest
import com.example.chatik.api.model.Id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(
  context: Context,
  onLoginSuccess: (Int) -> Unit,
  onRegisterClicked: () -> Unit
) {
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    TextField(
      value = username,
      onValueChange = { username = it },
      label = { Text("Username") }
    )
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("Password") },
      visualTransformation = PasswordVisualTransformation(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = {
      // Perform login logic here
      loginUser(context, username, password) { userId: Int ->
        if (userId >= 0) {
          onLoginSuccess(userId)
        } else {
          Toast.makeText(
            context,
            "Login failed. Please try again.",
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }) {
      Text("Login")
    }
    Spacer(modifier = Modifier.height(16.dp))
    TextButton(onClick = onRegisterClicked) {
      Text("Register")
    }
  }
}

private fun loginUser(context: Context, username: String, password: String, callback: (Int) -> Unit) {
  val authService = RetrofitClient.createAuthService()
  CoroutineScope(Dispatchers.IO).launch {
    try {
      val response: Id = authService.loginUser(LoginRequest(username, password))
      val success = response.id >= 0
      withContext(Dispatchers.Main) {
        if (success) {
          callback(response.id)
        } else {
          callback(-1)
          Toast.makeText(
            context,
            "Login failed. Please try again.",
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      withContext(Dispatchers.Main) {
        callback(-1)
        Toast.makeText(
          context,
          "Login failed. Please try again.",
          Toast.LENGTH_SHORT
        ).show()
      }
    }
  }
}
