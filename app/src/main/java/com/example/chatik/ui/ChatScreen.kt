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

@Composable
fun ChatScreen(
  chat: Chat,
  currentUser: CurrentUser,
  onBackClicked: () -> Unit
) {
  var messageText by remember { mutableStateOf("") }
  var messages by remember { mutableStateOf<List<Message>>(emptyList()) }

  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val scrollState = rememberScrollState()

  LaunchedEffect(key1 = chat) {
    messages = if (chat.messages.isNullOrEmpty()) {
      loadMessages(context, currentUser, chat)
    } else {
      chat.messages.reversed()
    }
  }

  LaunchedEffect(messages) {
    scrollState.scrollTo(scrollState.maxValue)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(Color.White)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(
        onClick = { onBackClicked() },
        modifier = Modifier.padding(8.dp)
      ) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
      }
      Text(
        text = "${chat.username}#${chat.id}",
        modifier = Modifier
          .weight(2f)
          .padding(8.dp),
        style = TextStyle(fontSize = 18.sp),
        textAlign = TextAlign.Center
      )
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
          messages.forEach { message ->
            val formattedDateTime = "${message.datetime.substring(8, 10)}.${message.datetime.substring(5, 7)}-${message.datetime.substring(11, 16)}"
            Surface(
              modifier = Modifier.padding(4.dp),
              shape = RoundedCornerShape(8.dp),
              color = Color.LightGray
            ) {
              Box(
                modifier = Modifier.padding(8.dp)
              ) {
                Column {
                  Text(
                    text = message.from.username,
                    modifier = Modifier.align(Alignment.Start)
                  )
                  Text(
                    text = formattedDateTime,
                    modifier = Modifier.align(Alignment.End)
                  )
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
            sendMessage(context, currentUser, chat, messageText)
            messageText = ""
            messages = loadMessages(context, currentUser, chat)
            delay(100)
            scrollState.scrollTo(scrollState.maxValue)
          }
        }
      )
    )
  }
}

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
  val index = (username.hashCode().absoluteValue) % defaultColors.size
  return defaultColors[index]
}

private suspend fun loadMessages(
  context: Context,
  currentUser: CurrentUser,
  chat: Chat
): List<Message> {
  return withContext(Dispatchers.IO) {
    try {
      val userMessagesRequest = UserMessagesRequest(currentUser.username, currentUser.password, chat.id)
      val messageList: MessagesList = RetrofitClient.createMessageService().getUserMessages(userMessagesRequest)
      messageList.messagesList.reversed()
    } catch (e: Exception) {
      e.printStackTrace()
      // Handle error
      emptyList()
    }
  }
}

private suspend fun sendMessage(
  context: Context,
  currentUser: CurrentUser,
  chat: Chat,
  message: String
) {
  try {
    val sendMessageRequest = SendMessageRequest(currentUser.username, currentUser.password, chat.id, message)
    RetrofitClient.createMessageService().sendMessage(sendMessageRequest)

  } catch (e: Exception) {
    e.printStackTrace()
  }
}
