package com.cloffygames.foodfleet.uix.viewmodel

import android.util.Log
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

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val userRepository: UserRepository,
    private val foodRepository: FoodRepository,
    private val firebaseFoodRepository: FirebaseFoodRepository,
    private val firebaseCouponRepository: FirebaseCouponRepository
) : ViewModel() {
    // Firebase'den çekilen yemek listesi LiveData olarak tutulur
    val firebaseFoodList: LiveData<List<FirebaseFood>> = firebaseFoodRepository.getFoodsFromFireStore()
    // Firebase'den çekilen kupon listesi LiveData olarak tutulur
    val firebaseCouponList: LiveData<List<FirebaseCoupon>> = firebaseCouponRepository.getCouponsFromFireStore()
    // Diğer yemek verileri için MutableLiveData (REST API veya başka bir kaynaktan gelen)
    val foodList = MutableLiveData<List<Food>>()

    val cartFoods = MutableLiveData<List<Cart>>()

    // ViewModel başlatıldığında yemek verilerini getirir
    init {
        getFoods()
    }

    fun getCartFoods(kullanici_adi: String) {
        CoroutineScope(Dispatchers.Main).launch {
            cartFoods.value = cartRepository.getCartFoods(kullanici_adi)
        }

    }

    fun increaseFoodQuantity(cartItem: Cart) {
        viewModelScope.launch {
            cartRepository.increaseFoodQuantity(cartItem)
            getCartFoods(cartItem.kullanici_adi) // Güncellenmiş sepet verilerini tekrar getir
        }
    }

    fun decreaseFoodQuantity(cartItem: Cart) {
        if (cartItem.yemek_siparis_adet > 1) {
            viewModelScope.launch {
                cartRepository.decreaseFoodQuantity(cartItem)
                getCartFoods(cartItem.kullanici_adi) // Güncellenmiş sepet verilerini tekrar getir
            }
        }
    }



    fun deleteFoodFromCart(sepet_yemek_id: Int, kullanici_adi: String){
        CoroutineScope(Dispatchers.Main).launch {
            cartRepository.deleteFoodFromCart(sepet_yemek_id, kullanici_adi)
            getCartFoods(kullanici_adi) // Sepetten yemek silindikten sonra güncellenen sepet verisini tekrar alıyoruz
        }
    }

    private fun getFoods() {
        CoroutineScope(Dispatchers.Main).launch {
            foodList.value = foodRepository.getFoods()
        }
    }

    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) = userRepository.getUser(onSuccess, onFailure)
}