package com.afauzi.zimovieapp.presentation.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.local.AppDatabase
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.data.repository.MovieBookmarkRepository
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.ActivityDetailMovieBinding
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieBookmark
import com.afauzi.zimovieapp.presentation.adapter.AdapterGenresMovie2
import com.afauzi.zimovieapp.presentation.adapter.AdapterMovieReviewsPaging
import com.afauzi.zimovieapp.presentation.adapter.stateadapter.StateLoadAdapterMoviePaging
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieBookmarkViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieBookmarkViewModelFactory
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import com.afauzi.zimovieapp.utils.Helper
import com.bumptech.glide.Glide
import io.paperdb.Paper
import kotlinx.coroutines.launch

class DetailMovieActivity : AppCompatActivity(), AdapterGenresMovie2.ListenerAdapterGenre {
    private lateinit var binding: ActivityDetailMovieBinding

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieBokmarkViewModel: MovieBookmarkViewModel
    private lateinit var movieApiService: MovieApiService
    private lateinit var movieRepository: MovieRepository
    private lateinit var movieViewModelFactory: MovieViewModelFactory
    private lateinit var genresAdapterMovie2: AdapterGenresMovie2
    private lateinit var movieReviewsAdapterPaging: AdapterMovieReviewsPaging
    private var favState = false

    companion object {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        Paper.init(this)
    }

    override fun onStart() {
        super.onStart()

        Helper.checkConnection(this, object : Helper.OnCheckFinished {
            override fun onConnected() {
                viewVisible()
            }

            override fun onDisconnected() {
                binding.contentContainer.visibility = View.GONE
                binding.disconnetedContainer.root.visibility = View.VISIBLE
                binding.disconnetedContainer.btnRefreshConnection.setOnClickListener {
                    Helper.checkConnection(
                        this@DetailMovieActivity,
                        object : Helper.OnCheckFinished {
                            override fun onConnected() {
                                viewVisible()
                            }

                            override fun onDisconnected() {
                                Toast.makeText(
                                    this@DetailMovieActivity,
                                    "Please check your connection internet",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })
                }
            }

        })

    }

    private fun viewVisible() {
        binding.contentContainer.visibility = View.VISIBLE
        binding.disconnetedContainer.root.visibility = View.GONE

        val bundleExtras = intent.extras
        val movieId = bundleExtras?.getString("movieId")
        val backdrop = bundleExtras?.getString("backDrop")
        val title = bundleExtras?.getString("title")
        val overview = bundleExtras?.getString("overview")
        val voteAverage = bundleExtras?.getString("voteAverage")
        val genresId = intent.getIntegerArrayListExtra("genresId")

        movieBokmarkViewModel.getCachedMovieToBookmark().observe(this) { list ->
            list.map {
                if (it.id.toString() == movieId) {
                    favState = true
                }
            }
            when (favState) {
                true -> {
                    binding.fabFavCollapsing.setImageResource(R.drawable.ic_star)
                }
                false -> {
                    binding.fabFavCollapsing.setImageResource(R.drawable.ic_star_outline)
                }
            }
        }

        setView {
            binding.tvItemVoteAverage.text = voteAverage
            binding.collapsingToolbar.title = title
            Glide.with(this).load(MovieApiProvider.BASE_URL_PATH + backdrop)
                .placeholder(R.drawable.image_placeholder_50).into(binding.ivBackdropCollapse)
            if (overview != null) {
                if (overview.isNotEmpty()) binding.tvOverView.text = overview
                if (overview.isEmpty()) binding.tvOverView.text = "Is Not Overview"
            }
        }

        onClickView(movieId)
        setUpRecyclerView()
        setUpViewModel(movieId, genresId)
    }

    private fun setUpRecyclerView() {
        binding.rvGenres.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = genresAdapterMovie2
        }

        binding.rvMovieReviews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = movieReviewsAdapterPaging.withLoadStateHeaderAndFooter(
                header = StateLoadAdapterMoviePaging { movieReviewsAdapterPaging.retry() },
                footer = StateLoadAdapterMoviePaging { movieReviewsAdapterPaging.retry() }
            )

            // Untuk mengecek data count pada paging first
            movieReviewsAdapterPaging.addLoadStateListener { loadState ->
                if (loadState.append.endOfPaginationReached) {
                    if (movieReviewsAdapterPaging.itemCount < 1) {
                        // data empty state
                        binding.tvDataReviewsIsEmpty.visibility = View.VISIBLE
                    } else {
                        // data is not empty
                        binding.tvDataReviewsIsEmpty.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun setView(view: () -> Unit) {
        view()
    }

    private fun onClickView(movieId: String?) {
        binding.cvBtnPlayVideo.setOnClickListener {
            if (movieId != null) {
                setUpViewModel(movieId.toInt()) { key ->
                    val intent = Intent(this, VideoPlayerActivity::class.java)
                    intent.putExtra("movieKey", key)
                    startActivity(intent)
                }
            }
        }

        binding.fabFavCollapsing.setOnClickListener {
            favState = !favState

            if (movieId != null) {
                movieViewModel.movieDetail.observe(this) {
                    // save movie to cached
                    it.let {
                        val movieBookmarkDao =
                            AppDatabase.getInstance(applicationContext).movieBookmarkDao()
                        val data = MovieBookmark(
                            it.id,
                            it.backdropPath,
                            it.title,
                            it.originalLanguage,
                            it.overview,
                            it.voteAverage.toString(),
                            it.revenue,
                            it.popularity,
                            it.voteCount,
                            it.posterPath,
                            it.releaseDate
                        )
                        when (favState) {
                            true -> {
                                binding.fabFavCollapsing.setImageResource(R.drawable.ic_star)
                                Toast.makeText(
                                    this,
                                    "Movie ditambahkan ke bookmark",
                                    Toast.LENGTH_SHORT
                                ).show()
                                lifecycleScope.launch {
                                    movieBookmarkDao.insertMovieToBookmark(data)
                                }
                            }
                            false -> {
                                binding.fabFavCollapsing.setImageResource(R.drawable.ic_star_outline)
                                Toast.makeText(
                                    this,
                                    "Movie batal ditambahkan ke bookmark",
                                    Toast.LENGTH_SHORT
                                ).show()
                                lifecycleScope.launch {
                                    movieBookmarkDao.deleteSingleMovieBookmark(data)
                                }
                            }
                        }
                    }
                }
                movieViewModel.getMovieDetail(movieId.toInt())
            }

        }
    }

    private fun getGenresNameById(id: Int, genre: List<Genre>): Genre? {
        return genre.find { it.id == id }
    }

    override fun onClickItemGenre(data: Genre) {
        val bundle = Bundle()
        bundle.putString("idGenres", data.id.toString())
        bundle.putString("nameGenres", data.name)
        val intent = Intent(this, MoviesByGenreActivity::class.java)
        intent.putExtras(bundle)
        startActivity(intent)

    }

    private fun initView() {
        movieApiService = MovieApiProvider.provideMovieApiService()
        movieRepository = MovieRepository(movieApiService)
        movieViewModelFactory = MovieViewModelFactory(movieRepository, movieApiService)
        movieViewModel = ViewModelProvider(this, movieViewModelFactory)[MovieViewModel::class.java]
        genresAdapterMovie2 = AdapterGenresMovie2(arrayListOf(), this)
        movieReviewsAdapterPaging = AdapterMovieReviewsPaging(this)

        val movieBookmarkDao = AppDatabase.getInstance(this).movieBookmarkDao()
        val repository = MovieBookmarkRepository(movieBookmarkDao)
        val viewModelFactory = MovieBookmarkViewModelFactory(repository)
        movieBokmarkViewModel =
            ViewModelProvider(this, viewModelFactory)[MovieBookmarkViewModel::class.java]
    }

    private fun setUpViewModel(movieId: String?, genresId: ArrayList<Int>?) {
        binding.progressBar.visibility = View.VISIBLE
        binding.contentContainer.visibility = View.GONE

        lifecycleScope.launch {
            if (movieId != null) {
                movieViewModel.listMovieReviews(movieId.toInt()).collect {
                    movieReviewsAdapterPaging.submitData(it)
                }
            }
        }

        movieViewModel.genres.observe(this) { genreList ->
            val genres = genresId?.mapNotNull { getGenresNameById(it, genreList) }
            if (genres != null) {
                genresAdapterMovie2.setData(genres)
            }
        }
        movieViewModel.genreResult.observe(this) {
            when (it) {
                is MovieViewModel.GenreResult.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.contentContainer.visibility = View.VISIBLE
                    binding.disconnetedContainer.root.visibility = View.GONE
                }
                is MovieViewModel.GenreResult.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    binding.contentContainer.visibility = View.GONE
                    binding.disconnetedContainer.root.visibility = View.VISIBLE
                    binding.disconnetedContainer.tvError.text = it.error
                    binding.disconnetedContainer.btnRefreshConnection.setOnClickListener {
                        finish()
                        overridePendingTransition(0, 0)
                        startActivity(intent)
                        overridePendingTransition(0, 0)
                    }
                }
            }
        }
        movieViewModel.getGenres()
    }

    private fun setUpViewModel(movieId: Int, getResult: (key: String) -> Unit) {
        movieViewModel.movieVideos.observe(this) {
            getResult(it.random()?.key.toString())
        }
        movieViewModel.getMovieVideos(movieId)
    }
}