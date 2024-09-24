package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloffygames.foodfleet.data.entity.User
import com.cloffygames.foodfleet.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ProfileDetailViewModel, kullanıcı profili ile ilgili işlemleri yöneten ViewModel sınıfıdır.
 * Kullanıcı bilgilerini eklemek ve yönetmek için UserRepository ile etkileşimde bulunur.
 *
 * @param userRepository Kullanıcı verilerini yönetmek için kullanılan repository
 */
@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val userRepository: UserRepository // Kullanıcı işlemlerini yöneten repository
) : ViewModel() {

    /**
     * Kullanıcı eklemek için kullanılan fonksiyon.
     *
     * @param user Eklenmek istenen kullanıcı nesnesi
     * @param onSuccess Başarılı olunduğunda çalıştırılacak lambda fonksiyonu
     * @param onFailure Hata olduğunda çalıştırılacak lambda fonksiyonu
     */
    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        // Arka planda çalışan coroutine içinde kullanıcıyı ekler
        viewModelScope.launch {
            userRepository.addUser(user, onSuccess, onFailure)
        }
    }
}
