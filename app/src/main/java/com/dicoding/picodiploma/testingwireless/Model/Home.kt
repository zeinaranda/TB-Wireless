package com.dicoding.picodiploma.testingwireless.Model

import com.google.gson.annotations.SerializedName

data class Home(


    @field:SerializedName("restaurant")
    val body: Body,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

annotation class Body(


    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("nim")
    val nim: String,

    @field:SerializedName("id_user")
    val id: String
)


