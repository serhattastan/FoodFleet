package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.entity.Food
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
 * SearchViewModel, arama ekranında kullanılan yemek ve kategori verilerini yönetir.
 * Firebase ve REST API'den veri çekerek UI'a sunar. Ayrıca sepet işlemlerini de yönetir.
 *
 * @param firebaseFoodRepository Firestore'dan yemek verilerini sağlayan repository.
 * @param frepo REST API'den yemek verilerini sağlayan repository.
 * @param cartRepository Sepet işlemlerini yöneten repository.
 * @param userRepo Kullanıcı verilerini yöneten repository.
 */
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firebaseFoodRepository: FirebaseFoodRepository, // Firebase'den yemek verilerini sağlar
    private val frepo: FoodRepository, // REST API'den yemek verilerini sağlar
    private val cartRepository: CartRepository, // Sepet işlemlerini yönetir
    private val userRepo: UserRepository // Kullanıcı bilgilerini yönetir
): ViewModel() {

    // Firebase'den çekilen yemek listesi LiveData olarak tutulur
    val firebaseFoodList: LiveData<List<FirebaseFood>> = firebaseFoodRepository.getFoodsFromFireStore()

    // Firebase'deki yemek kategorileri ve resim URL'leri LiveData olarak tutulur
    val firebaseCategoryList: LiveData<Map<String, String>> = firebaseFoodRepository.getCategoriesWithImageUrls()

    // REST API veya başka kaynaklardan gelen yemek verileri için MutableLiveData
    val foodList = MutableLiveData<List<Food>>()

    /**
     * REST API'dan yemek verilerini getirip foodList'e atar.
     * Bu işlem arka planda yapılır ve UI katmanında sonuç gösterilir.
     */
    fun getFoods() {
        CoroutineScope(Dispatchers.Main).launch {
            foodList.value = frepo.getFoods() // API'den yemek verilerini al ve LiveData'ya ata
        }
    }

    /**
     * Sepete yemek ekleyen veya güncelleyen fonksiyon.
     *
     * @param yemek_adi Sepete eklenmek istenen yemeğin adı
     * @param yemek_resim_adi Yemeğin resmi veya URL'si
     * @param yemek_fiyat Yemeğin fiyatı
     * @param yemek_siparis_adet Sepete eklenecek yemek miktarı
     * @param kullanici_adi Oturum açmış kullanıcının adı
     */
    fun addFoodToCart(yemek_adi: String, yemek_resim_adi: String, yemek_fiyat: Int, yemek_siparis_adet: Int, kullanici_adi: String){
        CoroutineScope(Dispatchers.Main).launch {
            cartRepository.updateOrAddFoodToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi) // Sepete yemek ekle veya güncelle
        }
    }

    /**
     * Kullanıcı bilgilerini getiren fonksiyon.
     *
     * @param onSuccess Başarılı olursa kullanıcıyı geri döner
     * @param onFailure Hata olursa Exception döner
     */
    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) = userRepo.getUser(onSuccess, onFailure)
}