package com.afauzi.zimovieapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.zimovieapp.databinding.ItemGenre2Binding
import com.afauzi.zimovieapp.databinding.ItemGenresBinding
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre

class GenresAdapterMovie2(
    private val items: ArrayList<Genre>,
    private val onClickListenerAdapterGenres: ListenerAdapterGenre
): RecyclerView.Adapter<GenresAdapterMovie2.ViewHolder>() {
    class ViewHolder(val binding: ItemGenre2Binding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGenre2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.chipTvGenre.text = name
                binding.chipTvGenre.setOnClickListener {
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