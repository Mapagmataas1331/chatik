package com.example.chatik.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.chatik.api.model.LoginRequest
import com.example.chatik.api.request.AuthService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
  authService: AuthService,
  navigateToRegistration: () -> Unit,
  navigateToChat: () -> Unit
) {
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var isLoading by remember { mutableStateOf(false) }
  var error by remember { mutableStateOf("") }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    TextField(
      value = username,
      onValueChange = { username = it },
      modifier = Modifier.fillMaxWidth(),
      label = { Text("Username") }
    )
    TextField(
      value = password,
      onValueChange = { password = it },
      modifier = Modifier.fillMaxWidth(),
      label = { Text("Password") }
    )
    if (isLoading) {
      CircularProgressIndicator()
    } else {
      Button(
        onClick = {
          isLoading = true
          GlobalScope.launch(Dispatchers.Main) {
            try {
              val response = authService.loginUser(LoginRequest(username, password))
              if (response.id != 0) {
                navigateToChat()
              } else {
                error = "Invalid username or password"
              }
            } catch (e: Exception) {
              error = "An error occurred"
            } finally {
              isLoading = false
            }
          }
        },
        modifier = Modifier.align(Alignment.CenterHorizontally)
      ) {
        Text("Login")
      }
      Button(
        onClick = navigateToRegistration,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      ) {
        Text("Register")
      }
    }
    if (error.isNotBlank()) {
      Text(text = error, color = Color.Red)
    }
  }
}

