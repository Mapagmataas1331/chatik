package com.example.chatik.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatik.api.RetrofitClient
import com.example.chatik.model.CurrentUser
import com.example.chatik.api.model.Message
import com.example.chatik.api.model.MessagesList
import com.example.chatik.api.model.SendMessageRequest
import com.example.chatik.api.model.UserMessagesRequest
import com.example.chatik.model.Chat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.absoluteValue

/**
 * Компонент для отображения чата.
 *
 * @param chat Чат для отображения.
 * @param currentUser Текущий пользователь.
 * @param onBackClicked Callback-функция для обработки нажатия кнопки "назад".
 */
@Composable
fun ChatScreen(
  chat: Chat,
  currentUser: CurrentUser,
  onBackClicked: () -> Unit
) {
  // Строка для ввода сообщения
  var messageText by remember { mutableStateOf("") }
  // Список сообщений в чате
  var messages by remember { mutableStateOf<List<Message>>(emptyList()) }

  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val scrollState = rememberScrollState()

  // Загрузка сообщений при изменении чата
  LaunchedEffect(key1 = chat) {
    messages = if (chat.messages.isNullOrEmpty()) {
      loadMessages(context, currentUser, chat)
    } else {
      chat.messages.reversed()
    }
  }

  // Прокрутка к последнему сообщению
  LaunchedEffect(messages) {
    scrollState.scrollTo(scrollState.maxValue)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White)
  ) {
    // Верхняя панель с информацией о чате
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Кнопка "назад"
      IconButton(
        onClick = { onBackClicked() },
        modifier = Modifier.padding(8.dp)
      ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
      }
      // Имя чата и его идентификатор
      Text(
        text = "${chat.username}#${chat.id}",
        modifier = Modifier
          .weight(2f)
          .padding(8.dp),
        style = TextStyle(fontSize = 18.sp),
        textAlign = TextAlign.Center
      )
      // Отображение инициалов пользователя в круглой области
      val color = generateColor(chat.username)
      Box(
        modifier = Modifier
          .padding(8.dp)
          .size(40.dp)
          .background(color, shape = CircleShape),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = chat.username.substring(0, 1).uppercase(),
          style = TextStyle(fontSize = 20.sp),
          textAlign = TextAlign.Center
        )
      }
    }

    // Область для отображения сообщений
    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
    ) {
      Box(
        modifier = Modifier
          .padding(4.dp)
          .verticalScroll(state = scrollState)
      ) {
        Column {
          // Отображение каждого сообщения в отдельном блоке
          messages.forEach { message ->
            // Форматирование даты и времени сообщения
            val formattedDateTime = "${message.datetime.substring(8, 10)}.${message.datetime.substring(5, 7)}-${message.datetime.substring(11, 16)}"
            // Отображение сообщения
            Surface(
              modifier = Modifier.padding(4.dp),
              shape = RoundedCornerShape(8.dp),
              color = Color.LightGray
            ) {
              Box(
                modifier = Modifier.padding(8.dp)
              ) {
                Column {
                  // Отображение имени отправителя
                  Text(
                    text = message.from.username,
                    modifier = Modifier.align(Alignment.Start)
                  )
                  // Отображение даты и времени
                  Text(
                    text = formattedDateTime,
                    modifier = Modifier.align(Alignment.End)
                  )
                  // Отображение текста сообщения
                  Text(
                    text = message.message,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                  )
                }
              }
            }
          }
        }
      }
    }

    // Поле для ввода нового сообщения
    TextField(
      value = messageText,
      onValueChange = { messageText = it },
      modifier = Modifier.fillMaxWidth(),
      placeholder = { Text("Type a message...") },
      keyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Send
      ),
      keyboardActions = KeyboardActions(
        onSend = {
          coroutineScope.launch {
            // Отправка сообщения
            sendMessage(context, currentUser, chat, messageText)
            messageText = ""
            // Загрузка обновленного списка сообщений
            messages = loadMessages(context, currentUser, chat)
            // Задержка перед прокруткой к последнему сообщению
            delay(100)
            scrollState.scrollTo(scrollState.maxValue)
          }
        }
      )
    )
  }
}

/**
 * Генерация цвета на основе имени пользователя.
 *
 * @param username Имя пользователя.
 * @return Сгенерированный цвет.
 */
@Composable
fun generateColor(username: String): Color {
  val defaultColors = listOf(
    Color.Red,
    Color.Blue,
    Color.Green,
    Color.Magenta,
    Color.Cyan,
    Color.Yellow
  )
  // Выбор цвета на основе хэш-кода имени пользователя
  val index = (username.hashCode().absoluteValue) % defaultColors.size
  return defaultColors[index]
}

/**
 * Загрузка сообщений из чата.
 *
 * @param context Контекст приложения.
 * @param currentUser Текущий пользователь.
 * @param chat Чат для загрузки сообщений.
 * @return Список загруженных сообщений.
 */
private suspend fun loadMessages(
  context: Context,
  currentUser: CurrentUser,
  chat: Chat
): List<Message> {
  return withContext(Dispatchers.IO) {
    try {
      val userMessagesRequest = UserMessagesRequest(currentUser.username, currentUser.password, chat.id)
      // Запрос на сервер для получения сообщений пользователя
      val messageList: MessagesList = RetrofitClient.createMessageService().getUserMessages(userMessagesRequest)
      // Переворачиваем список сообщений, чтобы последние сообщения были вверху
      messageList.messagesList.reversed()
    } catch (e: Exception) {
      e.printStackTrace()
      // Обработка ошибки
      emptyList()
    }
  }
}

/**
 * Отправка сообщения в чат.
 *
 * @param context Контекст приложения.
 * @param currentUser Текущий пользователь.
 * @param chat Чат для отправки сообщения.
 * @param message Текст сообщения.
 */
private suspend fun sendMessage(
  context: Context,
  currentUser: CurrentUser,
  chat: Chat,
  message: String
) {
  try {
    val sendMessageRequest = SendMessageRequest(currentUser.username, currentUser.password, chat.id, message)
    // Отправка сообщения на сервер
    RetrofitClient.createMessageService().sendMessage(sendMessageRequest)

  } catch (e: Exception) {
    e.printStackTrace()
  }
}
