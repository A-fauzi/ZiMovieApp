package com.afauzi.zimovieapp.data.repository

import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie

class MovieRepository(private val movieApiService: MovieApiService) {

    suspend fun getGenreMovies(): List<Genre>{
        val response = movieApiService.getGenreMovies(
            apiKey = MovieApiProvider.API_KEY,
            language = "en-US",
        )
        if (response.isSuccessful) {
            return response.body()?.genres ?: emptyList()
        } else {
            throw Exception("Error retrieving movies")
        }
    }

    companion object {
        private const val TAG = "MovieRepository"
    }
}