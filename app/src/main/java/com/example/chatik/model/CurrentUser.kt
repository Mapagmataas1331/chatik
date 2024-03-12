package com.example.chatik.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentUser(
  @Json(name = "id") val id: Int,
  @Json(name = "username") val username: String,
  @Json(name = "password") val password: String
)