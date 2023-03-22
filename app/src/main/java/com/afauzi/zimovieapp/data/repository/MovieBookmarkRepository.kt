package com.afauzi.zimovieapp.data.repository

import androidx.lifecycle.LiveData
import com.afauzi.zimovieapp.data.local.MovieBookmarkDao
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieBookmark
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieDetail

class MovieBookmarkRepository(private val movieBookmarkDao: MovieBookmarkDao) {
    fun getCachedMovieToBookmark(): LiveData<List<MovieBookmark>> {
        return movieBookmarkDao.getMovieBookmark()
    }
}