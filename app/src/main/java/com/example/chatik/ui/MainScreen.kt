package com.example.chatik.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.chatik.api.model.LoginRequest
import com.example.chatik.api.model.LoginResponse
import com.example.chatik.api.model.Message
import com.example.chatik.api.model.RegistrationRequest
import com.example.chatik.api.model.RegistrationResponse
import com.example.chatik.api.model.SearchRequest
import com.example.chatik.api.model.User
import com.example.chatik.api.model.UserIdRequest
import com.example.chatik.api.model.UserInfoResponse
import com.example.chatik.api.request.AuthService
import com.example.chatik.api.request.MessageService
import com.example.chatik.api.request.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
  authService: AuthService,
  messageService: MessageService,
  userService: UserService
) {
  var username by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var searchString by remember { mutableStateOf("") }
  var registrationId by remember { mutableStateOf(0) }
  var loginId by remember { mutableStateOf(0) }
  var lastMessages by remember { mutableStateOf(emptyList<Message>()) }
  var foundUsers by remember { mutableStateOf(emptyList<User>()) }
  var userInfo by remember { mutableStateOf(UserInfoResponse(0, "", "", "")) }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    TextField(
      value = username,
      onValueChange = { username = it },
      modifier = Modifier.fillMaxWidth(),
      placeholder = { Text("Username") }
    )
    TextField(
      value = password,
      onValueChange = { password = it },
      modifier = Modifier.fillMaxWidth(),
      placeholder = { Text("Password") }
    )
    Button(
      onClick = {
        GlobalScope.launch(Dispatchers.Main) {
          val response = authService.registerUser(RegistrationRequest(username, password, null, null))
          registrationId = response.id
        }
      },
      modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
      Text("Register")
    }
    Button(
      onClick = {
        GlobalScope.launch(Dispatchers.Main) {
          val response = authService.loginUser(LoginRequest(username, password))
          loginId = response.id
        }
      },
      modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
      Text("Login")
    }
    Spacer(modifier = Modifier.height(16.dp))
    TextField(
      value = searchString,
      onValueChange = { searchString = it },
      modifier = Modifier.fillMaxWidth(),
      label = { Text("Search String") }
    )
    Button(
      onClick = {
        GlobalScope.launch(Dispatchers.Main) {
          val response = userService.searchUsers(SearchRequest(searchString))
          foundUsers = response.searchUsers
        }
      },
      modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
      Text("Search Users")
    }
    Spacer(modifier = Modifier.height(16.dp))
    Button(
      onClick = {
        GlobalScope.launch(Dispatchers.Main) {
          val response = userService.getUserInfo(username)
          userInfo = response
        }
      },
      modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
      Text("Get User Info")
    }

    Spacer(modifier = Modifier.height(16.dp))
    Button(
      onClick = {
        GlobalScope.launch(Dispatchers.Main) {
          val response = messageService.getLastMessages(UserIdRequest(loginId))
          lastMessages = response.lastMessages
        }
      },
      modifier = Modifier.align(Alignment.CenterHorizontally)
    ) {
      Text("Get Last Messages")
    }
  }
}
