package com.cloffygames.foodfleet.data.datasource

import com.cloffygames.foodfleet.data.entity.Cart
import com.cloffygames.foodfleet.retrofit.CartDao
import kotlinx.coroutines.Dispatchers
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
        return@withContext cartDao.getCartFoods(kullanici_adi).sepet_yemekler
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
        yemek_fiyat: String,
        yemek_siparis_adet: String,
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
}
