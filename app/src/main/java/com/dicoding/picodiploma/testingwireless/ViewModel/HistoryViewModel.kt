package com.dicoding.picodiploma.testingwireless.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.testingwireless.Model.History
import com.dicoding.picodiploma.testingwireless.Model.ItemsItem
import com.dicoding.picodiploma.testingwireless.Network.ApiConfig
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel (private val pref: AuthPreferences) : ViewModel() {

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    private val _load = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _load

    private val _list = MutableLiveData<List<ItemsItem>>()
    val list: LiveData<List<ItemsItem>> = _list

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setStories(userId: String){
        _isLoading.value =true
        val client =  ApiConfig.getApiService().getHistory(userId)
        client.enqueue(object : Callback<History> {
            override fun onResponse(
                call: Call<History>,
                response: Response<History>
            ) {
                _isLoading.value = true
                if (response.isSuccessful){
                    if (response.body() != null){

                        _list.value = response.body()?.items
                        _error.postValue(response.body()?.status)
                        Log.i("request_success", response.body().toString())
                    } else {
                        _error.postValue(response.body()?.status)
                        Log.i("response_failed", response.body().toString())
                    }
                }
                _isLoading.value = false
            }

            override fun onFailure(call: Call<History>, t: Throwable) {
                _isLoading.value = true
                Log.d("Failed", t.message.toString())
                _isLoading.value = false
            }
        })
    }

    fun getStories(): LiveData<List<ItemsItem>> {
        return list
    }
}