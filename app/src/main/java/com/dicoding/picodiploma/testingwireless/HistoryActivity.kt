package com.dicoding.picodiploma.testingwireless

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.testingwireless.Adapter.HistoryAdapter
import com.dicoding.picodiploma.testingwireless.Model.ItemsItem
import com.dicoding.picodiploma.testingwireless.Preference.AuthPreferences
import com.dicoding.picodiploma.testingwireless.ViewModel.HistoryViewModel
import com.dicoding.picodiploma.testingwireless.ViewModel.HistoryViewModelFactory
import com.dicoding.picodiploma.testingwireless.databinding.ActivityHistoryBinding
import java.util.ArrayList
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.dicoding.picodiploma.testingwireless.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView

class HistoryActivity : AppCompatActivity() {
    private lateinit var preferences: AuthPreferences
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter
    private val list = ArrayList<ItemsItem>()
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        toolbar.title = "Riwayat Absensi"
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = AuthPreferences(this)
        adapter = HistoryAdapter(list)
        adapter.notifyDataSetChanged()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
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

        viewModel = ViewModelProvider(this, HistoryViewModelFactory(preferences)).get(
            HistoryViewModel::class.java
        )

        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(this@HistoryActivity).apply {
                reverseLayout = true
            }
            rvUsers.adapter = adapter
        }

        userId = preferences.getId()
        viewModel.setStories(userId!!)
        viewModel.getStories().observe(this, {
            if (it != null) {
                adapter.setList(it.sortedBy { it.tanggal })
            }
        })
        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStories(userId!!)
    }
}
