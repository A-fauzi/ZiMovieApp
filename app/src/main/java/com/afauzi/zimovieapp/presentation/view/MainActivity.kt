package com.afauzi.zimovieapp.presentation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.ActivityMainBinding
import com.afauzi.zimovieapp.presentation.adapter.MovieAdapterPaging
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapterPaging: MovieAdapterPaging
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        Toast.makeText(this, user?.email, Toast.LENGTH_SHORT).show()

        val movieApiService = MovieApiProvider.provideMovieApiService()
        val movieRepository = MovieRepository(movieApiService)
        val viewModelFactory = MovieViewModelFactory(movieRepository, movieApiService)
        viewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]
//        viewModel.movies.observe(this) { list ->
//            list.forEach {
//                Log.d(TAG, it.originalTitle.toString())
//            }
//        }
//        viewModel.getMovies()

        movieAdapterPaging = MovieAdapterPaging()
        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieAdapterPaging
        }

        lifecycleScope.launch {
            viewModel.listDataMovie.collect {
                movieAdapterPaging.submitData(it)
            }
        }

    }

    companion object {
        private const val TAG = "MainActivity"
    }
}