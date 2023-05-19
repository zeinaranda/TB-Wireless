package com.dicoding.picodiploma.testingwireless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import com.dicoding.picodiploma.testingwireless.ViewModel.RegisterViewModel
import com.dicoding.picodiploma.testingwireless.ViewModel.RegisterViewModelFactory
import com.dicoding.picodiploma.testingwireless.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels {
        RegisterViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Register"
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initAction()

        binding.tvToLogin.setOnClickListener {
            val myIntent = Intent(this, LoginActivity::class.java)
            startActivity(myIntent)
        }
    }

    fun initAction() {
        binding.btnRegister.setOnClickListener {
            register()
        }
    }

    fun register() {

        viewModel.getRegist(
            name = binding.etNama.text.toString(),
            email = binding.etEmail.text.toString(),
            password = binding.etPass.text.toString(),
            nim = binding.etNim.text.toString(),
        ).observe(this, { response ->
            if (response != null) {
                when (response) {
                    is com.dicoding.picodiploma.testingwireless.utils.Result.Loading -> {
                   //     binding.progressBar.visibility = View.VISIBLE
                    }
                    is com.dicoding.picodiploma.testingwireless.utils.Result.Success -> {
                     //   binding.progressBar.visibility = View.GONE
                        val myIntent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        startActivity(myIntent)
                        Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                    }
                    is com.dicoding.picodiploma.testingwireless.utils.Result.Failure -> {
                    //    binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Register Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}