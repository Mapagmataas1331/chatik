package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LastMessagesRequest(
  @Json(name = "username") val username: String,
  @Json(name = "password") val password: String
)
