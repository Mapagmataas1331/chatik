package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Message(
  @Json(name = "id") val id: Int,
  @Json(name = "message") val message: String,
  @Json(name = "from") val from: User,
  @Json(name = "to") val to: User,
  @Json(name = "datetime") val datetime: String
)