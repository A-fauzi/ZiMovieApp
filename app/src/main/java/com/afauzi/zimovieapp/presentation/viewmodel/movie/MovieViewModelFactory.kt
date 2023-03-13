package com.afauzi.zimovieapp.presentation.viewmodel.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.data.repository.MovieRepository

class MovieViewModelFactory(private val movieRepository: MovieRepository, private val movieApiService: MovieApiService): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(movieRepository, movieApiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}