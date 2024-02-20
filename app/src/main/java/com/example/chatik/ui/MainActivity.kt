package com.example.chatik.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ChatikApp()
    }
  }
}

@Composable
fun ChatikApp() {
  var currentScreen by rememberSaveable { mutableStateOf(Screen.Login) }

  when (currentScreen) {
    Screen.Login -> LoginScreen(
      onLoginSuccess = {
        currentScreen = Screen.Chats
      },
      onRegisterClicked = {
        currentScreen = Screen.Registration
      }
    )
    Screen.Registration -> RegistrationScreen(
      onLoginClicked = {
        currentScreen = Screen.Login
      }
    )
    Screen.Chats -> ChatsScreen(
      onChatClicked = { friendUsername ->
        // Navigate to chat screen with friendUsername
      }
    )

    else -> {}
  }
}

enum class Screen {
  Login,
  Registration,
  Chats,
  Chat
}
