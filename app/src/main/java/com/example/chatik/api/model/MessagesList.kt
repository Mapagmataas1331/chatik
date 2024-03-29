package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessagesList(
  @Json(name = "messages_list") val messagesList: List<Message>
)