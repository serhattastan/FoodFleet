package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import com.cloffygames.foodfleet.data.repo.AuthenticationRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
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

    fun signInWithGoogle(token: String, onResult: (Boolean, String?) -> Unit) {
        authRepo.signInWithGoogle(token, onResult)
    }

    fun getGoogleSignInClient(): GoogleSignInClient {
        return authRepo.getGoogleSignInClient()
    }

    // Giriş yapmış kullanıcıyı kontrol eden fonksiyon
    fun getCurrentUser(): FirebaseUser? {
        return authRepo.getCurrentUser()
    }

    fun isUserLoggedIn(): Boolean {
        return getCurrentUser() != null
    }
}
