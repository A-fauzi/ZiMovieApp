package com.afauzi.zimovieapp.presentation.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.ActivityDetailMovieBinding
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre
import com.afauzi.zimovieapp.presentation.adapter.AdapterGenresMovie2
import com.afauzi.zimovieapp.presentation.adapter.AdapterMovieReviewsPaging
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class DetailMovieActivity : AppCompatActivity(), AdapterGenresMovie2.ListenerAdapterGenre {
    private lateinit var binding: ActivityDetailMovieBinding

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieApiService: MovieApiService
    private lateinit var movieRepository: MovieRepository
    private lateinit var movieViewModelFactory: MovieViewModelFactory
    private lateinit var genresAdapterMovie2: AdapterGenresMovie2
    private lateinit var movieReviewsAdapterPaging: AdapterMovieReviewsPaging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    override fun onStart() {
        super.onStart()

        val bundleExtras = intent.extras
        val movieId = bundleExtras?.getString("movieId")
        val backdrop = bundleExtras?.getString("backDrop")
        val title = bundleExtras?.getString("title")
        val overview = bundleExtras?.getString("overview")
        val voteAverage = bundleExtras?.getString("voteAverage")
        val genresId = intent.getIntegerArrayListExtra("genresId")

        setView {
            binding.tvItemVoteAverage.text = voteAverage
            binding.collapsingToolbar.title = title
            Glide.with(this).load(MovieApiProvider.BASE_URL_PATH + backdrop).placeholder(R.drawable.image_placeholder_50).into(binding.ivBackdropCollapse)
            if (overview != null) {
                if (overview.isNotEmpty()) binding.tvOverView.text = overview
                if (overview.isEmpty()) binding.tvOverView.text = "Is Not Overview"
            }
        }

        onClickView(movieId, title)
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
            adapter = movieReviewsAdapterPaging
        }
    }

    private fun setView(view: () -> Unit) {view()}

    private fun onClickView(movieId: String?, title: String?) {
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
            Toast.makeText(this, "your like $title", Toast.LENGTH_SHORT).show()
            binding.fabFavCollapsing.setImageResource(R.drawable.ic_star)
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
    }

    private fun setUpViewModel(movieId: String?, genresId: ArrayList<Int>?) {
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
        movieViewModel.getGenres()
    }

    private fun setUpViewModel(movieId: Int, getResult: (key: String) -> Unit) {
        movieViewModel.movieVideos.observe(this) {
            getResult(it.random()?.key.toString())
        }
        movieViewModel.getMovieVideos(movieId)
    }
}