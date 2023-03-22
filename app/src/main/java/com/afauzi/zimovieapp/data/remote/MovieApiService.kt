package com.afauzi.zimovieapp.data.remote

import com.afauzi.zimovieapp.domain.modelentities.genre.Genres
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieDetail
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieResponse
import com.afauzi.zimovieapp.domain.modelentities.moviereviews.MovieReviewsResponse
import com.afauzi.zimovieapp.domain.modelentities.video.VideoResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("genre/movie/list")
    fun getGenreMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Call<Genres>

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

    @GET("movie/{id}/videos")
    suspend fun getMovieVideo(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<VideoResponse>

    @GET("movie/{id}/reviews")
    suspend fun getMovieReviews(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MovieReviewsResponse>

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
    ): Response<MovieDetail>
}