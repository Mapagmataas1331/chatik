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
  var currentUserID by remember { mutableIntStateOf(-1) }
  var currentFriendUsername by remember { mutableStateOf("") }

  val onLoginSuccess: (Int) -> Unit = { userId ->
    currentUserID = userId
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
      currentUserID = currentUserID,
      onChatClicked = onChatClicked
    )

    Screen.Chat -> ChatScreen(
      friendUsername = currentFriendUsername,
      currentUserID = currentUserID,
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
