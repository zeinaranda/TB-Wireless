package com.dicoding.picodiploma.testingwireless.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.testingwireless.Model.Auth
import com.dicoding.picodiploma.testingwireless.Model.User
import com.dicoding.picodiploma.testingwireless.Network.ApiConfig
import com.dicoding.picodiploma.testingwireless.utils.ConsumableValue
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel : ViewModel() {
    private val _result = MutableLiveData<Auth>()
    val result : LiveData<Auth> get() = _result

    private val _isLoading = MutableLiveData<ConsumableValue<Boolean>>()
    val isLoading: LiveData<ConsumableValue<Boolean>> = _isLoading

    private val _error = MutableLiveData<ConsumableValue<Int>>()
    val error: LiveData<ConsumableValue<Int>> = _error

    fun loginUser(email: String, password: String) {
        _isLoading.value = ConsumableValue(true)
        val client =  ApiConfig.getApiService().loginUser(User(email,password))
        viewModelScope.launch {
            try {
                client.enqueue(object : Callback<Auth> {
                    override fun onResponse(
                        call: Call<Auth>,
                        response: Response<Auth>
                    ) {
                        _isLoading.value = ConsumableValue(true)
                        if (response.isSuccessful){
                            if (response.body() != null){
                                val responseBody = response.body()
                                _result.value = responseBody!!
                                _error.postValue(ConsumableValue(response.body()!!.status))
                                Log.i("request_success", response.body().toString())
                            } else {
                                _error.postValue(ConsumableValue(response.body()!!.status))
                                Log.i("response_failed", response.body().toString())
                            }
                        } else {
                            _error.postValue(ConsumableValue(0))
                            Log.i("response_failed", "0")
                            _isLoading.value = ConsumableValue(false)
                        }
                    }
                    override fun onFailure(call: Call<Auth>, t: Throwable) {
                        _isLoading.value = ConsumableValue(true)
                        Log.d("Failed", t.message.toString())
                        _isLoading.value = ConsumableValue(false)
                    }
                })
            } catch (e: Exception) {
                Log.i("yahaha","hayuk")
                val errorMessage = "Login failed: ${e.message}"
                _result.value!!.message = errorMessage
                Log.e("LoginViewModel", errorMessage, e)
            }
        }
    }
    fun getLogin(): LiveData<Auth> {
        return result
    }
}