package com.cloffygames.foodfleet.data.repo

import com.cloffygames.foodfleet.data.datasource.AuthenticationDataSource
import javax.inject.Inject

/**
 * AuthenticationRepository, AuthenticationDataSource'tan gelen kimlik doğrulama işlemlerini yönetir.
 * Bu repository, veri kaynağından (data source) gelen işlemleri daha üst katmanlara sunar.
 *
 * @param ads AuthenticationDataSource nesnesi.
 */
class AuthenticationRepository @Inject constructor(
    var ads: AuthenticationDataSource
) {
    /**
     * Geçerli oturum açmış kullanıcıyı döner.
     *
     * @return Geçerli kullanıcı, yoksa null.
     */
    fun getCurrentUser() = ads.getCurrentUser()

    /**
     * Kullanıcı email ve şifre ile oturum açar.
     *
     * @param email Kullanıcı e-posta adresi.
     * @param password Kullanıcı şifresi.
     * @param onResult Oturum açma sonucunu dönen callback fonksiyonu.
     * onResult: (başarılı mı?, hata mesajı?)
     */
    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) =
        ads.login(email, password, onResult)

    /**
     * Kullanıcı email ve şifre ile yeni bir hesap oluşturur.
     *
     * @param email Kullanıcı e-posta adresi.
     * @param password Kullanıcı şifresi.
     * @param onResult Kayıt olma sonucunu dönen callback fonksiyonu.
     * onResult: (başarılı mı?, hata mesajı?)
     */
    fun register(email: String, password: String, onResult: (Boolean, String?) -> Unit) =
        ads.register(email, password, onResult)

    /**
     * Google Sign-In ile oturum açma işlemi yapar.
     *
     * @param token Google oturum açma işlemi için kullanılan token.
     * @param onResult Google ile oturum açma sonucunu dönen callback fonksiyonu.
     * onResult: (başarılı mı?, hata mesajı?)
     */
    fun signInWithGoogle(token: String, onResult: (Boolean, String?) -> Unit) =
        ads.signInWithGoogle(token, onResult)

    /**
     * Google Sign-In Client'ını döner.
     *
     * @return GoogleSignInClient nesnesi.
     */
    fun getGoogleSignInClient() = ads.getGoogleSignInClient()
}
