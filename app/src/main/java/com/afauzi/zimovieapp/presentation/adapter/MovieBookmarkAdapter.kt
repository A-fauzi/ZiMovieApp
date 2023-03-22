package com.afauzi.zimovieapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.databinding.ItemMovieBookmarkBinding
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieBookmark
import com.bumptech.glide.Glide

class MovieBookmarkAdapter(
    private val context: Context,
    private val items: ArrayList<MovieBookmark>,
    private val listener: MovieBookmarkListener
) : RecyclerView.Adapter<MovieBookmarkAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemMovieBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMovieBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                Glide.with(context).load(MovieApiProvider.BASE_URL_PATH + posterPath).placeholder(
                    R.drawable.image_placeholder_50
                ).into(binding.ivItemPoster)
                binding.tvItemTitle.text = title
                binding.cvItem.setOnClickListener {
                    listener.onClickItem(items[position])
                }
                binding.tvItemReleaseDate.text = releaseDate
                binding.tvItemVoteAverage.text = voteAverage
            }
        }
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<MovieBookmark>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    interface MovieBookmarkListener {
        fun onClickItem(data: MovieBookmark)
    }
}