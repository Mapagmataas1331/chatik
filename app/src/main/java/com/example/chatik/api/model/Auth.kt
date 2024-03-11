package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Auth(
  @Json(name = "id") val id: Int,
  @Json(name = "username") val username: String,
  @Json(name = "password") val password: String
)