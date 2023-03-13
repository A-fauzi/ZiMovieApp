package com.afauzi.zimovieapp.data.repository

import com.google.firebase.auth.FirebaseAuth

class AuthRepository(private val auth: FirebaseAuth) {
    interface CreateUserListener {
        fun onSuccess(msg: String)
        fun onFailure(error: String)
    }
    fun createUserWithEmailAndPassword(email: String, password: String, listener: CreateUserListener) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener.onSuccess(it.exception?.message ?: "Successfull create user")
                } else {
                    listener.onFailure(it.exception?.message ?: "Unknown error occurred")
                }
            }
            .addOnFailureListener {
                listener.onFailure(it.message ?: "Unknown error occurred")
            }
    }

    fun signInWithEmailAndPassword(email: String, password: String, listener: CreateUserListener) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener.onSuccess(it.exception?.message ?: "Successfull create user")
                } else {
                    listener.onFailure(it.exception?.message ?: "Unknown error occurred")
                }
            }
            .addOnFailureListener {
                listener.onFailure(it.message ?: "Unknown error occurred")
            }
    }
}