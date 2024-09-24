package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import com.cloffygames.foodfleet.data.repo.AuthenticationRepository
import com.cloffygames.foodfleet.data.repo.UserRepository
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * AuthViewModel, uygulamadaki kimlik doğrulama işlemlerini yönetir.
 * AuthenticationRepository kullanılarak Google ve e-posta ile kimlik doğrulama işlevlerini sağlar.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepo: AuthenticationRepository, private val userRepo: UserRepository) : ViewModel() {

    /**
     * Kullanıcı e-posta ve şifre ile giriş yapar.
     *
     * @param email Kullanıcının e-posta adresi
     * @param password Kullanıcının şifresi
     * @param onResult Sonucu geri döndüren lambda fonksiyonu
     */
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        authRepo.login(email, password, onResult)
    }

    /**
     * Kullanıcıyı e-posta ve şifre ile kaydeder.
     *
     * @param email Kullanıcının e-posta adresi
     * @param password Kullanıcının şifresi
     * @param onResult Sonucu geri döndüren lambda fonksiyonu
     */
    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        authRepo.register(email, password, onResult)
    }

    /**
     * Google hesabı ile giriş yapar.
     *
     * @param token Google'dan alınan token
     * @param onResult Sonucu geri döndüren lambda fonksiyonu
     */
    fun signInWithGoogle(token: String, onResult: (Boolean, String?) -> Unit) {
        authRepo.signInWithGoogle(token, onResult)
    }

    /**
     * Google giriş istemcisini döner.
     *
     * @return GoogleSignInClient nesnesi
     */
    fun getGoogleSignInClient(): GoogleSignInClient {
        return authRepo.getGoogleSignInClient()
    }

    /**
     * Mevcut oturum açmış kullanıcıyı döner.
     *
     * @return FirebaseUser nesnesi veya null
     */
    fun getCurrentUser(): FirebaseUser? {
        return authRepo.getCurrentUser()
    }

    /**
     * Kullanıcının oturum açıp açmadığını kontrol eder.
     *
     * @return Boolean değeri, true ise kullanıcı oturum açmış
     */
    fun isUserLoggedIn(): Boolean {
        return getCurrentUser() != null
    }

    fun checkIfUserExists(uid: String, onResult: (Boolean) -> Unit) {
        userRepo.getUserData(uid) { user ->
            if (user != null) {
                onResult(true) // Kullanıcı mevcut
            } else {
                onResult(false) // Kullanıcı yok, ilk defa giriş yapıyor
            }
        }
    }

}