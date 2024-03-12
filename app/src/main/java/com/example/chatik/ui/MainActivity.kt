package com.example.chatik.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.chatik.model.Chat
import com.example.chatik.model.CurrentUser

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
  var currentUser by remember { mutableStateOf(CurrentUser(id = -1, username = "", password = "")) }
  var currentChat by remember { mutableStateOf(Chat(id = -1, username = "", messages = null)) }

  val onLoginSuccess: (CurrentUser) -> Unit = { userAuth ->
    currentUser = userAuth
    currentScreen = Screen.Chats
  }

  val onChatClicked: (Chat) -> Unit = { chat ->
    currentChat = chat
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
      currentUser = currentUser,
      onChatClicked = onChatClicked
    )

    Screen.Chat -> ChatScreen(
      chat = currentChat,
      currentUser = currentUser,
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
