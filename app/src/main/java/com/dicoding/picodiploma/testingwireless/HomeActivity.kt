package com.dicoding.picodiploma.testingwireless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import com.dicoding.picodiploma.testingwireless.Model.Body
import com.dicoding.picodiploma.testingwireless.Model.CheckBody
import com.dicoding.picodiploma.testingwireless.Model.Home
import com.dicoding.picodiploma.testingwireless.Network.ApiConfig
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences
import com.dicoding.picodiploma.testingwireless.ViewModel.HomeViewModel
import com.dicoding.picodiploma.testingwireless.ViewModel.HomeViewModelFactory
import com.dicoding.picodiploma.testingwireless.data.DialogType
import com.dicoding.picodiploma.testingwireless.databinding.ActivityHomeBinding
import com.dicoding.picodiploma.testingwireless.dialog.PopupDialog
import com.google.android.material.navigation.NavigationView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class HomeActivity : AppCompatActivity() {

    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding: ActivityHomeBinding
    private lateinit var preferences: AuthPreferences
    private lateinit var userId: String
    private lateinit var mapsId: String
    private var statusCheck by Delegates.notNull<Boolean>()
    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory.getInstance(this)
    }

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toolbar.title = "Home"
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    drawerLayout.closeDrawer(navView)
                    true
                }
                R.id.nav_history -> {
                    drawerLayout.closeDrawer(navView)
                    val i = Intent(this, HistoryActivity::class.java)
                    startActivity(i)
                    true
                }
                R.id.nav_online -> {
                    drawerLayout.closeDrawer(navView)
                    val i = Intent(this, OnlineActivity::class.java)
                    startActivity(i)
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
        preferences.setStatusCheck(false)
        userId = preferences.getId()!!
        if (preferences.getUser().status == "Online"){
            preferences.setStatusCheck(true)
        }
        Log.i("checkya",preferences.getStatusCheck().toString())
        mapsId = preferences.getIdLoc()!!

        findRestaurant(userId)

        binding.checkIn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        binding.checkOut.setOnClickListener {
            statusCheck = preferences.getStatusCheck()
            if (statusCheck) {
                checkOut()
            } else {
                showDialog(DialogType.ERROR,"Anda Belum Check In")
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Keluar dari Aplikasi")
        builder.setMessage("Apakah Anda yakin ingin keluar?")
        builder.setPositiveButton("Ya") { _, _ ->
            super.onBackPressed()
            finish()
        }
        builder.setNegativeButton("Tidak", null)
        val dialog = builder.create()
        dialog.show()
    }


    private fun findRestaurant(id : String) {
        val client = ApiConfig.getApiService().getDetailUser(id)
        client.enqueue(object : Callback<Home> {
            override fun onResponse(
                call: Call<Home>,
                response: Response<Home>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.i("responsehome",responseBody.toString())
                    if (responseBody != null) {
                        binding.tvNama.text= responseBody.data?.nama ?: preferences.getUser().nama
                        binding.tvNim.text= responseBody.data?.nim ?: preferences.getUser().nim
                        val idmaps = responseBody.data?.id_maps
                        if (idmaps != null){
                            mapsId = idmaps
                            userId = preferences.getId().toString()
                            val check = CheckBody(
                                userId,
                                mapsId
                            )
                            preferences.setCheck(check)
                            Log.i("check",preferences.getIdLoc().toString())
                        }
                        val status = responseBody.data?.status
                        if (status == "Online") {
                            preferences.setStatusCheck(true)
                        } else if (status == "Offline") {
                            preferences.setStatusCheck(false)
                        }
                        Log.i("check1",preferences.getIdLoc().toString())
                        Log.i("checkstatus",status.toString())
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

    private fun moveToMainActivity() {
            if (preferences.getStatusCheck()) {
                preferences.checkOut()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
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
    private fun checkOut() {
        viewModel.getCheckOut( id_user = userId, id_wirelessmaps = mapsId)
            .observe(this) { response ->
                if (response != null) {
                    when (response) {
                        is com.dicoding.picodiploma.testingwireless.utils.Result.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is com.dicoding.picodiploma.testingwireless.utils.Result.Success -> {
                            binding.progressBar.visibility = View.GONE
                            preferences.checkOut()
                            preferences.setStatusCheck(false)
                            preferences.setStatus("Offline")
                            Log.i("check3",preferences.getIdLoc().toString())
                            statusCheck = preferences.getStatusCheck()
                            showDialog(DialogType.SUCCESS,"Anda Telah Check Out")
                            Toast.makeText(
                                applicationContext,
                                "Check Out Berhasil",
                                Toast.LENGTH_SHORT
                            )
                                .show()
//                            preferences.getCheck().id_maps
                        }

                        is com.dicoding.picodiploma.testingwireless.utils.Result.Failure -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                            showDialog(DialogType.ERROR,response.message)
                        }
                    }
                }
            }
    }

}