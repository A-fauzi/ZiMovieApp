package com.afauzi.zimovieapp.presentation.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.afauzi.zimovieapp.R
import com.afauzi.zimovieapp.databinding.ActivityMainBinding
import androidx.navigation.ui.NavigationUI.setupWithNavController

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigation = binding.bottomNav
        setupWithNavController(bottomNavigation, navController)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}