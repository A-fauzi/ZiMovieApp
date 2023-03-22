package com.afauzi.zimovieapp.presentation.viewmodel.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.afauzi.zimovieapp.data.repository.MovieBookmarkRepository
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import kotlinx.coroutines.runBlocking

class MovieBookmarkViewModelFactory(private val movieBookmarkRepository: MovieBookmarkRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieBookmarkViewModel::class.java)) {
            return MovieBookmarkViewModel(movieBookmarkRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}