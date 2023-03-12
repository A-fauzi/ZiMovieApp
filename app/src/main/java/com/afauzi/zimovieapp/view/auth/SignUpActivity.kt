package com.afauzi.zimovieapp.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import com.afauzi.zimovieapp.databinding.ActivitySignUpBinding
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText

    private fun initView() {
        etEmail = binding.etEmail
        etUsername = binding.etUsername
        etPassword = binding.etPassword
        etConfirmPassword = binding.etConfirmPassword
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

        etEmail.addTextChangedListener(textWatcher(etEmail))
        etUsername.addTextChangedListener(textWatcher(etUsername))
        etPassword.addTextChangedListener(textWatcher(etPassword))
        etConfirmPassword.addTextChangedListener(textWatcher(etConfirmPassword))
    }

    override fun onStart() {
        super.onStart()

        binding.btnNextRegister.setOnClickListener {
            Toast.makeText(this, "Sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.ivBtnCreateByGoogle.setOnClickListener {
            Toast.makeText(this, "Sedang dalam pengembangan", Toast.LENGTH_SHORT).show()
        }
        binding.cvBtnSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
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