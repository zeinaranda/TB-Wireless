package com.dicoding.picodiploma.testingwireless.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Auth (
    var status : Int,
    var message: String,
    var data : User
)

@Parcelize
data class User(
    @field:SerializedName("email")
    var email: String,

    @field:SerializedName("password")
    var password: String,

    @field:SerializedName("nama")
    var nama: String? = null,

    @field:SerializedName("nim")
    var nim: String? = null,

    @field:SerializedName("id")
    var userId: String? = null,

    @field:SerializedName("status")
    var status: String? = null,
) : Parcelable