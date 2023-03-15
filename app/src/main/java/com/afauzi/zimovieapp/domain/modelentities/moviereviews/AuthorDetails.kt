package com.afauzi.zimovieapp.domain.modelentities.moviereviews

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class AuthorDetails(

	@field:SerializedName("avatar_path")
	val avatarPath: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: Float? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable