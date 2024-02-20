package com.example.chatik.api.request

import com.example.chatik.api.model.SearchString
import com.example.chatik.api.model.User
import com.example.chatik.api.model.SearchUsers
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {
  @POST("/search")
  suspend fun searchUsers(@Body searchString: SearchString): SearchUsers

  @GET("/user/{username}")
  suspend fun getUserInfo(@Path("username") username: String): User
}
