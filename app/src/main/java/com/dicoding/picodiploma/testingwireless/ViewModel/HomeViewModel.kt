package com.dicoding.picodiploma.testingwireless.ViewModel

import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.testingwireless.Repository.UserRepository

class HomeViewModel (private val userRepository: UserRepository) : ViewModel() {
    fun getCheckOut(
        id_user: String,
        id_wirelessmaps: String
    ) = userRepository.checkOut(id_user, id_wirelessmaps)

}