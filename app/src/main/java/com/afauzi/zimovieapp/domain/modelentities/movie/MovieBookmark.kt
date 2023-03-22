package com.afauzi.zimovieapp.domain.modelentities.movie

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "movie_bookmark")
data class MovieBookmark(
    @PrimaryKey
    val id: Int?,

    val backdrop: String?,

    val title: String?,

    val originalLanguage: String?,

    val overView: String?,

    val voteAverage: String?,

    val revenue: Int?,

    val popularity: Double?,

    val voteCount: Int?,

    val posterPath: String?,

    val releaseDate: String?
)
