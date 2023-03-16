package com.afauzi.zimovieapp.presentation.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.ActivityMoviesByGenreBinding
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.presentation.adapter.AdapterMoviePaging
import com.afauzi.zimovieapp.presentation.adapter.stateadapter.StateLoadAdapterMoviePaging
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import com.afauzi.zimovieapp.utils.Helper
import kotlinx.coroutines.launch

class MoviesByGenreActivity : AppCompatActivity(), AdapterMoviePaging.ListenerMoviesAdapter {
    private lateinit var binding: ActivityMoviesByGenreBinding
    private lateinit var moviePagingAdapter: AdapterMoviePaging
    private lateinit var movieViewModel: MovieViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesByGenreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onStart() {
        super.onStart()

        when(Helper.checkInternetConnect(this)) {
            true -> {
                viewVisible()
            }
            false -> {
                binding.contentContainer.visibility = View.GONE
                binding.disconnetedContainer.root.visibility = View.VISIBLE
                binding.disconnetedContainer.btnRefreshConnection.setOnClickListener {
                    when(Helper.checkInternetConnect(this)) {
                        true -> { viewVisible() }
                        false -> {
                            Toast.makeText(this, "Please check your connection internet", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun viewVisible() {
        binding.contentContainer.visibility = View.VISIBLE
        binding.disconnetedContainer.root.visibility = View.GONE

        val bundleExtras = intent.extras
        val idGenre = bundleExtras?.getString("idGenres")
        val nameGenre = bundleExtras?.getString("nameGenres")
        binding.tvGenreName.text = nameGenre

        setUpRecyclerView()
        setUpViewModel(idGenre)
    }

    private fun setUpViewModel(idGenre: String?) {
        lifecycleScope.launch {
            movieViewModel.listDataMovieByGenre(idGenre.toString()).collect {
                moviePagingAdapter.submitData(it)
            }
        }
    }


    private fun setUpRecyclerView() {
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = moviePagingAdapter.withLoadStateHeaderAndFooter(
                header = StateLoadAdapterMoviePaging { moviePagingAdapter.retry() },
                footer = StateLoadAdapterMoviePaging { moviePagingAdapter.retry() },
            )
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

    private fun init() {
        val movieApiService = MovieApiProvider.provideMovieApiService()
        val movieRepository = MovieRepository(movieApiService)
        val viewModelFactory = MovieViewModelFactory(movieRepository, movieApiService)
        movieViewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        moviePagingAdapter = AdapterMoviePaging(this, this )
    }
}