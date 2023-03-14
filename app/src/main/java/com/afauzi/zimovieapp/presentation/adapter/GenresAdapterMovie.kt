package com.afauzi.zimovieapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.zimovieapp.databinding.ItemGenresBinding
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre

class GenresAdapterMovie(
    private val context: Context,
    private val items: ArrayList<Genre>,
    private val onClickListenerAdapterGenres: ListenerAdapterGenre
): RecyclerView.Adapter<GenresAdapterMovie.ViewHolder>() {
    class ViewHolder(val binding: ItemGenresBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGenresBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                val icon = arrayListOf("\uD83C\uDF89", "⛱", "\uD83C\uDF21", "\uD83E\uDDFF", "\uD83E\uDDF5", "\uD83D\uDE80", "\uD83E\uDE90", "\uD83C\uDF0C")
                binding.tvItemNameGenres.text = "${icon.random()}\n${name}"
                binding.cvBtnItemGenre.setOnClickListener {
                    onClickListenerAdapterGenres.onClickItemGenre(items[position])
                }
            }
        }
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<Genre>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    interface ListenerAdapterGenre {
        fun onClickItemGenre(data: Genre)
    }
}