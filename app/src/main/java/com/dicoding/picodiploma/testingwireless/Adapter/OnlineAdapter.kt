package com.dicoding.picodiploma.testingwireless.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.testingwireless.Model.ItemsItem
import com.dicoding.picodiploma.testingwireless.Model.OnlineItem
import com.dicoding.picodiploma.testingwireless.databinding.HistoryRowBinding
import com.dicoding.picodiploma.testingwireless.databinding.OnlineRowBinding
import com.dicoding.picodiploma.testingwireless.utils.DateUtils

class OnlineAdapter(private val listStory: ArrayList<OnlineItem>) : RecyclerView.Adapter<OnlineAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var binding: OnlineRowBinding

    fun setList (user: List<OnlineItem>){
        listStory.clear()
        listStory.addAll(user)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) : ViewHolder {
        val view =
            OnlineRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        listStory[position].let { viewHolder.bind(it) }

//        viewHolder.itemView.setOnClickListener {
//            onItemClickCallback.onItemClicked(listStory[viewHolder.adapterPosition])
//        }
    }

    override fun getItemCount() = listStory.size

    class ViewHolder( private var binding: OnlineRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listUser: OnlineItem) {
            binding.nama.text = listUser.nama
            binding.nim.text = listUser.nim
            binding.lokasi.text = listUser.jurusan
            binding.waktu.text = DateUtils.formatDate(listUser.waktu!!)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: OnlineItem)
    }
}
