package com.afauzi.zimovieapp.presentation.viewmodel.movie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.afauzi.zimovieapp.data.datasource.MovieReviewsPagingSource
import com.afauzi.zimovieapp.data.datasource.MoviesByGenrePagingSource
import com.afauzi.zimovieapp.data.datasource.MoviesPagingSource
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieDetail
import com.afauzi.zimovieapp.domain.modelentities.video.VideoResultsItem
import com.afauzi.zimovieapp.presentation.viewmodel.auth.AuthViewModel
import kotlinx.coroutines.launch

class MovieViewModel(
    private val movieRepository: MovieRepository,
    private val movieApiService: MovieApiService
) : ViewModel() {

    private val _genreResult = MutableLiveData<GenreResult>()
    val genreResult: LiveData<GenreResult>
        get() = _genreResult

    private val _genres = MutableLiveData<List<Genre>>()
    val genres: LiveData<List<Genre>>
        get() = _genres

    fun getGenres() {
        viewModelScope.launch {
            try {
                movieRepository.getGenreMovies( object : MovieRepository.OnFinishedRequestGenre{
                    override fun onSuccess(data: List<Genre>) {
                        _genres.value = data
                        _genreResult.postValue(GenreResult.Success("Success get data"))
                    }

                    override fun onError(msg: String) {
                        _genreResult.postValue(GenreResult.Failure(msg))
                    }

                })
            } catch (e: Exception) {
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
            } catch (e: Exception) {
                Log.e(TAG, "Error retrieving movie videos", e)
            }
        }
    }

    private val _movieDetail = MutableLiveData<MovieDetail>()
    val movieDetail: LiveData<MovieDetail>
        get() = _movieDetail
    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            try {
                _movieDetail.value = movieRepository.getMovieDetail(movieId)
            }catch (e: Exception) {
                Log.e(TAG, "Error retrieving movie detail", e)
            }
        }
    }

    val listDataMovie = Pager(PagingConfig(pageSize = 6)) {
        MoviesPagingSource(movieApiService)
    }.flow.cachedIn(viewModelScope)

    fun listDataMovieByGenre(genreId: String) = Pager(PagingConfig(pageSize = 6)) {
        MoviesByGenrePagingSource(movieApiService, genreId)
    }.flow.cachedIn(viewModelScope)

    fun listMovieReviews(movieId: Int) = Pager(PagingConfig(pageSize = 5)) {
        MovieReviewsPagingSource(movieApiService, movieId)
    }.flow.cachedIn(viewModelScope)

    sealed class GenreResult {
        class Success(val msg: String) : GenreResult()
        class Failure(val error: String) : GenreResult()
    }

    companion object {
        const val TAG = "MovieViewModel"
    }
}