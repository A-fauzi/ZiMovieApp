package com.afauzi.zimovieapp.data.repository

import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre
import com.afauzi.zimovieapp.domain.modelentities.moviereviews.MovieReview
import com.afauzi.zimovieapp.domain.modelentities.video.VideoResultsItem

class MovieRepository(private val movieApiService: MovieApiService) {

    suspend fun getGenreMovies(): List<Genre>{
        val response = movieApiService.getGenreMovies(
            apiKey = MovieApiProvider.MOVIE_API_KEY,
            language = "en-US",
        )
        if (response.isSuccessful) {
            return response.body()?.genres ?: emptyList()
        } else {
            throw Exception("Error retrieving movies")
        }
    }

    suspend fun getMovieVideos(movieId: Int): List<VideoResultsItem?>{
        val response = movieApiService.getMovieVideo(
            movieId = movieId,
            apiKey = MovieApiProvider.MOVIE_API_KEY,
            language = "en-US"
        )
        if (response.isSuccessful) {
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error retrieving movie videos")
        }
    }

    companion object {
        private const val TAG = "MovieRepository"
    }
}