package com.dicoding.picodiploma.testingwireless.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.picodiploma.testingwireless.Model.*
import com.dicoding.picodiploma.testingwireless.Network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception

class UserRepository(private val apiService: ApiService) {
//    fun userLogin(
//        email: String,
//        password: String,
//
//        ): LiveData<com.dicoding.picodiploma.testingwireless.utils.Result<Auth>> =
//        liveData {
//            emit(com.dicoding.picodiploma.testingwireless.utils.Result.Loading)
//            val responseLogin = apiService.loginUser(User(email,password))
////            try {
////                if (responseLogin.status==1){
////                    emit(com.dicoding.picodiploma.testingwireless.utils.Result.Success(responseLogin))
////                } else {
////                    emit(com.dicoding.picodiploma.testingwireless.utils.Result.Failure(responseLogin.message))
////                }
////            } catch (e: HttpException) {
////                e.printStackTrace()
////                emit(com.dicoding.picodiploma.testingwireless.utils.Result.Failure(e.message.toString()))
////            }
//        }

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
    fun getMarker(): LiveData<com.dicoding.picodiploma.testingwireless.utils.Result<Location>> =
        liveData {
            emit(com.dicoding.picodiploma.testingwireless.utils.Result.Loading)
            try {
                val response = apiService.getLocation()
                emit(com.dicoding.picodiploma.testingwireless.utils.Result.Success(response))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(com.dicoding.picodiploma.testingwireless.utils.Result.Failure(e.message.toString()))
            }
        }



    fun checkIn(
        id_user: String,
        id_wirelessmaps: String,

        ): LiveData<com.dicoding.picodiploma.testingwireless.utils.Result<Check>> =
        liveData {
            emit(com.dicoding.picodiploma.testingwireless.utils.Result.Loading)
            val responseCheck = apiService.checkIn(CheckBody(id_user,id_wirelessmaps))

            try {
                if (responseCheck.status==1){
                    emit(com.dicoding.picodiploma.testingwireless.utils.Result.Success(responseCheck))
                } else {
                    emit(com.dicoding.picodiploma.testingwireless.utils.Result.Failure(responseCheck.message))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(com.dicoding.picodiploma.testingwireless.utils.Result.Failure(e.message.toString()))
            }
        }
    fun checkOut(
        id_user: String,
        id_wirelessmaps: String,

        ): LiveData<com.dicoding.picodiploma.testingwireless.utils.Result<Check>> =
        liveData {
            emit(com.dicoding.picodiploma.testingwireless.utils.Result.Loading)
            val responseCheck = apiService.checkOut(CheckBody(id_user,id_wirelessmaps))

            try {
                if (responseCheck.status==1){
                    emit(com.dicoding.picodiploma.testingwireless.utils.Result.Success(responseCheck))
                } else {
                    emit(com.dicoding.picodiploma.testingwireless.utils.Result.Failure(responseCheck.message))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emit(com.dicoding.picodiploma.testingwireless.utils.Result.Failure(e.message.toString()))
            }
        }
}