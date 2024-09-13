package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import com.cloffygames.foodfleet.data.repo.AuthenticationRepository
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepo: AuthenticationRepository) : ViewModel() {

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        authRepo.login(email, password, onResult)
    }

    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        authRepo.register(email, password, onResult)
    }

    // Giriş yapmış kullanıcıyı kontrol eden fonksiyon
    fun getCurrentUser(): FirebaseUser? {
        return authRepo.getCurrentUser()
    }

    // Oturum açmış kullanıcı olup olmadığını kontrol etme
    fun isUserLoggedIn(): Boolean {
        return getCurrentUser() != null
    }
}