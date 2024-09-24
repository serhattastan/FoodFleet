package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloffygames.foodfleet.data.entity.User
import com.cloffygames.foodfleet.data.repo.AuthenticationRepository
import com.cloffygames.foodfleet.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ProfileViewModel, kullanıcı profilini yönetmek ve güncellemek için kullanılan ViewModel'dir.
 * Kullanıcı bilgilerini alır, günceller ve çıkış yapma işlemlerini yönetir.
 *
 * @param userRepository Kullanıcı bilgilerini yönetmek için kullanılan repository
 * @param authenticationRepository Kullanıcı oturum açma/kapama işlemlerini yöneten repository
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository, // Kullanıcı verilerini yöneten repository
    private val authenticationRepository: AuthenticationRepository // Oturum açma/kapama işlemlerini yöneten repository
) : ViewModel() {

    /**
     * Kullanıcı bilgilerini getiren fonksiyon.
     *
     * @param onSuccess Kullanıcı başarıyla alındığında çalışacak fonksiyon
     * @param onFailure Hata oluştuğunda çalışacak fonksiyon
     */
    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        // Arka planda kullanıcı verilerini almak için coroutine başlatır
        viewModelScope.launch {
            userRepository.getUser(onSuccess, onFailure)
        }
    }

    /**
     * Kullanıcı bilgilerini güncelleyen fonksiyon.
     *
     * @param user Güncellenmek istenen kullanıcı nesnesi
     * @param onSuccess Başarılı olduğunda çalışacak fonksiyon
     * @param onFailure Hata oluştuğunda çalışacak fonksiyon
     */
    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        // Arka planda kullanıcı güncellemesi yapmak için coroutine başlatır
        viewModelScope.launch {
            userRepository.updateUser(user, onSuccess, onFailure)
        }
    }

    /**
     * Kullanıcıyı oturumdan çıkaran fonksiyon.
     *
     * @param onSuccess Başarılı olduğunda çalışacak fonksiyon
     */
    fun logout(onSuccess: () -> Unit) {
        // Arka planda oturumu kapatmak için coroutine başlatır
        viewModelScope.launch {
            authenticationRepository.logout(onSuccess)
        }
    }
}