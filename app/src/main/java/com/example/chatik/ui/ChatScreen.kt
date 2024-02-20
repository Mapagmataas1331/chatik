package com.example.chatik.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chatik.api.model.Message
import com.example.chatik.api.model.User

@Composable
fun ChatScreen(
  messages: List<Message>,
  onChatItemClick: (User) -> Unit
) {
  // Group messages by sender or receiver to simulate chats
  val chats = messages.groupBy { if (it.from.id == currentUser.id) it.to else it.from }
    .map { (user, messages) ->
      Chat(user, messages.lastOrNull()?.message ?: "")
    }

  Column(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.Top
  ) {
    Spacer(modifier = Modifier.height(16.dp))
    ChatList(chats = chats, onChatItemClick = onChatItemClick)
  }
}

@Composable
fun ChatList(
  messages: List<Message>,
  onChatItemClick: (User) -> Unit
) {
  LazyColumn {
    items(messages) { message ->
      ChatItem(chat = chat, onItemClick = { onChatItemClick(message.id) })
      Divider()
    }
  }
}

@Composable
fun ChatItem(
  chat: Chat,
  onItemClick: () -> Unit
) {
  Column(
    modifier = Modifier.clickable { onItemClick() }.padding(16.dp)
  ) {
    Text(text = chat.user.username)
    Text(text = chat.lastMessage)
  }
}

@Composable
fun SearchBar(
  userService: UserService,
  onSearchResult: (List<User>) -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }

  Column(
    modifier = Modifier.fillMaxWidth()
  ) {
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      modifier = Modifier.fillMaxWidth(),
      label = { Text("Search users by username") }
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(
      onClick = {
        // Perform search operation
        if (searchQuery.isNotEmpty()) {
          GlobalScope.launch(Dispatchers.Main) {
            try {
              val searchResponse = userService.searchUsers(SearchRequest(searchQuery))
              onSearchResult(searchResponse.searchUsers)
            } catch (e: Exception) {
              // Handle error
              Log.e("Search", "Error: ${e.message}", e)
            }
          }
        }
      },
      modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
      Text("Search")
    }
  }
}
