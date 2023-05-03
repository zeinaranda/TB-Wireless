package com.dicoding.picodiploma.testingwireless.ViewModel

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.testingwireless.Repository.UserRepository

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    fun getLogin(email: String, password: String) = userRepository.userLogin(email, password)
}