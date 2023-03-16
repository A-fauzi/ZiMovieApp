package com.afauzi.zimovieapp.presentation.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.afauzi.zimovieapp.databinding.ActivityLandingBinding
import com.afauzi.zimovieapp.presentation.view.auth.SignInActivity
import com.afauzi.zimovieapp.presentation.view.auth.SignUpActivity
import com.afauzi.zimovieapp.presentation.view.main.MainActivity
import com.google.firebase.auth.FirebaseAuth

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onStart() {
        super.onStart()
        currentUser()
        onClickView()
    }

    private fun onClickView() {
        binding.btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.ivBtnCreateByGoogle.setOnClickListener {
            Toast.makeText(this, "Sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.cvBtnSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    private fun currentUser() {
        val user = auth.currentUser
        user.let {
            if (it != null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun init() {
        auth = FirebaseAuth.getInstance()
    }
}