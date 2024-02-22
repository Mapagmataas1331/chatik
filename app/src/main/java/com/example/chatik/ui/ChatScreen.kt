package com.example.chatik.ui

import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.chatik.api.RetrofitClient
import com.example.chatik.api.model.Message
import com.example.chatik.api.model.SendMessageRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ChatScreen(
  friendUsername: String,
  currentUserID: Int,
  onBackClicked: () -> Unit
) {
  var messageText by remember { mutableStateOf("") }
  var messages by remember { mutableStateOf<List<Message>>(emptyList()) }

  val context = LocalContext.current

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Button(onClick = { onBackClicked() }) {
        Text("Back")
      }
      Spacer(modifier = Modifier.width(16.dp))
      Text(text = friendUsername)
    }

    Column(
      modifier = Modifier
        .weight(1f)
        .fillMaxWidth()
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
          sendMessage(context, currentUserID, friendUsername, messageText) { success ->
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

private fun sendMessage(
  context: Context,
  currentUserID: Int,
  friendUsername: String,
  message: String,
  callback: (Boolean) -> Unit
) {
  CoroutineScope(Dispatchers.IO).launch {
    try {
      val friendInfo = RetrofitClient.createUserService().getUserInfo(friendUsername)
      val sendMessageRequest = SendMessageRequest(currentUserID, friendInfo.id, message)
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
