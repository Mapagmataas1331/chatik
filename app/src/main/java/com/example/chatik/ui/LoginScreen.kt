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
import com.example.chatik.model.CurrentUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Экран входа в приложение.
 *
 * @param context Контекст приложения.
 * @param onLoginSuccess Callback-функция для обработки успешного входа в приложение.
 * @param onRegisterClicked Callback-функция для перехода на экран регистрации.
 */
@Composable
fun LoginScreen(
  context: Context,
  onLoginSuccess: (CurrentUser) -> Unit,
  onRegisterClicked: () -> Unit
) {
  // Состояния для имени пользователя и пароля
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Поле ввода имени пользователя
    TextField(
      value = username,
      onValueChange = { username = it },
      label = { Text("Username") }
    )
    Spacer(modifier = Modifier.height(16.dp))
    // Поле ввода пароля
    TextField(
      value = password,
      onValueChange = { password = it },
      label = { Text("Password") },
      visualTransformation = PasswordVisualTransformation(),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
    Spacer(modifier = Modifier.height(16.dp))
    // Кнопка для входа
    Button(onClick = {
      // Выполнение логики входа
      loginUser(context, username, password) { currentUser: CurrentUser ->
        if (currentUser.id >= 0) {
          // В случае успешного входа вызываем соответствующий коллбэк
          onLoginSuccess(currentUser)
        } else {
          // В случае неудачного входа показываем сообщение об ошибке
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
    // Кнопка для перехода на экран регистрации
    TextButton(onClick = onRegisterClicked) {
      Text("Register")
    }
  }
}

/**
 * Функция для выполнения запроса на вход пользователя.
 *
 * @param context Контекст приложения.
 * @param username Имя пользователя.
 * @param password Пароль пользователя.
 * @param callback Callback-функция для обработки результата входа.
 */
private fun loginUser(context: Context, username: String, password: String, callback: (CurrentUser) -> Unit) {
  val authService = RetrofitClient.createAuthService()
  // Запуск корутины для выполнения запроса на вход
  CoroutineScope(Dispatchers.IO).launch {
    try {
      val response: Id = authService.loginUser(LoginRequest(username, password))
      val success = response.id >= 0
      // Возврат результата в основной поток
      withContext(Dispatchers.Main) {
        if (success) {
          callback(CurrentUser(response.id, username, password))
        } else {
          callback(CurrentUser(-1, username, password))
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      // Обработка ошибки и показ сообщения об ошибке в основном потоке
      withContext(Dispatchers.Main) {
        callback(CurrentUser(-1, username, password))
        Toast.makeText(
          context,
          e.message,
          Toast.LENGTH_SHORT
        ).show()
      }
    }
  }
}
