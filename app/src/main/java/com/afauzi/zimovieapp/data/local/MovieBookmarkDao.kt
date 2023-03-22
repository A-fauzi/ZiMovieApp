package com.afauzi.zimovieapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieBookmark
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieDetail

@Dao
interface MovieBookmarkDao {
    @Query("SELECT * FROM movie_bookmark")
    fun getMovieBookmark(): LiveData<List<MovieBookmark>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieToBookmark(movie: MovieBookmark)

    @Delete
    suspend fun deleteSingleMovieBookmark(movie: MovieBookmark)
}