package com.example.visit_jeju_app.login.retrofit

import com.example.visit_jeju_app.login.model.LoginDto
import com.example.visit_jeju_app.login.model.User
import retrofit2.Call
import retrofit2.http.*

interface INetworkService {
    @POST("")
    fun doInsertUser(@Body user: User?): Call<User>

    @POST("login")
    fun login(@Body loginDto: LoginDto): Call<LoginDto>

    @GET("walking/user/oneUser")
    fun doGetOneUser(@Query("email") email: String?): Call<User>

    @GET("walking/user/oneUserByNick")
    fun doGetOneUserByNickname(@Query("nickname") nickname: String?): Call<User>

    @POST("walking/user/update")
    fun doUpdateUser(@Body user: User?): Call<User>

    @GET("walking/user/delete")
    fun doDeleteUser(@Query("email") email:String?): Call<Int>


}