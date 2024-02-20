package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationRequest(
  @Json(name = "username") val username: String,
  @Json(name = "password") val password: String,
  @Json(name = "last_name") val lastName: String?,
  @Json(name = "first_name") val firstName: String?
)