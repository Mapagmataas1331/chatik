package com.example.chatik.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatik.api.RetrofitClient
import com.example.chatik.api.model.MessagesList
import com.example.chatik.api.model.SearchString
import com.example.chatik.api.model.SearchUsers
import com.example.chatik.api.model.UserMessagesRequest
import com.example.chatik.model.Chat
import com.example.chatik.model.CurrentUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatsScreen(
  context: Context,
  currentUser: CurrentUser,
  onChatClicked: (Chat) -> Unit
) {
  var allChats by remember { mutableStateOf<List<Chat>>(emptyList()) }
  var searchText by remember { mutableStateOf("") }

  Column(modifier = Modifier.fillMaxSize()) {
    SearchBar(
      query = searchText,
      onQueryChange = { searchText = it },
      onSearch = {},
      active = true,
      onActiveChange = {},
      placeholder = { Text("Search...") },
      content = { LoadChats(
        context,
        currentUser,
        searchText,
        onChatClicked,
        allChats,
        setAllChats = { allChats = it.toList() }
      ) }
    )
  }
}

@Composable
fun LoadChats(
  context: Context,
  currentUser: CurrentUser,
  searchText: String,
  onChatClicked: (Chat) -> Unit,
  allChats: List<Chat>,
  setAllChats: (List<Chat>) -> Unit
) {
  LaunchedEffect(searchText) {
    try {
      val searchString = SearchString(searchText)
      val response: SearchUsers = withContext(Dispatchers.IO) {
        RetrofitClient.createUserService().searchUsers(searchString)
      }

    val updatedChats = mutableListOf<Chat>()

      for (user in response.searchUsers) {
        if (user.id in 3..100) {
          continue
        }
        updatedChats.add(Chat(user.id, user.username, null))
      }

      setAllChats(updatedChats)
      updatedChats.clear()

      for (user in response.searchUsers) {
        if (user.id in 3..100) {
          continue
        }
        val userMessagesRequest = UserMessagesRequest(currentUser.username, currentUser.password, user.id)
        val messageList: MessagesList = withContext(Dispatchers.IO) {
          RetrofitClient.createMessageService().getUserMessages(userMessagesRequest)
        }

        val messages = messageList.messagesList.ifEmpty {
          null
        }

        updatedChats.add(Chat(user.id, user.username, messages))
      }

      val sortedChats = updatedChats.sortedByDescending { chat ->
        chat.messages?.maxByOrNull { it.datetime }?.datetime
      }

      setAllChats(sortedChats)

    } catch (e: Exception) {
      e.printStackTrace()
      // Handle error
    }
  }
  DisplayChats(allChats, onChatClicked)
}

@Composable
fun DisplayChats(
  chats: List<Chat>,
  onChatClicked: (Chat) -> Unit
) {
  Column(modifier = Modifier.fillMaxSize()) {
    LazyColumn(
      modifier = Modifier.weight(1f)
    ) {
      itemsIndexed(chats) { index, chat ->
        ChatItem(chat, onChatClicked)
        if (index < chats.size - 1) {
          HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }
      }
    }
  }
}

@Composable
fun ChatItem(
  chat: Chat,
  onChatClicked: (Chat) -> Unit
) {
  val color = generateColor(chat.username)

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onChatClicked(chat) },
    verticalAlignment = Alignment.CenterVertically
  ) {
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
    Column(
      modifier = Modifier.weight(1f)
    ) {
      Text(
        text = chat.username,
        modifier = Modifier.padding(8.dp)
      )
      chat.messages?.firstOrNull()?.let { firstMessage ->
        Text(
          text = firstMessage.message,
          modifier = Modifier.padding(8.dp),
          style = TextStyle(color = Color.Blue)
        )
      }
    }
  }
}