package com.afauzi.zimovieapp.domain.modelentities.moviereviews

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MovieReviewsResponse(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val results: List<MovieReview>? = null,

	@field:SerializedName("total_results")
	val totalResults: Int? = null
) : Parcelable