package com.afauzi.zimovieapp.presentation.view.main

import android.os.Bundle
import android.widget.Toast
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.databinding.ActivityVideoPlayerBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

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