package com.dicoding.picodiploma.testingwireless.Model

import com.google.gson.annotations.SerializedName

data class Auth (
    val loginResult: User,
    val msg: String,
    val status: Boolean,
    val token: String,
    val id_user: Int
    )

    data class User(
        @field:SerializedName("email")
        var email: String,

        @field:SerializedName("password")
        var password: String,

        @field:SerializedName("nama")
        var nama: String? = null,

        @field:SerializedName("nim")
        var nim: String? = null,

        @field:SerializedName("id_user")
        var userId: String? = null,
    )