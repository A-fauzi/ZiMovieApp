package com.afauzi.zimovieapp.presentation.viewmodel.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.afauzi.zimovieapp.data.repository.AuthRepository

class AuthViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult>
        get() = _authResult

    fun createUserWithEmailPassword(email: String, password: String) {
        authRepository.createUserWithEmailAndPassword(email, password, object : AuthRepository.CreateUserListener{
            override fun onSuccess(msg: String) {
                _authResult.postValue(AuthResult.Success(msg))
            }

            override fun onFailure(error: String) {
                _authResult.postValue(AuthResult.Failure(error))
            }

        })
    }

    fun signInWithEmailAndPassword(email: String, password: String){
        authRepository.signInWithEmailAndPassword(email, password, object : AuthRepository.CreateUserListener{
            override fun onSuccess(msg: String) {
                _authResult.postValue(AuthResult.Success(msg))
            }

            override fun onFailure(error: String) {
                _authResult.postValue(AuthResult.Failure(error))
            }

        })
    }
    sealed class AuthResult {
        class Success(val msg: String) : AuthResult()
        class Failure(val error: String) : AuthResult()
    }
}