package com.dicoding.picodiploma.testingwireless.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class Location(
    var data: List<LatLong>
)

@Parcelize
data class LatLong(
    @field:SerializedName("id")
    var id: String,

    @field:SerializedName("nama_program_studi")
    var jurusan: String,

    @field:SerializedName("latitude")
    var latitude: Double,

    @field:SerializedName("longitude")
    var longitude: Double,

    @field:SerializedName("waktu_buat")
    val createdAt: String? = null,

    ) : Parcelable