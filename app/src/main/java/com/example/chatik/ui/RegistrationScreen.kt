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

/**
 * Экран регистрации нового пользователя.
 *
 * @param context Контекст приложения.
 * @param onLoginClicked Callback-функция для перехода к экрану входа.
 */
@Composable
fun RegistrationScreen(
  context: Context,
  onLoginClicked: () -> Unit
) {
  // Состояния для полей ввода
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var firstName by remember { mutableStateOf("") }
  var lastName by remember { mutableStateOf("") }

  // Колонка с компонентами интерфейса
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
    // Поле ввода имени
    TextField(
      value = firstName,
      onValueChange = { firstName = it },
      label = { Text("First Name") }
    )
    Spacer(modifier = Modifier.height(16.dp))
    // Поле ввода фамилии
    TextField(
      value = lastName,
      onValueChange = { lastName = it },
      label = { Text("Last Name") }
    )
    Spacer(modifier = Modifier.height(16.dp))
    // Кнопка регистрации
    Button(onClick = {
      // Выполнение логики регистрации
      registerUser(context, username, password, firstName, lastName) { success ->
        if (success) {
          // Показ уведомления об успешной регистрации и переход к экрану входа
          Toast.makeText(
            context,
            "Registration successful.",
            Toast.LENGTH_SHORT
          ).show()
          onLoginClicked()
        } else {
          // Показ уведомления о неудачной регистрации
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
    // Кнопка для перехода к экрану входа
    TextButton(onClick = onLoginClicked) {
      Text("Login")
    }
  }
}

/**
 * Функция для регистрации нового пользователя.
 *
 * @param context Контекст приложения.
 * @param username Имя пользователя.
 * @param password Пароль пользователя.
 * @param firstName Имя пользователя.
 * @param lastName Фамилия пользователя.
 * @param callback Функция обратного вызова для обработки результатов регистрации.
 */
private fun registerUser(context: Context, username: String, password: String, firstName: String, lastName: String, callback: (Boolean) -> Unit) {
  val authService = RetrofitClient.createAuthService()
  CoroutineScope(Dispatchers.IO).launch {
    try {
      val response: Id = authService.registerUser(RegistrationRequest(username, password, firstName, lastName))
      val success = response.id >= 0
      withContext(Dispatchers.Main) {
        // Уведомляем о результате регистрации через коллбэк
        callback(success)
      }
    } catch (e: Exception) {
      e.printStackTrace()
      withContext(Dispatchers.Main) {
        // Уведомляем о возникшей ошибке и неудачной регистрации
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
