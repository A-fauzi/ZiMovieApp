package com.afauzi.zimovieapp.presentation.view.main.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.local.AppDatabase
import com.afauzi.zimovieapp.data.repository.MovieBookmarkRepository
import com.afauzi.zimovieapp.databinding.FragmentBookmarkBinding
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieBookmarkViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieBookmarkViewModelFactory

class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var viewModel: MovieBookmarkViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBookmarkBinding.inflate(layoutInflater, container, false)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getCachedMovieToBookmark().observe(requireActivity()) { list ->
            if (list.isEmpty()) Toast.makeText(requireActivity(), "Belum ada data favourite", Toast.LENGTH_SHORT).show()
            else list.forEach {
                Log.d("LIST_BOOKMARK", it.title.toString())
            }
        }
    }

    private fun initView() {
        val movieBookmarkDao = AppDatabase.getInstance(requireActivity()).movieBookmarkDao()
        val repository = MovieBookmarkRepository(movieBookmarkDao)
        val viewModelFactory = MovieBookmarkViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MovieBookmarkViewModel::class.java]
    }

}