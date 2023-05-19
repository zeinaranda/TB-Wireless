package com.dicoding.picodiploma.testingwireless.Network

import com.dicoding.picodiploma.testingwireless.Model.Auth
import com.dicoding.picodiploma.testingwireless.Model.History
import com.dicoding.picodiploma.testingwireless.Model.Home
import com.dicoding.picodiploma.testingwireless.Model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    //        @FormUrlEncoded
    @POST("user/login")
    suspend fun loginUser(
        @retrofit2.http.Body user: User?
    ): Auth

    @POST("user/insert")
    suspend fun insertUser(
        @retrofit2.http.Body user: User?
    ): Auth

    @GET("/view/user/home")
    fun getDetailUser(@Query("id_user") userId: String): Call<Home>

    @GET("maps/get_all")
    suspend fun getLocation(
    ): com.dicoding.picodiploma.testingwireless.Model.Location

    @GET("/view/user/riwayat")
    fun getHistory(@Query("id_user") userId: String): Call<History>



    //        @GET("home_user")
//        fun getStudent(@retrofit2.http.Body id_user: Int?): Call<Student>
    @HTTP(method = "GET", path = "home_user", hasBody = true)
    fun getStudent(@retrofit2.http.Body id_user: Int?): Call<Home>
}