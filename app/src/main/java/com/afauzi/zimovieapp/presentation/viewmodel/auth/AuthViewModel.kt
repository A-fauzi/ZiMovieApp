package com.afauzi.zimovieapp.presentation.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afauzi.zimovieapp.data.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult>
        get() = _signUpResult

    fun createUserWithEmailPassword(email: String, password: String) {
        authRepository.createUserWithEmailAndPassword(email, password, object : AuthRepository.CreateUserListener{
            override fun onSuccess(msg: String) {
                _signUpResult.postValue(SignUpResult.Success(msg))
            }

            override fun onFailure(error: String) {
                _signUpResult.postValue(SignUpResult.Failure(error))
            }

        })
    }

    sealed class SignUpResult {
        class Success(val msg: String) : SignUpResult()
        class Failure(val error: String) : SignUpResult()
    }
}