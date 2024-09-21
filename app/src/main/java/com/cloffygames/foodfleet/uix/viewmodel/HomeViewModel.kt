package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloffygames.foodfleet.data.entity.FirebaseCoupon
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.data.repo.FirebaseCouponRepository
import com.cloffygames.foodfleet.data.repo.FirebaseFoodRepository
import com.cloffygames.foodfleet.data.repo.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * HomeViewModel, yemek ve kupon verilerini yöneten ViewModel sınıfıdır.
 * Firebase'den ve API'dan gelen yemek ve kupon verilerini UI katmanına sunar.
 *
 * @param firebaseFoodRepository Firestore'dan yemek verilerini sağlayan repository.
 * @param frepo API yemek verilerini sağlayan repository.
 * @param firebaseCouponRepository Firestore'dan kupon verilerini sağlayan repository.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseFoodRepository: FirebaseFoodRepository,
    private val frepo: FoodRepository,
    private val firebaseCouponRepository: FirebaseCouponRepository
) : ViewModel() {

    // Firebase'den çekilen yemek listesi LiveData olarak tutulur
    val firebaseFoodList: LiveData<List<FirebaseFood>> = firebaseFoodRepository.getFoodsFromFireStore()

    // Firebase'deki yemek kategorileri LiveData olarak tutulur
    val firebaseCategoryList: LiveData<Map<String, String>> = firebaseFoodRepository.getCategoriesWithImageUrls()

    // Firebase'den çekilen kupon listesi LiveData olarak tutulur
    val firebaseCouponList: LiveData<List<FirebaseCoupon>> = firebaseCouponRepository.getCouponsFromFireStore()

    // Diğer yemek verileri için MutableLiveData (REST API veya başka bir kaynaktan gelen)
    val foodList = MutableLiveData<List<Food>>()

    // ViewModel başlatıldığında yemek verilerini getirir
    init {
        getFoods()
    }

    /**
     * REST API'dan yemek verilerini alır ve foodList'e atar.
     * Bu işlem arka planda yapılır (Main dispatcher ile UI'da sonuç gösterilir).
     */
    private fun getFoods() {
        CoroutineScope(Dispatchers.Main).launch {
            foodList.value = frepo.getFoods()
        }
    }
}
