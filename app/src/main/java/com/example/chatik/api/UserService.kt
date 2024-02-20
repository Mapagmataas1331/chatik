package com.example.chatik.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
  @POST("/search")
  suspend fun searchUsers(@Body searchRequest: SearchRequest): SearchResponse

  @GET("/user/{username}")
  suspend fun getUserInfo(@Path("username") username: String): UserInfoResponse
}
