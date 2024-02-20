package com.example.chatik.api.request

import com.example.chatik.api.model.Message
import com.example.chatik.api.model.SendMessageRequest
import com.example.chatik.api.model.Id
import com.example.chatik.api.model.LastMessages
import com.example.chatik.api.model.MessagesList
import com.example.chatik.api.model.UserId
import com.example.chatik.api.model.UserMessageRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageService {
  @POST("/message")
  suspend fun getLastMessages(@Body userId: UserId): LastMessages

  @POST("/user-message")
  suspend fun getUserMessages(@Body userMessageRequest: UserMessageRequest): MessagesList

  @POST("/send")
  suspend fun sendMessage(@Body sendMessageRequest: SendMessageRequest)
}
