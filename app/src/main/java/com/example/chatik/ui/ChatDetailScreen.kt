package com.example.chatik.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatik.api.model.Message
import com.example.chatik.api.model.User

@Composable
fun ChatDetailScreen(
  chatUser: User,
  messages: List<Message>,
  onBackClick: () -> Unit,
  onSendMessageClick: (String) -> Unit
) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Top
  ) {
    ChatHeader(chatUser = chatUser, onBackClick = onBackClick)
    Spacer(modifier = Modifier.height(16.dp))
    MessageList(messages = messages)
    Spacer(modifier = Modifier.height(16.dp))
    SendMessageBar(onSendMessageClick = onSendMessageClick)
  }
}

@Composable
fun ChatHeader(chatUser: User, onBackClick: () -> Unit) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Button(onClick = onBackClick) {
      Text("Back")
    }
    Text(text = "Chat with ${chatUser.username}")
    // Дополнительные действия с пользователем, если нужно
  }
}

@Composable
fun MessageList(messages: List<Message>) {
  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Top
  ) {
    messages.forEach { message ->
      MessageItem(message = message)
      Spacer(modifier = Modifier.height(8.dp))
    }
  }
}

@Composable
fun SendMessageBar(onSendMessageClick: (String) -> Unit) {
  var messageText by remember { mutableStateOf("") }

  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    OutlinedTextField(
      value = messageText,
      onValueChange = { messageText = it },
      modifier = Modifier.weight(1f),
      placeholder = { Text("Type your message") }
    )
    Spacer(modifier = Modifier.width(8.dp))
    Button(
      onClick = {
        onSendMessageClick(messageText)
        messageText = ""
      }
    ) {
      Text("Send")
    }
  }
}

@Composable
fun MessageItem(message: Message) {
  Text(text = "${message.from.username}: ${message.message}")
}
