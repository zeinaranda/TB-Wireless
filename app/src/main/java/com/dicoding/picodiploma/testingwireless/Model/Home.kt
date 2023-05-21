package com.dicoding.picodiploma.testingwireless.Model

import com.google.gson.annotations.SerializedName

data class Home(


    @field:SerializedName("data")
    val data: Body,

    @field:SerializedName("status")
    val status: Int,

    @field:SerializedName("message")
    val message: String
)

data class Body(


    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("nim")
    val nim: String,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("id_user")
    val id: String,

    @field:SerializedName("id_maps")
    var id_maps: String? = null
)


