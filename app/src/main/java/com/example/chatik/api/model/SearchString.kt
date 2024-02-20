package com.example.chatik.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchString(
  @Json(name = "search_string") val searchString: String
)