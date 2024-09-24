package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
 * HomeViewModel, yemek ve kupon verilerini yöneten ViewModel sınıfıdır.
 * Firebase ve REST API'den gelen yemek ve kupon verilerini UI katmanına sunar.
 *
 * @param firebaseFoodRepository Firestore'dan yemek verilerini sağlayan repository.
 * @param frepo REST API'dan yemek verilerini sağlayan repository.
 * @param firebaseCouponRepository Firestore'dan kupon verilerini sağlayan repository.
 * @param userRepo Kullanıcı verilerini sağlayan repository.
 * @param cartRepository Sepet verilerini yöneten repository.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseFoodRepository: FirebaseFoodRepository, // Firebase'den yemek verilerini sağlar
    private val frepo: FoodRepository, // REST API'den yemek verilerini sağlar
    private val firebaseCouponRepository: FirebaseCouponRepository, // Firebase'den kupon verilerini sağlar
    private val userRepo: UserRepository, // Kullanıcı verilerini sağlar
    private val cartRepository: CartRepository // Sepet işlemlerini yönetir
) : ViewModel() {

    // Firebase'den çekilen yemek listesi LiveData olarak tutulur
    val firebaseFoodList: LiveData<List<FirebaseFood>> = firebaseFoodRepository.getFoodsFromFireStore()

    // Firebase'deki yemek kategorileri LiveData olarak tutulur
    val firebaseCategoryList: LiveData<Map<String, String>> = firebaseFoodRepository.getCategoriesWithImageUrls()

    // Firebase'den çekilen kupon listesi LiveData olarak tutulur
    val firebaseCouponList: LiveData<List<FirebaseCoupon>> = firebaseCouponRepository.getCouponsFromFireStore()

    // REST API veya başka kaynaklardan gelen yemek verileri için MutableLiveData
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
            foodList.value = frepo.getFoods() // API'den yemek verilerini al ve LiveData'ya ata
        }
    }

    /**
     * Kullanıcı bilgilerini getiren fonksiyon.
     *
     * @param onSuccess Başarılı olursa kullanıcıyı geri döner
     * @param onFailure Başarısız olursa hatayı geri döner
     */
    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) = userRepo.getUser(onSuccess, onFailure)

    /**
     * Belirtilen kullanıcı adına ait sepet yiyeceklerini getiren fonksiyon.
     *
     * @param kullanici_adi Kullanıcının adı
     */
    private fun getCardFoods(kullanici_adi: String) {
        CoroutineScope(Dispatchers.Main).launch {
            cartRepository.getCartFoods(kullanici_adi) // Sepetteki yiyecekleri getir
        }
    }

    /**
     * Sepete yemek ekleyen veya güncelleyen fonksiyon.
     *
     * @param yemek_adi Yemek adı
     * @param yemek_resim_adi Yemek resminin adı veya URL'si
     * @param yemek_fiyat Yemeğin fiyatı
     * @param yemek_siparis_adet Sepete eklenecek yemek miktarı
     * @param kullanici_adi Kullanıcının adı (oturum açmış kullanıcı)
     */
    fun addFoodToCart(yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int, kullanici_adi: String){
        CoroutineScope(Dispatchers.Main).launch {
            cartRepository.updateOrAddFoodToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi) // Sepeti güncelle veya yeni yemek ekle
        }
    }

    /**
     * Sepetten bir yiyeceği silen fonksiyon.
     *
     * @param sepet_yemek_id Sepetteki yiyeceğin ID'si
     * @param kullanici_adi Kullanıcının adı
     */
    fun deleteFoodFromCart(sepet_yemek_id: Int, kullanici_adi: String){
        CoroutineScope(Dispatchers.Main).launch {
            cartRepository.deleteFoodFromCart(sepet_yemek_id, kullanici_adi) // Sepetten yiyeceği sil
        }
    }
}