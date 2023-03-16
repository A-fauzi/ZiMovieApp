package com.afauzi.zimovieapp.data.repository

import android.util.Log
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre
import com.afauzi.zimovieapp.domain.modelentities.genre.Genres
import com.afauzi.zimovieapp.domain.modelentities.video.VideoResultsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieRepository(private val movieApiService: MovieApiService) {

    interface OnFinishedRequestGenre {
        fun onSuccess(data: List<Genre>)
        fun onError(msg: String)
    }

     fun getGenreMovies(listener: OnFinishedRequestGenre){
       movieApiService.getGenreMovies(
            apiKey = MovieApiProvider.MOVIE_API_KEY,
            language = "en-US",
        ).enqueue(object : Callback<Genres>{
            override fun onResponse(call: Call<Genres>, response: Response<Genres>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        listener.onSuccess(it.genres ?: emptyList())
                    }
                } else {
                    listener.onError(response.message())
                }
            }

            override fun onFailure(call: Call<Genres>, t: Throwable) {
                t.localizedMessage?.let { listener.onError(t.message.toString()) }
            }

        })
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
}