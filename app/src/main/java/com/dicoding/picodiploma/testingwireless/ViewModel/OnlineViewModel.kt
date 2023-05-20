package com.dicoding.picodiploma.testingwireless.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.testingwireless.Model.History
import com.dicoding.picodiploma.testingwireless.Model.ItemsItem
import com.dicoding.picodiploma.testingwireless.Model.Online
import com.dicoding.picodiploma.testingwireless.Model.OnlineItem
import com.dicoding.picodiploma.testingwireless.Network.ApiConfig
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OnlineViewModel (private val pref: AuthPreferences) : ViewModel() {

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> = _error

    private val _load = MutableLiveData<Boolean>()
    val load: LiveData<Boolean> = _load

    private val _list = MutableLiveData<List<OnlineItem>>()
    val list: LiveData<List<OnlineItem>> = _list

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun setOnlineUsers(){
        _isLoading.value =true
        val client =  ApiConfig.getApiService().getOnlineUsers()
        client.enqueue(object : Callback<Online> {
            override fun onResponse(
                call: Call<Online>,
                response: Response<Online>
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

            override fun onFailure(call: Call<Online>, t: Throwable) {
                _isLoading.value = true
                Log.d("Failed", t.message.toString())
                _isLoading.value = false
            }
        })
    }

    fun getOnlineUsers(): LiveData<List<OnlineItem>> {
        return list
    }
}