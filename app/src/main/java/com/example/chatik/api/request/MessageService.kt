package com.example.chatik.api.request

import com.example.chatik.api.model.LastMessagesResponse
import com.example.chatik.api.model.SendMessageRequest
import com.example.chatik.api.model.UserIdRequest
import com.example.chatik.api.model.UserMessageRequest
import com.example.chatik.api.model.UserMessagesResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface MessageService {
  @POST("/message")
  suspend fun getLastMessages(@Body userIdRequest: UserIdRequest): LastMessagesResponse

  @POST("/user-message")
  suspend fun getUserMessages(@Body userMessageRequest: UserMessageRequest): UserMessagesResponse

  @POST("/send")
  suspend fun sendMessage(@Body sendMessageRequest: SendMessageRequest)
}
