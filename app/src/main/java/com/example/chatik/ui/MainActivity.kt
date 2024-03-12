package com.example.chatik.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.chatik.model.Chat
import com.example.chatik.model.CurrentUser

/**
 * Основная активность приложения.
 */
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Устанавливаем контент с использованием Compose
    setContent {
      ChatikApp(this)
    }
  }
}

/**
 * Главный компонент приложения.
 *
 * @param context Контекст приложения.
 */
@Composable
fun ChatikApp(context: Context) {
  // Состояния для текущего экрана, текущего пользователя и текущего чата
  var currentScreen by rememberSaveable { mutableStateOf(Screen.Login) }
  var currentUser by remember { mutableStateOf(CurrentUser(id = -1, username = "", password = "")) }
  var currentChat by remember { mutableStateOf(Chat(id = -1, username = "", messages = null)) }

  // Callback-функция для успешного входа
  val onLoginSuccess: (CurrentUser) -> Unit = { userAuth ->
    currentUser = userAuth
    currentScreen = Screen.Chats
  }

  // Callback-функция для перехода к чату
  val onChatClicked: (Chat) -> Unit = { chat ->
    currentChat = chat
    currentScreen = Screen.Chat
  }

  // Callback-функция для выхода из аккаунта
  val onLogoutClicked: () -> Unit = {
    currentUser = CurrentUser(id = -1, username = "", password = "")
    currentScreen = Screen.Login
  }

  // В зависимости от текущего экрана отображаем соответствующий компонент
  when (currentScreen) {
    Screen.Login -> LoginScreen(
      context = context,
      onLoginSuccess = onLoginSuccess
    ) {
      // Переход к экрану регистрации
      currentScreen = Screen.Registration
    }

    Screen.Registration -> RegistrationScreen(
      context = context,
      onLoginClicked = {
        // Переход к экрану входа
        currentScreen = Screen.Login
      }
    )

    Screen.Chats -> ChatsScreen(
      context = context,
      currentUser = currentUser,
      onChatClicked = onChatClicked,
      onLogoutClicked = onLogoutClicked
    )

    Screen.Chat -> ChatScreen(
      chat = currentChat,
      currentUser = currentUser,
      onBackClicked = {
        // Переход к списку чатов
        currentScreen = Screen.Chats
      }
    )

  }
}

/**
 * Перечисление возможных экранов приложения.
 */
enum class Screen {
  Login,          // Экран входа
  Registration,   // Экран регистрации
  Chats,          // Экран списка чатов
  Chat            // Экран чата
}
