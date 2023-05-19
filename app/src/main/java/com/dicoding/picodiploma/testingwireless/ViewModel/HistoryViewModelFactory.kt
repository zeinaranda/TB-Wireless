package com.dicoding.picodiploma.testingwireless.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences

class HistoryViewModelFactory (private val pref: AuthPreferences): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)){
            return HistoryViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown VM Class: " + modelClass.name)
    }
}