package com.example.chatik.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegistrationRequest(
  @Json(name = "username") val username: String,
  @Json(name = "password") val password: String,
  @Json(name = "last_name") val lastName: String?,
  @Json(name = "first_name") val firstName: String?
)

@JsonClass(generateAdapter = true)
data class RegistrationResponse(
  @Json(name = "id") val id: Int
)

@JsonClass(generateAdapter = true)
data class LoginRequest(
  @Json(name = "username") val username: String,
  @Json(name = "password") val password: String
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
  @Json(name = "id") val id: Int
)

@JsonClass(generateAdapter = true)
data class UserIdRequest(
  @Json(name = "user_id") val userId: Int
)

@JsonClass(generateAdapter = true)
data class LastMessagesResponse(
  @Json(name = "last_messages") val lastMessages: List<Message>
)

@JsonClass(generateAdapter = true)
data class UserMessageRequest(
  @Json(name = "user_id") val userId: Int,
  @Json(name = "friend_id") val friendId: Int
)

@JsonClass(generateAdapter = true)
data class UserMessagesResponse(
  @Json(name = "messages_list") val messagesList: List<Message>
)

@JsonClass(generateAdapter = true)
data class SendMessageRequest(
  @Json(name = "from") val from: Int,
  @Json(name = "to") val to: Int,
  @Json(name = "message") val message: String
)

@JsonClass(generateAdapter = true)
data class SearchRequest(
  @Json(name = "search_string") val searchString: String
)

@JsonClass(generateAdapter = true)
data class SearchResponse(
  @Json(name = "search_users") val searchUsers: List<User>
)

@JsonClass(generateAdapter = true)
data class UserInfoResponse(
  @Json(name = "id") val id: Int,
  @Json(name = "username") val username: String,
  @Json(name = "last_name") val lastName: String,
  @Json(name = "first_name") val firstName: String
)

@JsonClass(generateAdapter = true)
data class Message(
  @Json(name = "id") val id: Int,
  @Json(name = "message") val message: String,
  @Json(name = "from") val from: User,
  @Json(name = "to") val to: User,
  @Json(name = "datetime") val datetime: String
)

@JsonClass(generateAdapter = true)
data class User(
  @Json(name = "id") val id: Int,
  @Json(name = "username") val username: String,
  @Json(name = "last_name") val lastName: String,
  @Json(name = "first_name") val firstName: String
)
