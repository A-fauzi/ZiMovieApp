package com.afauzi.zimovieapp.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.afauzi.zimovieapp.databinding.ActivityLandingBinding
import com.afauzi.zimovieapp.view.auth.SignInActivity
import com.afauzi.zimovieapp.view.auth.SignUpActivity

class LandingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

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
}