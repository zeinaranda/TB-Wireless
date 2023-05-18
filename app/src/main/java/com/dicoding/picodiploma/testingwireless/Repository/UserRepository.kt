package com.dicoding.picodiploma.testingwireless.Repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.testingwireless.Model.Auth
import com.dicoding.picodiploma.testingwireless.Model.User
import com.dicoding.picodiploma.testingwireless.Network.ApiService
import java.lang.Exception

class UserRepository(private val apiService: ApiService) {
    fun userLogin(
        email: String,
        password: String,

        ): LiveData<com.dicoding.picodiploma.testingwireless.utils.Result<Auth>> =
        liveData {
            emit(com.dicoding.picodiploma.testingwireless.utils.Result.Loading)
            val responseLogin = apiService.loginUser(User(email,password))

            try {
                if (responseLogin.status==1){
                    emit(com.dicoding.picodiploma.testingwireless.utils.Result.Success(responseLogin))
                } else {
                    emit(com.dicoding.picodiploma.testingwireless.utils.Result.Failure(responseLogin.message))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(com.dicoding.picodiploma.testingwireless.utils.Result.Failure(e.message.toString()))
            }
        }

    fun userRegist(
        nama: String,
        email: String,
        password: String,
        nim : String,
    ): LiveData<com.dicoding.picodiploma.testingwireless.utils.Result<Auth>> =
        liveData {
            emit(com.dicoding.picodiploma.testingwireless.utils.Result.Loading)
            try {
                val response = apiService.insertUser(User(email,password, nama, nim))
                emit(com.dicoding.picodiploma.testingwireless.utils.Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(com.dicoding.picodiploma.testingwireless.utils.Result.Failure(e.message.toString()))
            }
        }
}