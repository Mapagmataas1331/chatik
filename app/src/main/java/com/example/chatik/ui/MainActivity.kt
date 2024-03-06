package com.example.chatik.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.chatik.api.model.Auth

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
  var currentUserAuth by remember { mutableStateOf(Auth(id = -1, username = "", password = "")) }
  var currentFriendUsername by remember { mutableStateOf("") }

  val onLoginSuccess: (Auth) -> Unit = { userAuth ->
    currentUserAuth = userAuth
    currentScreen = Screen.Chats
  }

  val onChatClicked: (String) -> Unit = { friendUsername ->
    currentFriendUsername = friendUsername
    currentScreen = Screen.Chat
  }

  when (currentScreen) {
    Screen.Login -> LoginScreen(
      context = context,
      onLoginSuccess = onLoginSuccess
    ) {
      currentScreen = Screen.Registration
    }

    Screen.Registration -> RegistrationScreen(
      context = context,
      onLoginClicked = {
        currentScreen = Screen.Login
      }
    )

    Screen.Chats -> ChatsScreen(
      context = context,
      userAuth = currentUserAuth,
      onChatClicked = onChatClicked
    )

    Screen.Chat -> ChatScreen(
      friendUsername = currentFriendUsername,
      userAuth = currentUserAuth,
      onBackClicked = {
        currentScreen = Screen.Chats
      }
    )

  }
}

enum class Screen {
  Login,
  Registration,
  Chats,
  Chat
}
