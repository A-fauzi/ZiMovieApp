package com.afauzi.zimovieapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.databinding.ItemMoviesBinding
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.bumptech.glide.Glide

class MovieAdapterPaging(
    private val context: Context,
    private val onClickListenerMoviesAdapter: ListenerMoviesAdapter
    ): PagingDataAdapter<Movie, MovieAdapterPaging.ViewHolder>(ProductDiffComp) {
    object ProductDiffComp : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    inner class ViewHolder(val binding: ItemMoviesBinding): RecyclerView.ViewHolder(binding.root)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                binding.tvItemTitle.text = this?.originalTitle
                binding.tvItemReleaseDate.text = this?.releaseDate
                binding.cvBtnItem.setOnClickListener {
                    onClickListenerMoviesAdapter.onClickItemPopularMovies(getItem(position))
                }
                Glide.with(context).load(MovieApiProvider.BASE_URL_PATH + this?.posterPath).placeholder(R.drawable.image_placeholder_50).into(binding.ivItemPoster)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMoviesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    interface ListenerMoviesAdapter {
        fun onClickItemPopularMovies(data: Movie?)
    }
}