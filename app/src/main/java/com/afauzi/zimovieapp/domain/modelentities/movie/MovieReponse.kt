package com.afauzi.zimovieapp.domain.modelentities.movie

data class MovieResponse(
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)
