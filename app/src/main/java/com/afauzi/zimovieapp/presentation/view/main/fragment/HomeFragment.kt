package com.afauzi.zimovieapp.presentation.view.main.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.FragmentHomeBinding
import com.afauzi.zimovieapp.domain.modelentities.Movie
import com.afauzi.zimovieapp.presentation.adapter.MovieAdapterPaging
import com.afauzi.zimovieapp.presentation.view.auth.SignInActivity
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), MovieAdapterPaging.OnClickListenerMoviesAdapter {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapterPaging: MovieAdapterPaging
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        Toast.makeText(requireActivity(), user?.email, Toast.LENGTH_SHORT).show()

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireActivity(), SignInActivity::class.java))
            requireActivity().finish()
        }

        val movieApiService = MovieApiProvider.provideMovieApiService()
        val movieRepository = MovieRepository(movieApiService)
        val viewModelFactory = MovieViewModelFactory(movieRepository, movieApiService)
        viewModel = ViewModelProvider(this, viewModelFactory)[MovieViewModel::class.java]

        movieAdapterPaging = MovieAdapterPaging(requireActivity(), this )
        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = movieAdapterPaging
        }

        lifecycleScope.launch {
            viewModel.listDataMovie.collect {
                movieAdapterPaging.submitData(it)
            }
        }
    }

    override fun onClickItemPopularMovies(data: Movie?) {
        Toast.makeText(requireActivity(), data?.title, Toast.LENGTH_SHORT).show()
    }
}