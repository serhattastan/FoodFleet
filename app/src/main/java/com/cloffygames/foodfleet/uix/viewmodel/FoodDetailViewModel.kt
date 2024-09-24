package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.entity.User
import com.cloffygames.foodfleet.data.repo.CartRepository
import com.cloffygames.foodfleet.data.repo.FirebaseFoodRepository
import com.cloffygames.foodfleet.data.repo.FoodRepository
import com.cloffygames.foodfleet.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * FoodDetailViewModel, yemek detayları ekranında kullanılan veri ve işlemleri yönetir.
 * Firebase'den ve diğer kaynaklardan yiyecek verilerini alır ve kullanıcıya ait sepet işlemlerini yönetir.
 */
@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val foodRepository: FoodRepository, // REST API'den yiyecek verilerini almak için kullanılan repository
    private val firebaseFoodRepository: FirebaseFoodRepository, // Firebase'den yemek ve kategori verilerini almak için kullanılan repository
    private val cartRepository: CartRepository, // Sepet işlemleri için kullanılan repository
    private val userRepository: UserRepository // Kullanıcı bilgilerini almak için kullanılan repository
): ViewModel() {

    // Firebase'den çekilen yemek listesi LiveData olarak tutulur
    val firebaseFoodList: LiveData<List<FirebaseFood>> = firebaseFoodRepository.getFoodsFromFireStore()

    // Firebase'deki yemek kategorileri, resim URL'leri ile birlikte LiveData olarak tutulur
    val firebaseCategoryList: LiveData<Map<String, String>> = firebaseFoodRepository.getCategoriesWithImageUrls()

    /**
     * Kullanıcı bilgilerini getiren fonksiyon.
     *
     * @param onSuccess Başarılı olursa kullanıcıyı geri döner
     * @param onFailure Başarısız olursa hatayı geri döner
     */
    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) = userRepository.getUser(onSuccess, onFailure)

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
        // Ana iş parçacığında sepet güncellemesi yapılır
        CoroutineScope(Dispatchers.Main).launch {
            cartRepository.updateOrAddFoodToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        }
    }
}