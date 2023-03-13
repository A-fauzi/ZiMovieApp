package com.afauzi.zimovieapp.presentation.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.afauzi.zimovieapp.data.repository.AuthRepository
import com.afauzi.zimovieapp.databinding.ActivitySignUpBinding
import com.afauzi.zimovieapp.presentation.view.main.MainActivity
import com.afauzi.zimovieapp.presentation.viewmodel.auth.AuthViewModel
import com.afauzi.zimovieapp.presentation.viewmodel.auth.AuthViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    companion object {
        const val TAG = "SignUpActivity"
    }

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: AuthViewModel
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText


    private fun initView() {
        etEmail = binding.etEmail
        etUsername = binding.etUsername
        etPassword = binding.etPassword
        etConfirmPassword = binding.etConfirmPassword

        etEmail.addTextChangedListener(textWatcher(etEmail))
        etUsername.addTextChangedListener(textWatcher(etUsername))
        etPassword.addTextChangedListener(textWatcher(etPassword))
        etConfirmPassword.addTextChangedListener(textWatcher(etConfirmPassword))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        setUpViewModel()
    }

    override fun onStart() {
        super.onStart()

        binding.btnNextRegister.setOnClickListener {
            binding.btnNextRegister.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
            viewModel.createUserWithEmailPassword(etEmail.text.toString(), etPassword.text.toString())
        }
        binding.ivBtnCreateByGoogle.setOnClickListener {
            Toast.makeText(this, "Sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.cvBtnSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
    }

    private fun setUpViewModel() {
        val authRepository = AuthRepository(FirebaseAuth.getInstance())
        viewModel = ViewModelProvider(this, AuthViewModelProvider(authRepository))[AuthViewModel::class.java]

        viewModel.authResult.observe(this) { signUpResult ->
            when(signUpResult) {
                is AuthViewModel.AuthResult.Success -> {
                    Toast.makeText(this, signUpResult.msg, Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                is AuthViewModel.AuthResult.Failure -> {
                    Toast.makeText(this, signUpResult.error, Toast.LENGTH_SHORT).show()
                    binding.btnNextRegister.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

//    Fungsi validasi untuk input register
    private fun textWatcher(input: EditText) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            val text = p0.toString()
            when(input) {
                etEmail -> {
                    if (text.isEmpty()) {
                        binding.outlinedTextFieldEmail.error = "Email is required"
                        binding.outlinedTextFieldUsername.isEnabled = false
                    } else {
                        if (text.length < 3 ) {
                            binding.outlinedTextFieldEmail.error = "Email max 3 character"
                            binding.outlinedTextFieldUsername.isEnabled = false
                        } else {
                            binding.outlinedTextFieldEmail.isErrorEnabled = false
                            if (!text.contains("@gmail.com")) {
                                binding.outlinedTextFieldEmail.error = "Email character is not valid"
                                binding.outlinedTextFieldUsername.isEnabled = false
                            } else {
                                binding.outlinedTextFieldEmail.endIconMode = TextInputLayout.END_ICON_CUSTOM
                                binding.outlinedTextFieldUsername.isEnabled = true
                            }
                        }
                    }
                }
                etUsername -> {
                    if (text.isEmpty()) {
                        binding.outlinedTextFieldUsername.error = "Username is required"
                        binding.outlinedTextFielPassword.isEnabled = false
                    } else {
                        if (text.length < 3 ) {
                            binding.outlinedTextFieldUsername.error = "Username max 3 character"
                            binding.outlinedTextFielPassword.isEnabled = false
                        } else {
                            binding.outlinedTextFieldUsername.isErrorEnabled = false
                            binding.outlinedTextFieldUsername.endIconMode = TextInputLayout.END_ICON_CUSTOM
                            binding.outlinedTextFielPassword.isEnabled = true
                        }
                    }
                }
                etPassword -> {
                    if (text.isEmpty()) {
                        binding.outlinedTextFielPassword.error = "Password is required"
                        binding.outlinedTextFielConfirmPassword.isEnabled = false
                    } else {
                        if (text.length < 8 ) {
                            binding.outlinedTextFielPassword.error = "Password max 8 character"
                            binding.outlinedTextFielConfirmPassword.isEnabled = false
                        } else {
                            binding.outlinedTextFielPassword.isErrorEnabled = false
                            binding.outlinedTextFielPassword.endIconMode = TextInputLayout.END_ICON_CUSTOM
                            binding.outlinedTextFielConfirmPassword.isEnabled = true
                        }
                    }
                }
                etConfirmPassword -> {
                    if (text.isEmpty()) {
                        binding.outlinedTextFielConfirmPassword.error = "Password Confirmation is required"
                        binding.btnNextRegister.isEnabled = false
                    } else {
                        if (text.length < 8 ) {
                            binding.outlinedTextFielConfirmPassword.error = "Password Confirmation max 8 character"
                            binding.btnNextRegister.isEnabled = false
                        } else {
                           if (text != etPassword.text.toString()) {
                               binding.outlinedTextFielConfirmPassword.error = "Password Confirmation is not match"
                               binding.btnNextRegister.isEnabled = false
                           } else {
                               binding.outlinedTextFielConfirmPassword.isErrorEnabled = false
                               binding.outlinedTextFielConfirmPassword.endIconMode = TextInputLayout.END_ICON_CUSTOM
                               binding.btnNextRegister.isEnabled = true
                           }
                        }
                    }
                }
            }
        }
    }
}