package com.example.chatik.api.request

import com.example.chatik.api.model.LoginRequest
import com.example.chatik.api.model.RegistrationRequest
import com.example.chatik.api.model.Id
import retrofit2.http.Body
import retrofit2.http.POST

// Интерфейс AuthService для выполнения запросов к аутентификационному API
interface AuthService {
  // Метод для отправки запроса на регистрацию пользователя
  @POST("/register")
  suspend fun registerUser(@Body registrationRequest: RegistrationRequest): Id

  // Метод для отправки запроса на вход пользователя в систему
  @POST("/login")
  suspend fun loginUser(@Body loginRequest: LoginRequest): Id
}
