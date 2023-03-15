package com.afauzi.zimovieapp.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MovieApiProvider {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    const val MOVIE_API_KEY = "6b58a885b39527c4220c85208db1e0d6"
    const val YOUTUBE_API_KEY = "AIzaSyCnk147vH4_WhwcUP_m51QfMMI0Jp2QLV0"
    const val BASE_URL_PATH = "https://image.tmdb.org/t/p/original/"
    const val LANGUAGE = "id"

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun provideMovieApiService(): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }
}