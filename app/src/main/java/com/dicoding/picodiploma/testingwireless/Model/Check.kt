package com.dicoding.picodiploma.testingwireless.Model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class Check (
    var status : Int,
    var message: String,
    var data : CheckBody
)


data class CheckBody(
    @field:SerializedName("id_user")
    var id_user: String? = null,

    @field:SerializedName("id_wirelessmaps")
    var id_maps: String,

)