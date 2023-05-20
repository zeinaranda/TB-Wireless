package com.dicoding.picodiploma.testingwireless.Model

import com.google.gson.annotations.SerializedName

class Online (
    @field:SerializedName("status")
    val status: Int,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val items: List<OnlineItem>
)

data class OnlineItem(
    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("Nama_Program_Studi")
    val jurusan: String? = null,

    @field:SerializedName("latitude")
    val latitude: String? = null,

    @field:SerializedName("longitude")
    val longitude: String? = null,

    @field:SerializedName("nim")
    val nim: String? = null,

    @field:SerializedName("waktu")
    val waktu: String? = null,

    )