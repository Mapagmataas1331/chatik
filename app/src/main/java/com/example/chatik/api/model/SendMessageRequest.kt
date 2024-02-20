package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
  @Json(name = "from") val from: Int,
  @Json(name = "to") val to: Int,
  @Json(name = "message") val message: String
)