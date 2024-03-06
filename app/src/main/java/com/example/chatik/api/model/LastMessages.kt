package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LastMessages(
  @Json(name = "username") val username: String,
  @Json(name = "password") val password: String,
  @Json(name = "last_messages") val lastMessages: List<Message>
)