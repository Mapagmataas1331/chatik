package com.example.chatik.model

import com.example.chatik.api.model.Message
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Chat(
  @Json(name = "id") val id: Int,
  @Json(name = "username") val username: String,
  @Json(name = "messages") val messages: List<Message>?
)