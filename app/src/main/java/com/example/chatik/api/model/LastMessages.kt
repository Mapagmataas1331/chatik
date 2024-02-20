package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LastMessages(
  @Json(name = "last_messages") val lastMessages: List<Message>
)