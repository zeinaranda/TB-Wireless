package com.dicoding.picodiploma.testingwireless.Model

import com.google.gson.annotations.SerializedName

class History (
    @field:SerializedName("status")
    val status: Int,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("data")
    val items: List<ItemsItem>
)

data class ItemsItem(
    @field:SerializedName("id_maps")
    val id: String? = null,

    @field:SerializedName("Nama_Program_Studi")
    val jurusan: String? = null,

    @field:SerializedName("latitude")
    val latitude: String? = null,

    @field:SerializedName("longitude")
    val longitude: String? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("kategori")
    val kategori: String? = null,

    @field:SerializedName("keterangan")
    val keterangan: String? = null,

    @field:SerializedName("waktu")
    val tanggal: String? = null,

    )