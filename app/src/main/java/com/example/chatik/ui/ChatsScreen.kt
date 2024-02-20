package com.example.chatik.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChatsScreen(onChatClicked: (String) -> Unit) {
  val chats = remember { mutableStateListOf<String>() }
  var searchText by remember { mutableStateOf("") }

  Column(modifier = Modifier.fillMaxSize()) {
    SearchBar(searchText) { searchText = it }
    ChatsList(chats, onChatClicked)
  }
}

@Composable
fun SearchBar(searchText: String, onSearchTextChanged: (String) -> Unit) {
  TextField(
    value = searchText,
    onValueChange = onSearchTextChanged,
    modifier = Modifier.fillMaxWidth(),
    placeholder = { Text("Search...") }
  )
}

@Composable
fun ChatsList(chats: List<String>, onChatClicked: (String) -> Unit) {
  LazyColumn {
    items(chats) { chat ->
      Text(
        text = chat,
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onChatClicked(chat) }
          .padding(16.dp)
      )
    }
  }
}
