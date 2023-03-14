package com.afauzi.zimovieapp.presentation.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.databinding.ActivityDetailMovieBinding
import com.bumptech.glide.Glide

class DetailMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMovieBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundleExtras = intent.extras
        val backdrop = bundleExtras?.getString("backDrop")
        val title = bundleExtras?.getString("title")
        val overview = bundleExtras?.getString("overview")

        binding.collapsingToolbar.title = title
        Glide.with(this).load(MovieApiProvider.BASE_URL_PATH + backdrop).placeholder(R.drawable.image_placeholder_50).into(binding.ivBackdropCollapse)
        if (overview != null) {
            if (overview.isNotEmpty()) binding.tvOverView.text = overview
            if (overview.isEmpty()) binding.tvOverView.text = "Is Not Overview"
        }
//        binding.ivBtnFav.setOnClickListener {
//            Toast.makeText(this, "your like $title", Toast.LENGTH_SHORT).show()
//            binding.ivBtnFav.setImageResource(R.drawable.ic_star)
//        }
    }
}