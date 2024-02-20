package com.example.chatik.api

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
