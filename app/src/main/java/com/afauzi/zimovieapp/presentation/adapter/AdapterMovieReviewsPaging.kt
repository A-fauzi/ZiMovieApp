package com.afauzi.zimovieapp.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.zimovieapp.databinding.ItemsMovieReviewsBinding
import com.afauzi.zimovieapp.domain.modelentities.moviereviews.MovieReview

class AdapterMovieReviewsPaging: PagingDataAdapter<MovieReview, AdapterMovieReviewsPaging.ViewHolder>(MovieReviewDiffComp) {
    class ViewHolder(val binding: ItemsMovieReviewsBinding): RecyclerView.ViewHolder(binding.root)

    object MovieReviewDiffComp : DiffUtil.ItemCallback<MovieReview>() {
        override fun areItemsTheSame(oldItem: MovieReview, newItem: MovieReview): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MovieReview, newItem: MovieReview): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(getItem(position)) {
                binding.tvItemUserAuthor.text = this?.author
                if (this?.updatedAt != null || this?.updatedAt != "") binding.tvItemUserDatePost.text = this?.updatedAt
                if (this?.updatedAt?.isEmpty() == true)  binding.tvItemUserDatePost.text = this.createdAt
                binding.tvItemContent.text = this?.content
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsMovieReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}