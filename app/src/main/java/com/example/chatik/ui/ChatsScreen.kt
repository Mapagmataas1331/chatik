package com.example.chatik.ui

import android.content.Context
import android.util.Log
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
import com.example.chatik.api.model.MessagesList
import com.example.chatik.api.model.SearchString
import com.example.chatik.api.model.SearchUsers
import com.example.chatik.api.model.UserMessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
  context: Context,
  currentUserID: Int,
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
    LoadChats(context, chats, currentUserID, searchText, onChatClicked)
  }
}

@Composable
fun LoadChats(
  context: Context,
  chats: MutableList<String>,
  currentUserID: Int, // Receive the current user ID
  searchText: String,
  onChatClicked: (String) -> Unit
) {
  LaunchedEffect(searchText) {
    try {
      val searchString = SearchString(searchText)
      val response: SearchUsers = withContext(Dispatchers.IO) {
        RetrofitClient.createUserService().searchUsers(searchString)
      }
      Log.d("TAG----------------", response.searchUsers.toString())
      chats.clear()
      for (user in response.searchUsers) {
        val userMessageRequest = UserMessageRequest(currentUserID, user.id)
        val messages: MessagesList = withContext(Dispatchers.IO) {
          RetrofitClient.createMessageService().getUserMessages(userMessageRequest)
        }
        Log.d("TAG----------------", messages.messagesList.toString())
        if (messages.messagesList.isNotEmpty()) {
          Log.d("TAG----------------", "true")
          val lastMessage = messages.messagesList.last()
          val messageText = "${lastMessage.from.username}: ${lastMessage.message}"
          chats.add(messageText)
        } else {
          Log.d("TAG----------------", "false")
          chats.add(user.username)
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
