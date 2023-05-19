package com.dicoding.picodiploma.testingwireless

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

class HistoryActivity : AppCompatActivity() {
    private lateinit var preferences: AuthPreferences
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter
    private val list = ArrayList<ItemsItem>()
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferences = AuthPreferences(this)
        adapter = HistoryAdapter(list)
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this, HistoryViewModelFactory(preferences)).get(
            HistoryViewModel::class.java
        )

        binding.apply {
            rvUsers.setHasFixedSize(true)
            rvUsers.layoutManager = LinearLayoutManager(this@HistoryActivity)
            rvUsers.adapter = adapter
        }

        userId = preferences.getId()
        viewModel.setStories(userId!!)
        viewModel.getStories().observe(this, {
            if (it != null){
                adapter.setList(it)
                showRecyclerList()
            }
        })
        viewModel.isLoading.observe(this, {
            showLoading(it)
        })

    }



    private fun showRecyclerList() {

//        adapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
//            override fun onItemClicked(data: Story) {
//                val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
//                intentToDetail.putExtra(DetailActivity.NAME, data.name)
//                intentToDetail.putExtra(DetailActivity.DESC, data.description)
//                intentToDetail.putExtra(DetailActivity.PHOTO, data.photoUrl)
//                startActivity(intentToDetail)
//            }
//        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStories(userId!!)
    }
}