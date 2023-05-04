package com.dicoding.picodiploma.testingwireless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences
import com.dicoding.picodiploma.testingwireless.ViewModel.LoginViewModel
import com.dicoding.picodiploma.testingwireless.ViewModel.LoginViewModelFactory
import com.dicoding.picodiploma.testingwireless.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var preferences: AuthPreferences
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        LoginViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = AuthPreferences(this)

        initAction()

        binding.tvToRegister.setOnClickListener {
            val myIntent = Intent(this, RegisterActivity::class.java)
            startActivity(myIntent)
            finish()
        }

    }

    private fun initAction() {
        binding.btnLogin.setOnClickListener {
            handle()
        }
    }

    fun handle() {
        viewModel.getLogin(
            email = binding.etEmail.text.toString(),
            password = binding.etPass.text.toString()
        ).observe(this, { response ->
            if (response != null) {
                Log.i("isi response", response.toString())
                when (response) {
                    is com.dicoding.picodiploma.testingwireless.utils.Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is com.dicoding.picodiploma.testingwireless.utils.Result.Success -> {
                        binding.progressBar.visibility = View.GONE

//                        val user = User(
//                            response.data.loginResult.email,
//                            response.data.loginResult.password,
//                            response.data.loginResult.nama,
//                            response.data.loginResult.nim,
//                            response.data.loginResult.userId
//                        )
//                        preferences.setUser(user)
                        preferences.setStatusLogin(true)
                        startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                        finish()
                        Toast.makeText(applicationContext, "Login Berhasil", Toast.LENGTH_SHORT)
                            .show()

                    }
                    is com.dicoding.picodiploma.testingwireless.utils.Result.Failure -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Login Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}