package com.afauzi.zimovieapp.presentation.view.main.fragment

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.map
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.FragmentHomeBinding
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.presentation.adapter.GenresAdapterMovie
import com.afauzi.zimovieapp.presentation.adapter.MovieAdapterPaging
import com.afauzi.zimovieapp.presentation.view.main.MoviesByGenreActivity
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), MovieAdapterPaging.ListenerMoviesAdapter, GenresAdapterMovie.ListenerAdapterGenre {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapterPaging: MovieAdapterPaging
    private lateinit var genresAdapterMovie: GenresAdapterMovie
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        user?.let {
            binding.tvUsername.text = it.email
        }


        when (requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.switchToogle.isOn = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.switchToogle.isOn = false
            }
        }
        binding.switchToogle.setOnToggledListener { toggleableView, isOn ->
            if (isOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieApiService = MovieApiProvider.provideMovieApiService()
        val movieRepository = MovieRepository(movieApiService)
        val viewModelFactory = MovieViewModelFactory(movieRepository, movieApiService)
        viewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        movieAdapterPaging = MovieAdapterPaging(requireActivity(), this )
        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = movieAdapterPaging
        }

        genresAdapterMovie = GenresAdapterMovie(requireActivity(), arrayListOf(), this)
        binding.rvGenres.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = genresAdapterMovie
        }

        lifecycleScope.launch {
            viewModel.listDataMovie.collect {
                movieAdapterPaging.submitData(it)
            }
        }

        viewModel.genres.observe(requireActivity()){
            genresAdapterMovie.setData(it)
        }

        viewModel.getGenres()
    }

    override fun onClickItemPopularMovies(data: Movie?) {
        Toast.makeText(requireActivity(), data?.title, Toast.LENGTH_SHORT).show()
    }

    override fun onClickItemGenre(data: Genre) {
        val bundle = Bundle()
        bundle.putString("idGenres", data.id.toString())
        bundle.putString("nameGenres", data.name)
        val intent = Intent(requireActivity(), MoviesByGenreActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }
}