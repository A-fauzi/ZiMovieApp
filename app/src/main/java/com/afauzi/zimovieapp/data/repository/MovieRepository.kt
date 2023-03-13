package com.afauzi.zimovieapp.data.repository

import android.util.Log
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.domain.modelentities.Movie

class MovieRepository(private val movieApiService: MovieApiService) {
    suspend fun getPopularMovies(): List<Movie>{
        val response = movieApiService.getPopularMovies(
            apiKey = MovieApiProvider.API_KEY,
            language = MovieApiProvider.LANGUAGE,
            page = 1
        )
        if (response.isSuccessful) {
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Error retrieving movies")
        }
    }

    companion object {
        private const val TAG = "MovieRepository"
    }
}