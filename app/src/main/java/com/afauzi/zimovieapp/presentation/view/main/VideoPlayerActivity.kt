package com.afauzi.zimovieapp.presentation.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.remote.MovieApiService
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.ActivityVideoPlayerBinding
import com.afauzi.zimovieapp.presentation.adapter.GenresAdapterMovie
import com.afauzi.zimovieapp.presentation.adapter.MovieAdapterPaging
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.google.firebase.auth.FirebaseAuth
import io.paperdb.Paper
import kotlinx.coroutines.launch

class VideoPlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {
    private lateinit var binding: ActivityVideoPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ytPv.initialize(MovieApiProvider.YOUTUBE_API_KEY, this)
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        if (!p2) {
            val movieKey = intent.getStringExtra("movieKey")
            p1?.loadVideo(movieKey, 0)
//            p1?.cueVideo(movieKey);
        }
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(this, "Youtube Api Initialization Failure", Toast.LENGTH_SHORT).show()
    }
}