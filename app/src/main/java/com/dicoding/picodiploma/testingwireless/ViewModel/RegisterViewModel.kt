package com.dicoding.picodiploma.testingwireless.ViewModel

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.testingwireless.Repository.UserRepository

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getRegist(name: String, email: String, password: String, nim: String) =
        userRepository.userRegist(name, email, password, nim)
}