package com.cloffygames.foodfleet.data.datasource

import android.util.Log
import com.cloffygames.foodfleet.data.entity.Cart
import com.cloffygames.foodfleet.retrofit.CartDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

/**
 * CartDataSource, sepetle ilgili verilerin sunucudan alınmasını ve yönetilmesini sağlar.
 * Bu sınıf, Retrofit DAO ile sunucuya API istekleri yaparak sepet işlemlerini yönetir.
 *
 * @param cartDao Retrofit kullanarak sepet işlemlerini gerçekleştiren DAO.
 */
class CartDataSource(private val cartDao: CartDao) {

    /**
     * Kullanıcının sepetindeki yemekleri sunucudan getirir.
     * İşlem, IO dispatcher'da arka planda çalıştırılır.
     *
     * @param kullanici_adi Sepet sahibi kullanıcının adı.
     * @return Kullanıcının sepetindeki yemekleri içeren liste.
     */
    suspend fun getCartFoods(kullanici_adi: String): List<Cart> = withContext(Dispatchers.IO) {
        try {
            val response = cartDao.getCartFoods(kullanici_adi)
            Log.e("API Yanıtı", response.toString()) // Gelen yanıtı logla

            val cartFoods = response.sepet_yemekler ?: emptyList()

            if (cartFoods.isEmpty()) {
                Log.e("Sepet Bilgisi", "Sepet boş veya yemekler bulunamadı.")
            }

            return@withContext cartFoods
        } catch (e: Exception) {
            Log.e("Hata", "API'den veri çekilirken hata oluştu: ${e.message}")
            return@withContext emptyList<Cart>()
        }
    }



    /**
     * Kullanıcının sepetine yeni bir yemek ekler.
     * API isteğiyle yemek verileri sunucuya gönderilir.
     *
     * @param yemek_adi Eklenecek yemeğin adı.
     * @param yemek_resim_adi Yemeğin resim dosyasının adı.
     * @param yemek_fiyat Yemeğin fiyatı.
     * @param yemek_siparis_adet Sipariş edilen yemek miktarı.
     * @param kullanici_adi Sepet sahibi kullanıcının adı.
     */
    suspend fun addFoodToCart(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) {
        cartDao.addFoodToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
    }

    /**
     * Kullanıcının sepetinden bir yemek siler.
     * Silinecek yemeğin ID'si ve kullanıcı adı API isteğiyle sunucuya iletilir.
     *
     * @param sepet_yemek_id Silinecek yemeğin sepet ID'si.
     * @param kullanici_adi Sepet sahibi kullanıcının adı.
     */
    suspend fun deleteFoodFromCart(sepet_yemek_id: Int, kullanici_adi: String) {
        cartDao.deleteFoodFromCart(sepet_yemek_id, kullanici_adi)
    }

    suspend fun updateOrAddFoodToCart(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: Int,
        yemek_siparis_adet: Int,
        kullanici_adi: String
    ) {
        // Mevcut sepetteki yemekleri al
        val currentCartFoods = getCartFoods(kullanici_adi)

        // Aynı adı taşıyan yemek var mı kontrol et
        val existingFood = currentCartFoods.find { it.yemek_adi == yemek_adi }

        if (existingFood != null) {
            // Eğer aynı yemek varsa, fiyat ve adet toplanarak güncellenir
            val updated_fiyat = existingFood.yemek_fiyat + yemek_fiyat
            val updated_adet = existingFood.yemek_siparis_adet + yemek_siparis_adet

            // Eski yemek silinir
            deleteFoodFromCart(existingFood.sepet_yemek_id, kullanici_adi)

            // Güncellenmiş yemek tekrar sepete eklenir
            cartDao.addFoodToCart(yemek_adi, yemek_resim_adi, updated_fiyat, updated_adet, kullanici_adi)
        } else {
            // Aynı yemek yoksa, direkt ekle
            cartDao.addFoodToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)
        }
    }

    suspend fun increaseFoodQuantity(cartItem: Cart) {
        withContext(Dispatchers.IO) {
            try {
                // Sepetteki mevcut öğeyi sil
                deleteFoodFromCart(cartItem.sepet_yemek_id, cartItem.kullanici_adi)

                // Adeti artır
                val updatedQuantity = cartItem.yemek_siparis_adet + 1
                val updatedPrice: Int
                if(cartItem.yemek_siparis_adet > 1){
                    updatedPrice = (cartItem.yemek_fiyat / cartItem.yemek_siparis_adet) * updatedQuantity
                    Log.e("Fiyat", updatedPrice.toString())
                }else{
                    updatedPrice = cartItem.yemek_fiyat * updatedQuantity
                }


                // Yeni adet ile tekrar ekle
                cartDao.addFoodToCart(
                    yemek_adi = cartItem.yemek_adi,
                    yemek_resim_adi = cartItem.yemek_resim_adi,
                    yemek_fiyat = updatedPrice,
                    yemek_siparis_adet = updatedQuantity,
                    kullanici_adi = cartItem.kullanici_adi
                )
            } catch (e: Exception) {
                Log.e("Hata", "Yemek artırma işleminde hata: ${e.message}")
            }
        }
    }

    suspend fun decreaseFoodQuantity(cartItem: Cart) {
        withContext(Dispatchers.IO) {
            if (cartItem.yemek_siparis_adet > 1) {
                try {
                    // Sepetteki mevcut öğeyi sil
                    deleteFoodFromCart(cartItem.sepet_yemek_id, cartItem.kullanici_adi)

                    // Adeti azalt
                    val updatedQuantity = cartItem.yemek_siparis_adet - 1
                    val updatedPrice = (cartItem.yemek_fiyat / cartItem.yemek_siparis_adet) * updatedQuantity

                    // Yeni adet ile tekrar ekle
                    cartDao.addFoodToCart(
                        yemek_adi = cartItem.yemek_adi,
                        yemek_resim_adi = cartItem.yemek_resim_adi,
                        yemek_fiyat = updatedPrice,
                        yemek_siparis_adet = updatedQuantity,
                        kullanici_adi = cartItem.kullanici_adi
                    )
                } catch (e: Exception) {
                    Log.e("Hata", "Yemek azaltma işleminde hata: ${e.message}")
                }
            }
        }
    }

    suspend fun deleteAllFoodsFromCart(kullanici_adi: String, cartFoodList: List<Cart>) {
        withContext(Dispatchers.IO) {
            cartFoodList.forEach { cartItem ->
                try {
                    // Yemek sepetten silinir
                    cartDao.deleteFoodFromCart(cartItem.sepet_yemek_id, kullanici_adi)
                    Log.d("Sepet", "Yemek başarıyla silindi: ${cartItem.yemek_adi}")

                } catch (e: Exception) {
                    Log.e("Sepet", "Yemek silme hatası: ${e.message}")
                }
            }
        }
    }
}
