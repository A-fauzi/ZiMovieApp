package com.afauzi.zimovieapp.presentation.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.data.remote.MovieApiProvider
import com.afauzi.zimovieapp.data.repository.MovieRepository
import com.afauzi.zimovieapp.databinding.ActivityMainBinding
import com.afauzi.zimovieapp.domain.modelentities.Movie
import com.afauzi.zimovieapp.presentation.adapter.MovieAdapterPaging
import com.afauzi.zimovieapp.presentation.view.auth.SignInActivity
import com.afauzi.zimovieapp.presentation.view.main.fragment.AccountFragment
import com.afauzi.zimovieapp.presentation.view.main.fragment.BookmarkFragment
import com.afauzi.zimovieapp.presentation.view.main.fragment.HomeFragment
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.movie.MovieViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigation = binding.bottomNav
        setupWithNavController(bottomNavigation, navController)
//        setUpBottomNav()
    }

    private fun setUpBottomNav() {
        loadFragment(HomeFragment(), this)
        bottomNavigationView = binding.bottomNav
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.homeFragment -> {
                    loadFragment(HomeFragment(), this)
                    true
                }
                R.id.bookmarkFragment -> {
                    loadFragment(BookmarkFragment(), this)
                    true
                }
                R.id.accountFragment -> {
                    loadFragment(AccountFragment(), this)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment, fragmentActivity: FragmentActivity) {
        val transaction = fragmentActivity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}