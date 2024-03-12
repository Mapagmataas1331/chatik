package com.example.chatik.api.request

import com.example.chatik.api.model.SearchString
import com.example.chatik.api.model.User
import com.example.chatik.api.model.SearchUsers
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Интерфейс UserService для выполнения запросов к сервису пользователей
interface UserService {
  // Метод для поиска пользователей по строке запроса
  @POST("/search")
  suspend fun searchUsers(@Body searchString: SearchString): SearchUsers

  // Метод для получения информации о пользователе по имени пользователя
  @GET("/user/{username}")
  suspend fun getUserInfo(@Path("username") username: String): User
}
