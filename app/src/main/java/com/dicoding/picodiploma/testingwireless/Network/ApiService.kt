package com.dicoding.picodiploma.testingwireless.Network

import com.dicoding.picodiploma.testingwireless.Model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    //        @FormUrlEncoded
    @POST("user/login")
    fun loginUser(
        @retrofit2.http.Body user: User?
    ): Call<Auth>

    @POST("user/insert")
    suspend fun insertUser(
        @retrofit2.http.Body user: User?
    ): Auth

    @GET("/view/user/home")
    fun getDetailUser(@Query("id_user") userId: String): Call<Home>

    @GET("maps/get_all")
    suspend fun getLocation(
    ): Location

    @GET("maps/get_online_user")
    fun getOnlineUsers(): Call<Online>

    @GET("/view/user/riwayat")
    fun getHistory(@Query("id_user") userId: String): Call<History>

    @POST("user/checkin")
    suspend fun checkIn(
        @retrofit2.http.Body check: CheckBody?
    ): Check

    @POST("user/checkout")
    suspend fun checkOut(
        @retrofit2.http.Body check: CheckBody?
    ): Check

    //        @GET("home_user")
//        fun getStudent(@retrofit2.http.Body id_user: Int?): Call<Student>
    @HTTP(method = "GET", path = "home_user", hasBody = true)
    fun getStudent(@retrofit2.http.Body id_user: Int?): Call<Home>
}