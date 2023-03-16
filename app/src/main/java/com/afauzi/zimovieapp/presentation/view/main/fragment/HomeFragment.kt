package com.afauzi.zimovieapp.presentation.view.main.fragment

import android.R
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.FragmentHomeBinding
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre
import com.afauzi.zimovieapp.domain.modelentities.movie.Movie
import com.afauzi.zimovieapp.presentation.adapter.AdapterGenresMovie
import com.afauzi.zimovieapp.presentation.adapter.AdapterMoviePaging
import com.afauzi.zimovieapp.presentation.adapter.stateadapter.StateLoadAdapterMoviePaging
import com.afauzi.zimovieapp.presentation.view.main.DetailMovieActivity
import com.afauzi.zimovieapp.presentation.view.main.MoviesByGenreActivity
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import com.afauzi.zimovieapp.utils.Helper
import com.google.firebase.auth.FirebaseAuth
import io.paperdb.Paper
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), AdapterMoviePaging.ListenerMoviesAdapter,
    AdapterGenresMovie.ListenerAdapterGenre {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapterPaging: AdapterMoviePaging
    private lateinit var genresAdapterMovie: AdapterGenresMovie
    private lateinit var auth: FirebaseAuth
    private lateinit var movieApiService: MovieApiService
    private lateinit var movieRepository: MovieRepository
    private lateinit var movieViewModelFactory: MovieViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        initView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Helper.checkConnection(requireActivity(), object : Helper.OnCheckFinished {
            override fun onConnected() {
                viewVisible()
            }

            override fun onDisconnected() {
                viewDisconnected("Please your check connection internet")
            }

        })
    }

    private fun viewVisible() {
        binding.containerFullContent.visibility = View.VISIBLE
        binding.containerLayoutDisconnected.root.visibility = View.GONE

        currentUser()

        themeAction()

        setUpRecyclerView()

        setUpViewModel()
    }

    private fun viewDisconnected(msg: String, onClickViewAction: (() -> Unit)? = null) {
        themeAction()
        binding.progressBar.visibility = View.GONE
        binding.containerFullContent.visibility = View.GONE
        binding.containerLayoutDisconnected.root.visibility = View.VISIBLE
        binding.containerLayoutDisconnected.tvError.text = msg
        binding.containerLayoutDisconnected.btnRefreshConnection.setOnClickListener {

            if (onClickViewAction == null) {
                Helper.checkConnection(requireActivity(), object : Helper.OnCheckFinished {
                    override fun onConnected() {
                        viewVisible()
                    }

                    override fun onDisconnected() {
                        Toast.makeText(
                            requireActivity(),
                            msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                })
            } else {
                onClickViewAction()
            }
        }
    }

    private fun themeAction() {
        checkThemeState()

        checkStateThemeIfDarkMode()

        switchToggleThemeAction()
    }

    private fun setUpViewModel() {
        binding.progressBar.visibility = View.VISIBLE
        binding.containerFullContent.visibility = View.GONE

        lifecycleScope.launch {
            viewModel.listDataMovie.collect {
                movieAdapterPaging.submitData(it)
            }
        }

        viewModel.genres.observe(requireActivity()) {
            genresAdapterMovie.setData(it)
        }
        viewModel.genreResult.observe(requireActivity()) {
            when(it) {
                is MovieViewModel.GenreResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.containerFullContent.visibility = View.VISIBLE
                }
                is MovieViewModel.GenreResult.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.containerFullContent.visibility = View.GONE
                    binding.containerLayoutDisconnected.root.visibility = View.VISIBLE
                    binding.containerLayoutDisconnected.tvError.text = it.error
                    binding.containerLayoutDisconnected.btnRefreshConnection.setOnClickListener {
                        val fragmentId =  findNavController().currentDestination?.id
                        if (fragmentId != null) {
                            findNavController().popBackStack(fragmentId, true)
                            findNavController().navigate(fragmentId)
                        }
                    }
                }
            }
        }
        viewModel.getGenres()
    }

    override fun onClickItemPopularMovies(data: Movie?) {
        val bundle = Bundle()
        bundle.putString("movieId", data?.id.toString())
        bundle.putString("backDrop", data?.backdropPath)
        bundle.putString("title", data?.originalTitle)
        bundle.putString("overview", data?.overview)
        bundle.putString("voteAverage", data?.voteAverage.toString())

        val intent = Intent(requireActivity(), DetailMovieActivity::class.java)
        intent.putExtras(bundle)
        intent.putIntegerArrayListExtra("genresId", data?.genreIds as ArrayList<Int?>?)
        startActivity(intent)
    }

    override fun onClickItemGenre(data: Genre) {
        val bundle = Bundle()
        bundle.putString("idGenres", data.id.toString())
        bundle.putString("nameGenres", data.name)
        val intent = Intent(requireActivity(), MoviesByGenreActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    private fun checkStateThemeIfDarkMode() {
        when (requireActivity().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.switchToogle.isOn = true
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.switchToogle.isOn = false
            }
        }
    }

    private fun currentUser() {
        val user = auth.currentUser
        user?.let {
            binding.tvUsername.text = it.email
        }
    }

    private fun checkThemeState() {
        when (Paper.book().read<String>("darkTheme").toString()) {
            "true" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "false" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun switchToggleThemeAction() {
        binding.switchToogle.setOnToggledListener { toggleableView, isOn ->
            if (isOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                Paper.book().write("darkTheme", isOn)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                Paper.book().write("darkTheme", isOn)
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvGenres.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = genresAdapterMovie
        }

        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = movieAdapterPaging.withLoadStateHeaderAndFooter(
                header = StateLoadAdapterMoviePaging { movieAdapterPaging.retry() },
                footer = StateLoadAdapterMoviePaging { movieAdapterPaging.retry() }
            )
        }
    }

    private fun initView() {
        Paper.init(requireActivity())

        auth = FirebaseAuth.getInstance()

        movieApiService = MovieApiProvider.provideMovieApiService()
        movieRepository = MovieRepository(movieApiService)
        movieViewModelFactory = MovieViewModelFactory(movieRepository, movieApiService)

        viewModel = ViewModelProvider(this, movieViewModelFactory)[MovieViewModel::class.java]

        movieAdapterPaging = AdapterMoviePaging(requireActivity(), this)
        genresAdapterMovie = AdapterGenresMovie(requireActivity(), arrayListOf(), this)
    }

}