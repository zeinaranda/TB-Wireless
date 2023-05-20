package com.dicoding.picodiploma.testingwireless.Preference

import android.content.Context
import com.dicoding.picodiploma.testingwireless.Model.Check
import com.dicoding.picodiploma.testingwireless.Model.CheckBody
import com.dicoding.picodiploma.testingwireless.Model.User

class AuthPreferences(context: Context) {
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setUser(value: User) {
        val editor = preferences.edit()
        editor.putString(NAME, value.nama)
        editor.putString(NIM, value.nim)
        editor.putString(EMAIL, value.email)
        editor.putString(ID, value.userId)
        editor.apply()
    }

    fun setStatusLogin(value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(LOGIN, value)
        editor.apply()
    }

    fun getStatusLogin(): Boolean {
        return preferences.getBoolean(LOGIN, false)
    }

    fun getId(): String? {
        return preferences.getString(ID, "")
    }

    fun getUser(): User =
        User(
            userId = preferences.getString(ID, "").toString(),
            nama = preferences.getString(NAME, "").toString(),
            nim = preferences.getString(NIM, "").toString(),
            email = preferences.getString(EMAIL, "").toString(),
            password = preferences.getString(PASSWORD, "").toString()
        )

    fun logout() {
        val editor = preferences.edit()
        editor.clear().apply()
    }
    fun setCheck(value: CheckBody) {
        val editor = preferences.edit()
        editor.putString(ID, value.id_user)
        editor.putString(ID_MAPS, value.id_maps)

        editor.apply()
    }

    fun getCheck(): CheckBody =
        CheckBody(
            id_user = preferences.getString(ID, "").toString(),
            id_maps = preferences.getString(ID_MAPS, "").toString(),
        )
    fun setStatusCheck(value: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean(CHECK, value)
        editor.apply()
    }

    fun getStatusCheck(): Boolean {
        return preferences.getBoolean(CHECK, false)
    }

    fun getIdLoc(): String? {
        return preferences.getString(ID_MAPS, "")
    }
    fun checkOut() {
        val editor = preferences.edit()
        editor.clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "prefs_name"
        private const val ID = "id"
        private const val NAME = "nama"
        private const val NIM = "nim"
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val LOGIN = "login"
        private const val CHECK = "check"
        private const val ID_MAPS = "id_wirelessmaps"
    }
}
