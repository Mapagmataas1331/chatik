package com.example.chatik.api.request

import com.example.chatik.api.model.LoginRequest
import com.example.chatik.api.model.LoginResponse
import com.example.chatik.api.model.RegistrationRequest
import com.example.chatik.api.model.RegistrationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
  @POST("/register")
  suspend fun registerUser(@Body registrationRequest: RegistrationRequest): RegistrationResponse

  @POST("/login")
  suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse
}
