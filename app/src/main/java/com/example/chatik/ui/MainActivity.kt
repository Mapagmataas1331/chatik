package com.example.chatik.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      ChatikApp(this)
    }
  }
}

@Composable
fun ChatikApp(context: Context) {
  var currentScreen by rememberSaveable { mutableStateOf(Screen.Login) }

  when (currentScreen) {
    Screen.Login -> LoginScreen(
      context = context,
      onLoginSuccess = {
        currentScreen = Screen.Chats
      },
      onRegisterClicked = {
        currentScreen = Screen.Registration
      }
    )
    Screen.Registration -> RegistrationScreen(
      context = context,
      onLoginClicked = {
        currentScreen = Screen.Login
      }
    )
    Screen.Chats -> ChatsScreen(
      context = context,
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
