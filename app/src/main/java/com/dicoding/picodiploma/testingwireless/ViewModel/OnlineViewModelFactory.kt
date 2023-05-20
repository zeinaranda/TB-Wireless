package com.dicoding.picodiploma.testingwireless.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences

class OnlineViewModelFactory (private val pref: AuthPreferences): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnlineViewModel::class.java)){
            return OnlineViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown VM Class: " + modelClass.name)
    }
}