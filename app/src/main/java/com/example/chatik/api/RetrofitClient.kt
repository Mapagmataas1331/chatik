package com.example.chatik.api

import com.example.chatik.api.request.AuthService
import com.example.chatik.api.request.MessageService
import com.example.chatik.api.request.UserService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// Объект RetrofitClient, отвечающий за создание экземпляров сервисов Retrofit
object RetrofitClient {
  // Базовый URL для всех сервисов
  private const val BASE_URL = "http://109.196.164.62:5000"

  // Функция для создания экземпляра AuthService
  fun createAuthService(): AuthService {
    // Создание экземпляра Retrofit с базовым URL и конвертером Moshi
    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    // Создание и возвращение сервиса AuthService с помощью Retrofit
    return retrofit.create(AuthService::class.java)
  }

  // Функция для создания экземпляра MessageService
  fun createMessageService(): MessageService {
    // Создание экземпляра Retrofit с базовым URL и конвертером Moshi
    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    // Создание и возвращение сервиса MessageService с помощью Retrofit
    return retrofit.create(MessageService::class.java)
  }

  // Функция для создания экземпляра UserService
  fun createUserService(): UserService {
    // Создание экземпляра Retrofit с базовым URL и конвертером Moshi
    val retrofit = Retrofit.Builder()
      .baseUrl(BASE_URL)
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

    // Создание и возвращение сервиса UserService с помощью Retrofit
    return retrofit.create(UserService::class.java)
  }
}
