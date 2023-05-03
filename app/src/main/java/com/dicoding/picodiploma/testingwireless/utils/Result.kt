package com.dicoding.picodiploma.testingwireless.utils

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Failure(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
}