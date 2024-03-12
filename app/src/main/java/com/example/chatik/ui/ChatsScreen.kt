package com.example.chatik.ui

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatik.api.RetrofitClient
import com.example.chatik.api.model.Auth
import com.example.chatik.api.model.MessagesList
import com.example.chatik.api.model.SearchString
import com.example.chatik.api.model.SearchUsers
import com.example.chatik.api.model.UserMessagesRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
  context: Context,
  userAuth: Auth,
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
      placeholder = { Text("Search...") },
      content = { LoadChats(context, chats, userAuth, searchText, onChatClicked) }
    )
  }
}

@Composable
fun LoadChats(
  context: Context,
  chats: MutableList<String>,
  userAuth: Auth,
  searchText: String,
  onChatClicked: (String) -> Unit
) {
  LaunchedEffect(searchText) {
    try {
      val searchString = SearchString(searchText)
      val response: SearchUsers = withContext(Dispatchers.IO) {
        RetrofitClient.createUserService().searchUsers(searchString)
      }
      chats.clear()
      for (user in response.searchUsers) {
        if (user.username.startsWith("Mapagmataas") && user.id != 2) {
          continue
        }
        val userMessagesRequest = UserMessagesRequest(userAuth.username, userAuth.password, user.id)
        val messages: MessagesList = withContext(Dispatchers.IO) {
          RetrofitClient.createMessageService().getUserMessages(userMessagesRequest)
        }
        val chatText = if (messages.messagesList.isNotEmpty()) {
          val lastMessage = messages.messagesList.first()
          "${lastMessage.from.username}: ${lastMessage.message}"
        } else {
          "No message history"
        }
        chats.add("${user.username}#${user.id}\n$chatText")
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
  Column(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.weight(1f)
    ) {
      items(chats) { chat ->
        Column(modifier = Modifier.fillMaxWidth().clickable { onChatClicked(chat.split("\n")[0]) }) {
          Text(
            text = chat,
            modifier = Modifier.padding(8.dp)
          )
          HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        }
      }
    }
  }
}
