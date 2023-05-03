package com.dicoding.picodiploma.testingwireless.utils

import com.dicoding.picodiploma.testingwireless.Network.ApiConfig
import com.dicoding.picodiploma.testingwireless.Repository.UserRepository

object Injection {
    fun provideRepository(): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository(apiService)
    }
}