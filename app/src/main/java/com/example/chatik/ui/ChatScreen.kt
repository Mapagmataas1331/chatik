package com.example.chatik.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
  friendUsername: String,
  onBackClicked: () -> Unit
) {
  val messages = remember { mutableStateListOf<String>() }
  var newMessageText by remember { mutableStateOf("") }

  Column(modifier = Modifier.fillMaxSize()) {
    TopAppBar(
      title = { Text(text = friendUsername) },
      navigationIcon = {
        IconButton(onClick = onBackClicked) {
          Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
      }
    )
    MessagesList(messages)
    MessageInput(
      message = newMessageText,
      onMessageChange = { newMessageText = it },
      onSend = {
        messages.add(it)
        // Logic for sending message to the friend
        newMessageText = ""
      }
    )
  }
}

@Composable
fun MessagesList(messages: List<String>) {
  LazyColumn {
    items(messages) { message ->
      Text(text = message)
    }
  }
}

@Composable
fun MessageInput(
  message: String,
  onMessageChange: (String) -> Unit,
  onSend: (String) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    TextField(
      value = message,
      onValueChange = { onMessageChange(it) },
      modifier = Modifier.weight(1f),
      placeholder = { Text("Type a message...") }
    )
    Button(onClick = { onSend(message) }) {
      Text("Send")
    }
  }
}
