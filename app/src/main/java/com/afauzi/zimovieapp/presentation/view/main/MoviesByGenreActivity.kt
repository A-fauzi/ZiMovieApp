package com.afauzi.zimovieapp.presentation.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.ActivityMoviesByGenreBinding
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.presentation.adapter.AdapterMoviePaging
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import kotlinx.coroutines.launch

class MoviesByGenreActivity : AppCompatActivity(), AdapterMoviePaging.ListenerMoviesAdapter {
    private lateinit var binding: ActivityMoviesByGenreBinding
    private lateinit var moviePagingAdapter: AdapterMoviePaging
    private lateinit var movieViewModel: MovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesByGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val movieApiService = MovieApiProvider.provideMovieApiService()
        val movieRepository = MovieRepository(movieApiService)
        val viewModelFactory = MovieViewModelFactory(movieRepository, movieApiService)
        movieViewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        moviePagingAdapter = AdapterMoviePaging(this, this )
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
        val bundle = Bundle()
        bundle.putString("movieId", data?.id.toString())
        bundle.putString("backDrop", data?.backdropPath)
        bundle.putString("title", data?.originalTitle)
        bundle.putString("overview", data?.overview)
        bundle.putString("overview", data?.overview)
        bundle.putString("voteAverage", data?.voteAverage.toString())

        val intent = Intent(this, DetailMovieActivity::class.java)
        intent.putExtras(bundle)
        intent.putIntegerArrayListExtra("genresId", data?.genreIds as ArrayList<Int?>?)
        startActivity(intent)
    }
}