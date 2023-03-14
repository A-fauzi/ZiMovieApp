package com.afauzi.zimovieapp.data.remote

import com.afauzi.zimovieapp.domain.modelentities.genre.Genres
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("genre/movie/list")
    suspend fun getGenreMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<Genres>

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("sort_by") sortBy: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("include_video") includeVideo: Boolean,
        @Query("with_genres") genreId: String,
        @Query("page") page: Int
    ): Response<MovieResponse>
}