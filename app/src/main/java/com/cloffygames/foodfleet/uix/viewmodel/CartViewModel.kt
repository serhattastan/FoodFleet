package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloffygames.foodfleet.data.entity.Cart
import com.cloffygames.foodfleet.data.entity.FirebaseCoupon
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.data.entity.User
import com.cloffygames.foodfleet.data.repo.CartRepository
import com.cloffygames.foodfleet.data.repo.FirebaseCouponRepository
import com.cloffygames.foodfleet.data.repo.FirebaseFoodRepository
import com.cloffygames.foodfleet.data.repo.FoodRepository
import com.cloffygames.foodfleet.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * CartViewModel, sepet işlemlerini ve Firebase'den alınan yemek ve kupon verilerini yönetir.
 * Ayrıca kullanıcının oturum bilgileri ve sepetteki yiyecekleri içerir.
 */
@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository, // Sepetle ilgili veri işlemlerini yapan repository
    private val userRepository: UserRepository, // Kullanıcı bilgilerini sağlayan repository
    private val foodRepository: FoodRepository, // REST API'den yemek verilerini sağlayan repository
    private val firebaseFoodRepository: FirebaseFoodRepository, // Firebase'den yemek verilerini sağlayan repository
    private val firebaseCouponRepository: FirebaseCouponRepository // Firebase'den kupon verilerini sağlayan repository
) : ViewModel() {

    // Firebase'den çekilen yemek listesi LiveData olarak tutulur
    val firebaseFoodList: LiveData<List<FirebaseFood>> = firebaseFoodRepository.getFoodsFromFireStore()

    // Firebase'den çekilen kupon listesi LiveData olarak tutulur
    val firebaseCouponList: LiveData<List<FirebaseCoupon>> = firebaseCouponRepository.getCouponsFromFireStore()

    // REST API veya başka kaynaklardan gelen yemek verileri için MutableLiveData
    val foodList = MutableLiveData<List<Food>>()

    // Sepetteki yiyecekler listesi MutableLiveData olarak tutulur
    val cartFoods = MutableLiveData<List<Cart>>()

    /**
     * ViewModel başlatıldığında yemek verilerini getirir.
     */
    init {
        getFoods()
    }

    /**
     * Belirtilen kullanıcıya ait sepet verilerini getirir.
     *
     * @param kullanici_adi Kullanıcının adı
     */
    fun getCartFoods(kullanici_adi: String) {
        CoroutineScope(Dispatchers.Main).launch {
            cartFoods.value = cartRepository.getCartFoods(kullanici_adi) // Sepetteki yiyecekleri getir ve LiveData'ya ata
        }
    }

    /**
     * Sepetteki bir yiyeceğin miktarını artırır.
     *
     * @param cartItem Sepetteki yiyecek
     */
    fun increaseFoodQuantity(cartItem: Cart) {
        viewModelScope.launch {
            cartRepository.increaseFoodQuantity(cartItem)
            getCartFoods(cartItem.kullanici_adi) // Güncellenen sepet verilerini tekrar getir
        }
    }

    /**
     * Sepetteki bir yiyeceğin miktarını azaltır. Yiyecek miktarı 1'den az olamaz.
     *
     * @param cartItem Sepetteki yiyecek
     */
    fun decreaseFoodQuantity(cartItem: Cart) {
        if (cartItem.yemek_siparis_adet > 1) {
            viewModelScope.launch {
                cartRepository.decreaseFoodQuantity(cartItem)
                getCartFoods(cartItem.kullanici_adi) // Güncellenen sepet verilerini tekrar getir
            }
        }
    }

    /**
     * Sepetten bir yiyeceği siler.
     *
     * @param sepet_yemek_id Sepetteki yiyeceğin ID'si
     * @param kullanici_adi Kullanıcının adı
     */
    fun deleteFoodFromCart(sepet_yemek_id: Int, kullanici_adi: String) {
        CoroutineScope(Dispatchers.Main).launch {
            cartRepository.deleteFoodFromCart(sepet_yemek_id, kullanici_adi)
            getCartFoods(kullanici_adi) // Sepetten silinen yiyeceğin ardından güncellenen sepet verilerini getir
        }
    }

    /**
     * REST API'den yemek verilerini getirir ve foodList'e atar.
     */
    private fun getFoods() {
        CoroutineScope(Dispatchers.Main).launch {
            foodList.value = foodRepository.getFoods()
        }
    }

    /**
     * Kullanıcı bilgilerini getirir.
     *
     * @param onSuccess Başarılı olursa kullanıcıyı geri döner
     * @param onFailure Başarısız olursa hatayı geri döner
     */
    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) = userRepository.getUser(onSuccess, onFailure)
}