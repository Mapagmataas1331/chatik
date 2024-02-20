package com.example.chatik.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.chatik.api.RetrofitClient

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      MaterialTheme {
        Surface {
          MainScreen(
            authService = RetrofitClient.createAuthService(),
            messageService = RetrofitClient.createMessageService(),
            userService = RetrofitClient.createUserService()
          )
        }
      }
    }
  }
}
