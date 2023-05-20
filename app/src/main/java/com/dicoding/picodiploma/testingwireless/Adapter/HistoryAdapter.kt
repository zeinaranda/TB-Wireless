package com.dicoding.picodiploma.testingwireless.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.testingwireless.Model.ItemsItem
import com.dicoding.picodiploma.testingwireless.databinding.HistoryRowBinding

class HistoryAdapter(private val listStory: ArrayList<ItemsItem>) : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var binding: HistoryRowBinding

    fun setList (user: List<ItemsItem>){
        listStory.clear()
        listStory.addAll(user)
        notifyDataSetChanged()
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) : ViewHolder {
        val view =
            HistoryRowBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        listStory[position].let { viewHolder.bind(it) }

        viewHolder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listStory[viewHolder.adapterPosition])
        }
    }

    override fun getItemCount() = listStory.size

    class ViewHolder( private var binding: HistoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(listStory: ItemsItem) {
            binding.tvStatus.text = listStory.status
            binding.jurusan.text = listStory.jurusan
            binding.tvStatus.text = listStory.kategori
            binding.tanggal.text = listStory.tanggal
            binding.latitude.text = listStory.latitude
            binding.longitude.text = listStory.longitude

        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ItemsItem)
    }
}
