package com.example.chatik.api.request

import com.example.chatik.api.model.SendMessageRequest
import com.example.chatik.api.model.LastMessages
import com.example.chatik.api.model.MessagesList
import com.example.chatik.api.model.LastMessagesRequest
import com.example.chatik.api.model.UserMessagesRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageService {
  @POST("/message")
  suspend fun getLastMessages(@Body lastMessageRequest: LastMessagesRequest): LastMessages

  @POST("/user-message")
  suspend fun getUserMessages(@Body userMessagesRequest: UserMessagesRequest): MessagesList

  @POST("/send")
  suspend fun sendMessage(@Body sendMessageRequest: SendMessageRequest)
}
