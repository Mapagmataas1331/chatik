package com.example.chatik.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.chatik.api.RetrofitClient
import com.example.chatik.api.model.UserMessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
  context: Context,
  onChatClicked: (String) -> Unit
) {
  val chats = remember { mutableStateListOf<String>() }
  var searchText by remember { mutableStateOf("") }

  Column(modifier = Modifier.fillMaxSize()) {
    SearchBar(
      query = searchText,
      onQueryChange = { searchText = it },
      onSearch = {},
      active = true,
      onActiveChange = {},
      content = {}
    )
    LoadChats(context, chats, searchText, onChatClicked)
  }
}

@Composable
fun LoadChats(
  context: Context,
  chats: MutableList<String>,
  searchText: String,
  onChatClicked: (String) -> Unit
) {
  val userService = remember { RetrofitClient.createUserService() }
  val currentUser = remember { /* Obtain the current user */ 0 } // Replace 0 with the current user ID

  LaunchedEffect(searchText) {
    try {
      val users = withContext(Dispatchers.IO) { userService.searchUsers(searchText) }
      chats.clear()
      for (user in users) {
        val userMessageRequest = UserMessageRequest(currentUser, user.id)
        val messages = withContext(Dispatchers.IO) {
          RetrofitClient.createMessageService().getUserMessages(userMessageRequest)
        }
        if (messages.isNotEmpty()) {
          val lastMessage = messages.last()
          val messageText = "${lastMessage.from.username}: ${lastMessage.message}"
          chats.add(messageText)
        }
      }
    } catch (e: Exception) {
      e.printStackTrace()
      // Handle error
    }
  }
  DisplayChats(chats, onChatClicked)
}

@Composable
fun DisplayChats(
  chats: List<String>,
  onChatClicked: (String) -> Unit
) {
  Column {
    chats.forEach { chat ->
      Text(
        text = chat,
        modifier = Modifier.clickable { onChatClicked(chat) }
      )
    }
  }
}
