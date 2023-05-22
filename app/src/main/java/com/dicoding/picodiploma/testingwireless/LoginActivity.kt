package com.dicoding.picodiploma.testingwireless

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import com.dicoding.picodiploma.testingwireless.Model.User
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences
import com.dicoding.picodiploma.testingwireless.ViewModel.LoginViewModel
import com.dicoding.picodiploma.testingwireless.ViewModel.LoginViewModelFactory
import com.dicoding.picodiploma.testingwireless.data.DialogType
import com.dicoding.picodiploma.testingwireless.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.testingwireless.dialog.PopupDialog

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
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.title = "Login"
        setSupportActionBar(toolbar)

        preferences = AuthPreferences(this)

        initAction()


        binding.tvToRegister.setOnClickListener {
            val myIntent = Intent(this, RegisterActivity::class.java)
            startActivity(myIntent)
        }

    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Keluar dari Aplikasi")
        builder.setMessage("Apakah Anda yakin ingin keluar?")
        builder.setPositiveButton("Ya") { _, _ ->
            super.onBackPressed()
        }
        builder.setNegativeButton("Tidak", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun initAction() {
        binding.btnLogin.setOnClickListener {
            Log.i("kepencet","kan")
            handle()
        }
    }

    fun handle() {
        viewModel.loginUser(email = binding.etEmail.text.toString(),
            password = binding.etPass.text.toString())
        viewModel.getLogin().observe(this) {
            Log.i("aa",it.toString())
            if (it != null) {
                    Log.i("status",it.status.toString())
                    val user = User(
                        it.data.email,
                        it.data.password,
                        it.data.nama,
                        it.data.nim,
                        it.data.userId,
                        it.data.status,
                    )
                    preferences.setUser(user)
                    preferences.setStatusLogin(true)
                    val savedUser = preferences.setUser(user)
                    Log.i("savedUser", savedUser.toString())
                    startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                    finish()
                    Toast.makeText(applicationContext, "Login Berhasil", Toast.LENGTH_SHORT)
                        .show()
            }
        }
        viewModel.error.observe(this){ error ->
            error.handle { showDialog(DialogType.ERROR,"Email atau Password Salah") }
        }
        viewModel.isLoading.observe(this) { loading ->
            if (isNetworkAvailable()) {
                loading.handle { showLoading(it) }
            } else {
                loading.handle { showLoading(false) }
                Toast.makeText(applicationContext, "Tidak ada koneksi internet", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    // Metode untuk memeriksa ketersediaan koneksi internet
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun showDialog(type: DialogType, msg:String) {
        val callback = object : PopupDialog.DialogCallback {
            override fun dismissDialog(dialog: DialogFragment) {
                // Tindakan yang ingin dilakukan ketika dialog ditutup
                dialog.dismiss()
            }
        }
        val dialogFragment = PopupDialog(type, msg, callback)
        dialogFragment.show(supportFragmentManager,"PopUpDialog")
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}