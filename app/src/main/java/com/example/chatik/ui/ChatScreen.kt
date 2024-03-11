package com.example.chatik.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.chatik.api.RetrofitClient
import com.example.chatik.api.model.Auth
import com.example.chatik.api.model.Message
import com.example.chatik.api.model.SendMessageRequest
import com.example.chatik.api.model.UserMessagesRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ChatScreen(
  friendUsername: String,
  userAuth: Auth,
  onBackClicked: () -> Unit
) {
  var messageText by remember { mutableStateOf("") }
  var messages by remember { mutableStateOf<List<Message>>(emptyList()) }

  val context = LocalContext.current

  LaunchedEffect(key1 = friendUsername) {
    // Load messages between user and friendUsername
    messages = loadMessages(context, userAuth, friendUsername)
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
      Button(
        onClick = { onBackClicked() },
        modifier = Modifier.padding(16.dp)
      ) {
        Text("Back")
      }
      Text(
        text = "Chatting with: $friendUsername",
        modifier = Modifier.padding(16.dp)
      )
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      messages.forEach { message ->
        Text(text = "${message.from.username}: ${message.message}")
      }
    }

    TextField(
      value = messageText,
      onValueChange = { messageText = it },
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
      placeholder = { Text("Type a message...") },
      keyboardOptions = KeyboardOptions.Default.copy(
        imeAction = ImeAction.Send
      ),
      keyboardActions = KeyboardActions(
        onSend = {
          sendMessage(context, userAuth, friendUsername, messageText) { success ->
            if (success) {
              messageText = ""
            } else {
              Toast.makeText(
                context,
                "Failed to send message. Please try again.",
                Toast.LENGTH_SHORT
              ).show()
            }
          }
        }
      )
    )
  }
}

private suspend fun loadMessages(
  context: Context,
  userAuth: Auth,
  friendUsername: String
): List<Message> {
  return withContext(Dispatchers.IO) {
    try {
      val friendInfo = RetrofitClient.createUserService().getUserInfo(friendUsername)
      val userMessagesRequest = UserMessagesRequest(userAuth.username, userAuth.password, friendInfo.id)
      val messages = RetrofitClient.createMessageService().getUserMessages(userMessagesRequest)
      messages.messagesList
    } catch (e: Exception) {
      e.printStackTrace()
      // Handle error
      emptyList()
    }
  }
}

private fun sendMessage(
  context: Context,
  userAuth: Auth,
  friendUsername: String,
  message: String,
  callback: (Boolean) -> Unit
) {
  CoroutineScope(Dispatchers.IO).launch {
    try {
      val friendInfo = RetrofitClient.createUserService().getUserInfo(friendUsername)
      val sendMessageRequest = SendMessageRequest(userAuth.username, userAuth.password, friendInfo.id, message)
      RetrofitClient.createMessageService().sendMessage(sendMessageRequest)
      withContext(Dispatchers.Main) {
        callback(true)
      }
    } catch (e: Exception) {
      e.printStackTrace()
      withContext(Dispatchers.Main) {
        callback(false)
      }
    }
  }
}
