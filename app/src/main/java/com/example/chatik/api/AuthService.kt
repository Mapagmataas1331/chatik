package com.example.chatik.api

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
  @POST("/register")
  suspend fun registerUser(@Body registrationRequest: RegistrationRequest): RegistrationResponse

  @POST("/login")
  suspend fun loginUser(@Body loginRequest: LoginRequest): LoginResponse
}
