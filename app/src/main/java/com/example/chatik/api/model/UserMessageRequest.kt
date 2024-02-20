package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserMessageRequest(
  @Json(name = "user_id") val userId: Int,
  @Json(name = "friend_id") val friendId: Int
)