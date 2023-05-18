package com.dicoding.picodiploma.testingwireless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences
import com.dicoding.picodiploma.testingwireless.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView

class HistoryActivity : AppCompatActivity() {
    lateinit var toggle : ActionBarDrawerToggle
    private lateinit var preferences: AuthPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout : DrawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        toolbar.title = "Riwayat Absensi"
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        preferences = AuthPreferences(this)

        navView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_home -> {
                    val i = Intent(this, HomeActivity::class.java)
                    startActivity(i)
                    finish()
                    true
                }
                R.id.nav_history -> {
                    drawerLayout.closeDrawer(navView)
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
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}