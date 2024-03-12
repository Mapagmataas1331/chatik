package com.example.chatik.api.request

import com.example.chatik.api.model.SendMessageRequest
import com.example.chatik.api.model.LastMessages
import com.example.chatik.api.model.MessagesList
import com.example.chatik.api.model.LastMessagesRequest
import com.example.chatik.api.model.UserMessagesRequest
import retrofit2.http.Body
import retrofit2.http.POST

// Интерфейс MessageService для выполнения запросов к сервису сообщений
interface MessageService {
  // Метод для получения последних сообщений
  @POST("/message")
  suspend fun getLastMessages(@Body lastMessageRequest: LastMessagesRequest): LastMessages

  // Метод для получения сообщений пользователя
  @POST("/user-message")
  suspend fun getUserMessages(@Body userMessagesRequest: UserMessagesRequest): MessagesList

  // Метод для отправки сообщения
  @POST("/send")
  suspend fun sendMessage(@Body sendMessageRequest: SendMessageRequest)
}
