package com.afauzi.zimovieapp.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.databinding.ItemsMovieReviewsBinding
import com.afauzi.zimovieapp.domain.modelentities.moviereviews.MovieReview
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

class AdapterMovieReviewsPaging(private val context: Context): PagingDataAdapter<MovieReview, AdapterMovieReviewsPaging.ViewHolder>(MovieReviewDiffComp) {
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

                val updateAtConvert = this?.updatedAt?.let { dateConverter(it) }
                val createAtConvert = this?.createdAt?.let { dateConverter(it) }

                if (this?.updatedAt != null || this?.updatedAt != "") binding.tvItemUserDatePost.text = updateAtConvert
                if (this?.updatedAt?.isEmpty() == true)  binding.tvItemUserDatePost.text = createAtConvert
                binding.tvItemContent.text = this?.content
                Glide.with(context).load(MovieApiProvider.BASE_URL_PATH + this?.authorDetails?.avatarPath).placeholder(
                    R.drawable.image_placeholder_50).error(R.drawable.image_example).into(binding.ivItemUserAvatar)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemsMovieReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    private fun dateConverter(inputFormat: String): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(inputFormat)

        val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm:ss", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getDefault()

        return date?.let { outputFormat.format(it) }
    }
}