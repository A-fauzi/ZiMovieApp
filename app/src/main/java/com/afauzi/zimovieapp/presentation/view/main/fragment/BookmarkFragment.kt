package com.afauzi.zimovieapp.presentation.view.main.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.local.AppDatabase
import com.afauzi.zimovieapp.data.repository.MovieBookmarkRepository
import com.afauzi.zimovieapp.databinding.FragmentBookmarkBinding
import com.afauzi.zimovieapp.domain.modelentities.movie.MovieBookmark
import com.afauzi.zimovieapp.presentation.adapter.MovieBookmarkAdapter
import com.afauzi.zimovieapp.presentation.view.main.DetailMovieActivity
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieBookmarkViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieBookmarkViewModelFactory

class BookmarkFragment : Fragment(), MovieBookmarkAdapter.MovieBookmarkListener {

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var viewModel: MovieBookmarkViewModel
    private lateinit var bookmarkAdapter: MovieBookmarkAdapter

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

        // recylerview
        binding.rvBookmarkMovie.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bookmarkAdapter
        }

        viewModel.getCachedMovieToBookmark().observe(requireActivity()) { list ->
            if (list.isEmpty()) {
                binding.containerContent.visibility = View.GONE
                binding.tvNoItems.visibility = View.VISIBLE
            }
            else {
                binding.containerContent.visibility = View.VISIBLE
                binding.tvNoItems.visibility = View.GONE
                bookmarkAdapter.setData(list)
            }
        }
    }

    private fun initView() {
        val movieBookmarkDao = AppDatabase.getInstance(requireActivity()).movieBookmarkDao()
        val repository = MovieBookmarkRepository(movieBookmarkDao)
        val viewModelFactory = MovieBookmarkViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MovieBookmarkViewModel::class.java]
        bookmarkAdapter = MovieBookmarkAdapter(requireActivity(), arrayListOf(), this)
    }

    override fun onClickItem(data: MovieBookmark) {
        val bundle = Bundle()
        bundle.putString("movieId", data.id.toString())
        bundle.putString("backDrop", data.backdrop)
        bundle.putString("title", data.title)
        bundle.putString("overview", data.overView)
        bundle.putString("voteAverage", data.voteAverage.toString())

        val intent = Intent(requireActivity(), DetailMovieActivity::class.java)
        intent.putExtras(bundle)
//        intent.putIntegerArrayListExtra("genresId", data?.genreIds as ArrayList<Int?>?)
        startActivity(intent)
    }

}