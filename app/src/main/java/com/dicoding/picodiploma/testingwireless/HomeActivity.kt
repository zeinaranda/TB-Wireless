package com.dicoding.picodiploma.testingwireless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.dicoding.picodiploma.testingwireless.Model.Body
import com.dicoding.picodiploma.testingwireless.Model.Home
import com.dicoding.picodiploma.testingwireless.Network.ApiConfig
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences
import com.dicoding.picodiploma.testingwireless.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding: ActivityHomeBinding
    private lateinit var preferences: AuthPreferences
    private lateinit var userId: String

    companion object {
        private const val TAG = "MainActivity"
        private const val RESTAURANT_ID = "4"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    val i = Intent(this, HomeActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.nav_history -> {
                    val i = Intent(this, HistoryActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.nav_logout -> {
                    preferences.logout()
                    val i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
            }

            true
        }

        preferences = AuthPreferences(this)
        userId = preferences.getId()!!
        findRestaurant(userId)

//        binding.history.setOnClickListener {
//            getLogout()
//            this.finish()
//        }
//        binding.history.setOnClickListener {
//            val intent = Intent(this, HistoryActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun findRestaurant(userId : String) {
        val client = ApiConfig.getApiService().getDetailUser(userId)
        client.enqueue(object : Callback<Home> {
            override fun onResponse(
                call: Call<Home>,
                response: Response<Home>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {

                        binding.tvNama.text=preferences.getUser().nama
                        binding.tvNim.text=preferences.getUser().nim
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Home>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }


    private fun setRestaurantData(restaurant: Body?) {
        if (restaurant != null) {
            binding.tvNama.text = restaurant.nama
            binding.tvNim.text = restaurant.nim
        }
    }
    private fun getLogout() {
        preferences.logout()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}