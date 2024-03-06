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
import com.example.chatik.api.model.RegistrationRequest
import com.example.chatik.api.model.Id
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RegistrationScreen(
  context: Context,
  onLoginClicked: () -> Unit
) {
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var firstName by remember { mutableStateOf("") }
  var lastName by remember { mutableStateOf("") }

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
    TextField(
      value = firstName,
      onValueChange = { firstName = it },
      label = { Text("First Name") }
    )
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
      value = lastName,
      onValueChange = { lastName = it },
      label = { Text("Last Name") }
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = {
      // Perform registration logic here
      registerUser(context, username, password, firstName, lastName) { success ->
        if (success) {
          Toast.makeText(
            context,
            "Registration successful.",
            Toast.LENGTH_SHORT
          ).show()
          onLoginClicked()
        } else {
          Toast.makeText(
            context,
            "Registration failed. Please try again.",
            Toast.LENGTH_SHORT
          ).show()
        }
      }
    }) {
      Text("Register")
    }
    Spacer(modifier = Modifier.height(16.dp))
    TextButton(onClick = onLoginClicked) {
      Text("Login")
    }
  }
}

private fun registerUser(context: Context, username: String, password: String, firstName: String, lastName: String, callback: (Boolean) -> Unit) {
  val authService = RetrofitClient.createAuthService()
  CoroutineScope(Dispatchers.IO).launch {
    try {
      val response: Id = authService.registerUser(RegistrationRequest(username, password, firstName, lastName))
      val success = response.id >= 0
      withContext(Dispatchers.Main) {
        if (success) {
          callback(true)
        } else {
          callback(false)
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      withContext(Dispatchers.Main) {
        callback(false)
        Toast.makeText(
          context,
          e.message,
          Toast.LENGTH_SHORT
        ).show()
      }
    }
  }
}
