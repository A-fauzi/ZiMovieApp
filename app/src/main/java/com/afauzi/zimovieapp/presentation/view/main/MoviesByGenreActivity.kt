package com.afauzi.zimovieapp.presentation.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.GridLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.ActivityMoviesByGenreBinding
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.presentation.adapter.MovieAdapterPaging
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import kotlinx.coroutines.launch

class MoviesByGenreActivity : AppCompatActivity(), MovieAdapterPaging.ListenerMoviesAdapter {
    private lateinit var binding: ActivityMoviesByGenreBinding
    private lateinit var moviePagingAdapter: MovieAdapterPaging
    private lateinit var movieViewModel: MovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesByGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val movieApiService = MovieApiProvider.provideMovieApiService()
        val movieRepository = MovieRepository(movieApiService)
        val viewModelFactory = MovieViewModelFactory(movieRepository, movieApiService)
        movieViewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        moviePagingAdapter = MovieAdapterPaging(this, this )
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = moviePagingAdapter
        }

        val bundleExtras = intent.extras
        val idGenre = bundleExtras?.getString("idGenres")
        val nameGenre = bundleExtras?.getString("nameGenres")
        binding.tvGenreName.text = nameGenre

        lifecycleScope.launch {
            movieViewModel.listDataMovieByGenre(idGenre.toString()).collect {
                moviePagingAdapter.submitData(it)
            }
        }
    }

    override fun onClickItemPopularMovies(data: Movie?) {
        Toast.makeText(this, data?.title, Toast.LENGTH_SHORT).show()
    }
}