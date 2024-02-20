package com.example.chatik.api

import com.example.chatik.api.request.AuthService
import com.example.chatik.api.request.MessageService
import com.example.chatik.api.request.UserService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitClient {
  private const val BASE_URL = "http://109.196.164.62:5000"

  fun createAuthService(): AuthService {
    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    return retrofit.create(AuthService::class.java)
  }

  fun createMessageService(): MessageService {
    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    return retrofit.create(MessageService::class.java)
  }

  fun createUserService(): UserService {
    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    return retrofit.create(UserService::class.java)
  }
}
