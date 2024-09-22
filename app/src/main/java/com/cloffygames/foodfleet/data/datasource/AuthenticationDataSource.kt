package com.cloffygames.foodfleet.data.datasource

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

/**
 * AuthenticationDataSource sınıfı, Firebase Authentication ve Google Sign-In işlemlerini yönetir.
 * Kullanıcı oturum açma, kayıt olma ve Google ile oturum açma işlemleri içerir.
 *
 * @param firebaseAuth Firebase kimlik doğrulama işlemlerini yönetmek için kullanılan FirebaseAuth nesnesi.
 * @param googleSignInClient Google Sign-In işlemleri için kullanılan GoogleSignInClient nesnesi.
 */
class AuthenticationDataSource(private val firebaseAuth: FirebaseAuth, private val googleSignInClient: GoogleSignInClient) {

    /**
     * Geçerli oturum açmış kullanıcıyı döner.
     *
     * @return Geçerli oturum açmış kullanıcı, yoksa null.
     */
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    /**
     * Kullanıcı email ve şifre ile oturum açar.
     *
     * @param email Kullanıcı e-posta adresi.
     * @param password Kullanıcı şifresi.
     * @param onResult Oturum açma sonucunu dönen callback fonksiyonu.
     * onResult: (başarılı mı?, hata mesajı?)
     */
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null) // Oturum açma başarılı, hata mesajı yok
                } else {
                    onResult(false, task.exception?.message) // Oturum açma başarısız, hata mesajı mevcut
                }
            }
    }

    /**
     * Kullanıcı email ve şifre ile yeni bir hesap oluşturur.
     *
     * @param email Kullanıcı e-posta adresi.
     * @param password Kullanıcı şifresi.
     * @param onResult Kayıt olma sonucunu dönen callback fonksiyonu.
     * onResult: (başarılı mı?, hata mesajı?)
     */
    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null) // Kayıt başarılı, hata mesajı yok
                } else {
                    onResult(false, task.exception?.message) // Kayıt başarısız, hata mesajı mevcut
                }
            }
    }

    /**
     * Google Sign-In ile oturum açma işlemi yapar.
     *
     * @param token Google oturum açma işlemi için kullanılan token.
     * @param onResult Google ile oturum açma sonucunu dönen callback fonksiyonu.
     * onResult: (başarılı mı?, hata mesajı?)
     */
    fun signInWithGoogle(token: String, onResult: (Boolean, String?) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, null) // Google ile oturum açma başarılı, hata mesajı yok
                } else {
                    onResult(false, task.exception?.message) // Google ile oturum açma başarısız, hata mesajı mevcut
                }
            }
    }

    /**
     * Google Sign-In Client'ını döner.
     *
     * @return GoogleSignInClient nesnesi.
     */
    fun getGoogleSignInClient(): GoogleSignInClient {
        return googleSignInClient
    }

    fun logout(onResult: () -> Unit) {
        firebaseAuth.signOut()
        onResult()
    }

}