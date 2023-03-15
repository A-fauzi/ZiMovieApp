package com.afauzi.zimovieapp.data.datasource

import android.util.Log
import android.widget.Toast
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.domain.modelentities.moviereviews.MovieReview

class MovieReviewsPagingSource(private val movieApiService: MovieApiService, private val movieId: Int): PagingSource<Int, MovieReview>() {
    override fun getRefreshKey(state: PagingState<Int, MovieReview>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieReview> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = movieApiService.getMovieReviews(
                apiKey = MovieApiProvider.MOVIE_API_KEY,
                language = "en-US",
                movieId = movieId,
                page = currentLoadingPageKey
            )
            val responseData = mutableListOf<MovieReview>()
            val data = response.body()?.results
            data?.let { responseData.addAll(it) }

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

           return if (responseData.isEmpty()) {
                LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = null
                )
            } else {
                LoadResult.Page(
                    data = responseData,
                    prevKey = prevKey,
                    nextKey = currentLoadingPageKey.plus(1)
                )
            }
        }catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}