package com.afauzi.zimovieapp.presentation.view.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.ActivityDetailMovieBinding
import com.afauzi.zimovieapp.domain.modelentities.genre.Genre
import com.afauzi.zimovieapp.presentation.adapter.GenresAdapterMovie2
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import com.bumptech.glide.Glide

class DetailMovieActivity : AppCompatActivity(), GenresAdapterMovie2.ListenerAdapterGenre {
    private lateinit var binding: ActivityDetailMovieBinding

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movieApiService: MovieApiService
    private lateinit var movieRepository: MovieRepository
    private lateinit var movieViewModelFactory: MovieViewModelFactory
    private lateinit var genresAdapterMovie2: GenresAdapterMovie2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundleExtras = intent.extras
        val backdrop = bundleExtras?.getString("backDrop")
        val title = bundleExtras?.getString("title")
        val overview = bundleExtras?.getString("overview")
        val voteAverage = bundleExtras?.getString("voteAverage")
        val genresId = intent.getIntegerArrayListExtra("genresId")

        binding.tvItemVoteAverage.text = voteAverage
        binding.collapsingToolbar.title = title
        Glide.with(this).load(MovieApiProvider.BASE_URL_PATH + backdrop).placeholder(R.drawable.image_placeholder_50).into(binding.ivBackdropCollapse)
        if (overview != null) {
            if (overview.isNotEmpty()) binding.tvOverView.text = overview
            if (overview.isEmpty()) binding.tvOverView.text = "Is Not Overview"
        }
        binding.fabFavCollapsing.setOnClickListener {
            Toast.makeText(this, "your like $title", Toast.LENGTH_SHORT).show()
            binding.fabFavCollapsing.setImageResource(R.drawable.ic_star)
        }

        genresAdapterMovie2 = GenresAdapterMovie2(arrayListOf(), this)

        binding.rvGenres.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = genresAdapterMovie2
        }

        movieApiService = MovieApiProvider.provideMovieApiService()
        movieRepository = MovieRepository(movieApiService)
        movieViewModelFactory = MovieViewModelFactory(movieRepository, movieApiService)

        movieViewModel = ViewModelProvider(this, movieViewModelFactory)[MovieViewModel::class.java]
        movieViewModel.genres.observe(this) { genreList ->
            val genres = genresId?.mapNotNull { getGenresNameById(it, genreList) }
            if (genres != null) {
                genresAdapterMovie2.setData(genres)
            }
        }
        movieViewModel.getGenres()

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
}