package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import com.cloffygames.foodfleet.data.entity.User
import com.cloffygames.foodfleet.data.repo.CartRepository
import com.cloffygames.foodfleet.data.repo.FirebaseFoodRepository
import com.cloffygames.foodfleet.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * CategoryDetailScreenViewModel, kategoriye göre yemeklerin listelenmesi ve kullanıcı işlemleri için kullanılır.
 * Firebase'den yiyecek verilerini çeker ve sepet işlemlerini yönetir.
 */
@HiltViewModel
class CategoryDetailScreenViewModel @Inject constructor(
    private val firebaseFoodRepository: FirebaseFoodRepository, // Firebase'den yemek verilerini almak için kullanılan repository
    private val userRepository: UserRepository, // Kullanıcı verilerini almak için kullanılan repository
    private val cartRepository: CartRepository // Sepet işlemlerini yönetmek için kullanılan repository
) : ViewModel() {

    // Firebase'den çekilen yemek listesi LiveData olarak tutulur
    val firebaseFoodList = firebaseFoodRepository.getFoodsFromFireStore()

    /**
     * Kullanıcı bilgilerini almak için bir fonksiyon.
     *
     * @param onSuccess Kullanıcı bilgilerini geri döner
     * @param onFailure Hata durumunda Exception döner
     */
    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) = userRepository.getUser(onSuccess, onFailure)

    /**
     * Bir yiyeceği sepete eklemek veya güncellemek için kullanılan fonksiyon.
     *
     * @param yemek_adi Eklenecek yiyeceğin adı
     * @param yemek_resim_adi Eklenecek yiyeceğin resim adı
     * @param yemek_fiyat Eklenecek yiyeceğin fiyatı
     * @param yemek_siparis_adet Sepete eklenecek yiyecek miktarı
     * @param kullanici_adi Sepete ekleyen kullanıcının adı
     */
    fun addFoodToCart(yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int, kullanici_adi: String) {
        // Ana iş parçacığında sepet verisini güncelle
        CoroutineScope(Dispatchers.Main).launch {
            cartRepository.updateOrAddFoodToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        }
    }
}