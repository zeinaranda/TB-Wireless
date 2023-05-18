package com.dicoding.picodiploma.testingwireless.ViewModel

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.testingwireless.Repository.UserRepository

class MapsViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getMarker() =
        userRepository.getMarker()
}