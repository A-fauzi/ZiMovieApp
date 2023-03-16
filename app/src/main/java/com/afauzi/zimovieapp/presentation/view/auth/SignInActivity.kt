package com.afauzi.zimovieapp.presentation.view.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.afauzi.zimovieapp.data.repository.AuthRepository
import com.afauzi.zimovieapp.databinding.ActivitySignInBinding
import com.afauzi.zimovieapp.presentation.view.main.MainActivity
import com.afauzi.zimovieapp.presentation.viewmodel.auth.AuthViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.auth.AuthViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    private fun initView() {
        etEmail = binding.etEmail
        etPassword = binding.etPassword
        etEmail.addTextChangedListener(textWatcher(etEmail))
        etPassword.addTextChangedListener(textWatcher(etPassword))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setUpViewModel()
    }

    override fun onStart() {
        super.onStart()
        onCLickView()
    }

    private fun onCLickView() {
        binding.cvBtnForgotPassword.setOnClickListener {
            Toast.makeText(this, "Sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.btnSignIn.setOnClickListener {
            binding.btnSignIn.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            viewModel.signInWithEmailAndPassword(
                etEmail.text.toString(),
                etPassword.text.toString()
            )
        }
        binding.ivBtnSignInByGoogle.setOnClickListener {
            Toast.makeText(this, "Sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.cvBtnSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    private fun setUpViewModel() {
        val authRepository = AuthRepository(FirebaseAuth.getInstance())
        viewModel = ViewModelProvider(this, AuthViewModelProvider(authRepository))[AuthViewModel::class.java]

        viewModel.authResult.observe(this) { authResult ->
            when(authResult) {
                is AuthViewModel.AuthResult.Success -> {
                    Toast.makeText(this, authResult.msg, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                is AuthViewModel.AuthResult.Failure -> {
                    Toast.makeText(this, authResult.error, Toast.LENGTH_SHORT).show()
                    binding.btnSignIn.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun textWatcher(input: EditText) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when(input) {
                etEmail -> {
                    if (text.isEmpty()) {
                        binding.outlinedTextFieldEmail.error = "Email is required"
                        binding.outlinedTextFielPassword.isEnabled = false
                    } else {
                        if (text.length < 3 ) {
                            binding.outlinedTextFieldEmail.error = "Email max 3 character"
                            binding.outlinedTextFielPassword.isEnabled = false
                        } else {
                            binding.outlinedTextFieldEmail.isErrorEnabled = false
                            if (!text.contains("@gmail.com")) {
                                binding.outlinedTextFieldEmail.error = "Email character is not valid"
                                binding.outlinedTextFielPassword.isEnabled = false
                            } else {
                                binding.outlinedTextFieldEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM
                                binding.outlinedTextFielPassword.isEnabled = true
                            }
                        }
                    }
                }
                etPassword -> {
                    if (text.isEmpty()) {
                        binding.outlinedTextFielPassword.error = "Password is required"
                        binding.btnSignIn.isEnabled = false
                    } else {
                        if (text.length < 8 ) {
                            binding.outlinedTextFielPassword.error = "Password max 8 character"
                            binding.btnSignIn.isEnabled = false
                        } else {
                            binding.outlinedTextFielPassword.isErrorEnabled = false
                            binding.outlinedTextFielPassword.endIconMode = TextInputLayout.END_ICON_CUSTOM
                            binding.btnSignIn.isEnabled = true
                        }
                    }
                }
            }
        }

    }
}