package com.example.chatik.api.request

import com.example.chatik.api.model.Message
import com.example.chatik.api.model.SendMessageRequest
import com.example.chatik.api.model.UserID
import com.example.chatik.api.model.UserMessageRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageService {
  @POST("/message")
  suspend fun getLastMessages(@Body userID: UserID): List<Message>

  @POST("/user-message")
  suspend fun getUserMessages(@Body userMessageRequest: UserMessageRequest): List<Message>

  @POST("/send")
  suspend fun sendMessage(@Body sendMessageRequest: SendMessageRequest)
}
