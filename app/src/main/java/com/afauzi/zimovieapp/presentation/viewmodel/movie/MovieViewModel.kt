package com.afauzi.zimovieapp.presentation.viewmodel.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.afauzi.zimovieapp.data.datasource.MoviePagingSource
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.domain.modelentities.Movie
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class MovieViewModel(private val movieRepository: MovieRepository, private val movieApiService: MovieApiService): ViewModel() {
//    private val _movies = MutableLiveData<List<Movie>>()
//    val movies: LiveData<List<Movie>>
//        get() = _movies
//
//     fun getMovies() {
//        viewModelScope.launch {
//            try {
//                _movies.value = movieRepository.getPopularMovies()
//            }catch (e: Exception) {
//                Log.e(TAG, "Error retrieving popular movies", e)
//            }
//        }
//    }

    val listDataMovie = Pager(PagingConfig(pageSize = 6)) {
        MoviePagingSource(movieApiService)
    }.flow.cachedIn(viewModelScope)

    companion object {
        const val TAG = "MovieViewModel"
    }
}