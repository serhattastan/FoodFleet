package com.cloffygames.foodfleet.data.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(private val firebaseAuth: FirebaseAuth) {
    // Giriş yapmış olan kullanıcıyı almak
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    // Giriş yapma fonksiyonu
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }

    // Kayıt olma fonksiyonu
    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null)
                } else {
                    onResult(false, task.exception?.message)
                }
            }
    }
}
