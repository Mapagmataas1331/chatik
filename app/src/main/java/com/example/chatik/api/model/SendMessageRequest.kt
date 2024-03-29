package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
  @Json(name = "username") val username: String,
  @Json(name = "password") val password: String,
  @Json(name = "to") val to: Int,
  @Json(name = "message") val message: String
)