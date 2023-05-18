package com.dicoding.picodiploma.testingwireless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat.startActivity
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences
import com.dicoding.picodiploma.testingwireless.databinding.ActivitySplashBinding
import com.dicoding.picodiploma.testingwireless.utils.Constant

class SplashActivity : AppCompatActivity() {
    private lateinit var pref: AuthPreferences
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        moveToMainActivity()

//        Handler().postDelayed({
//            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent) // Pindah ke Home Activity setelah 3 detik
//            finish()
//        }, Constant.SPLASH_TIME)
//
    }


    private fun moveToMainActivity() {
        pref = AuthPreferences(this)
        Handler(Looper.getMainLooper()).postDelayed({
            if (pref.getStatusLogin()) {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }, Constant.SPLASH_TIME)
    }
}