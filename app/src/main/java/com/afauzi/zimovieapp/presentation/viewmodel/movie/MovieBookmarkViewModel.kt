package com.afauzi.zimovieapp.presentation.viewmodel.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.afauzi.zimovieapp.data.repository.MovieBookmarkRepository
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieBookmark
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieDetail
import kotlinx.coroutines.runBlocking

class MovieBookmarkViewModel(private val movieBookmarkRepository: MovieBookmarkRepository): ViewModel() {
    fun getCachedMovieToBookmark(): LiveData<List<MovieBookmark>> {
        return runBlocking { movieBookmarkRepository.getCachedMovieToBookmark() }
    }
}