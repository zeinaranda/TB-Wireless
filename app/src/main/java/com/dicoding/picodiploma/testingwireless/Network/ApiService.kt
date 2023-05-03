package com.dicoding.picodiploma.testingwireless.Network

import com.dicoding.picodiploma.testingwireless.Model.Auth
import com.dicoding.picodiploma.testingwireless.Model.Home
import com.dicoding.picodiploma.testingwireless.Model.User
import retrofit2.Call
import retrofit2.http.HTTP
import retrofit2.http.POST

interface ApiService {
    //        @FormUrlEncoded
    @POST("login_user")
    suspend fun loginUser(
        @retrofit2.http.Body user: User?
    ): Auth

    @POST("insert_user")
    suspend fun insertUser(
        @retrofit2.http.Body user: User?
    ): Auth

    //        @GET("home_user")
//        fun getStudent(@retrofit2.http.Body id_user: Int?): Call<Student>
    @HTTP(method = "GET", path = "home_user", hasBody = true)
    fun getStudent(@retrofit2.http.Body id_user: Int?): Call<Home>
}