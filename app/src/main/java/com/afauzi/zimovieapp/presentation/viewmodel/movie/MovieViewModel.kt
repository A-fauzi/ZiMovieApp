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
import com.afauzi.zimovieapp.data.datasource.MovieReviewsPagingSource
import com.afauzi.zimovieapp.data.datasource.MoviesByGenrePagingSource
import com.afauzi.zimovieapp.data.datasource.MoviesPagingSource
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.domain.modelentities.video.VideoResultsItem
import kotlinx.coroutines.launch
import java.util.concurrent.Flow

class MovieViewModel(private val movieRepository: MovieRepository, private val movieApiService: MovieApiService): ViewModel() {


    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>>
        get() = _genres

    fun getGenres() {
        viewModelScope.launch {
            try {
                _genres.value = movieRepository.getGenreMovies()
            }catch (e: Exception) {
                Log.e(TAG, "Error retrieving popular movies", e)
            }
        }
    }

    private val _movieVideos = MutableLiveData<List<VideoResultsItem?>>()
    val movieVideos: LiveData<List<VideoResultsItem?>>
        get() = _movieVideos

    fun getMovieVideos(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieVideos.value = movieRepository.getMovieVideos(movieId)
            }catch (e: Exception){
                Log.e(TAG, "Error retrieving movie videos", e)
            }
        }
    }

    val listDataMovie = Pager(PagingConfig(pageSize = 6)) {
        MoviesPagingSource(movieApiService)
    }.flow.cachedIn(viewModelScope)

    fun listDataMovieByGenre(genreId: String) = Pager(PagingConfig(pageSize = 6)) {
        MoviesByGenrePagingSource(movieApiService, genreId)
    }.flow.cachedIn(viewModelScope)

    fun listMovieReviews(movieId: Int) = Pager(PagingConfig(pageSize = 10)) {
        MovieReviewsPagingSource(movieApiService, movieId)
    }.flow.cachedIn(viewModelScope)


    companion object {
        const val TAG = "MovieViewModel"
    }
}