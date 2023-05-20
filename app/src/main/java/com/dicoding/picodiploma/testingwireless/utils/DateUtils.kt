package com.dicoding.picodiploma.testingwireless.utils
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun formatDate(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale("id","ID"))
        val outputFormat = SimpleDateFormat("EEEE, d MMMM yyyy : HH:mm", Locale("id","ID"))

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }
}



