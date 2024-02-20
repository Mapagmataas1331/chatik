package com.example.chatik.api.request

import com.example.chatik.api.model.LoginRequest
import com.example.chatik.api.model.RegistrationRequest
import com.example.chatik.api.model.UserID
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
  @POST("/register")
  suspend fun registerUser(@Body registrationRequest: RegistrationRequest): UserID

  @POST("/login")
  suspend fun loginUser(@Body loginRequest: LoginRequest): UserID
}
