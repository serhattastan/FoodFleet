package com.cloffygames.foodfleet.data.repo

import com.cloffygames.foodfleet.data.datasource.CartDataSource

/**
 * CartRepository, sepetle ilgili işlemleri yönetir ve veri kaynağı (CartDataSource) ile iletişim kurar.
 * Bu sınıf, uygulamanın iş mantığı katmanında kullanılır ve sepet işlemlerini basitleştirir.
 *
 * @param cartDataSource Sepet verileriyle etkileşime geçmek için kullanılan veri kaynağı.
 */
class CartRepository(private val cartDataSource: CartDataSource) {

    /**
     * Kullanıcının sepetindeki yemekleri getirir.
     *
     * @param kullanici_adi Sepet sahibi kullanıcının adı.
     * @return Kullanıcının sepetindeki yemekleri içeren yanıt.
     */
    suspend fun getCartFoods(kullanici_adi: String) = cartDataSource.getCartFoods(kullanici_adi)

    /**
     * Sepete yeni bir yemek ekler.
     *
     * @param yemek_adi Eklenecek yemeğin adı.
     * @param yemek_resim_adi Yemeğin resim dosyasının adı.
     * @param yemek_fiyat Yemeğin fiyatı.
     * @param yemek_siparis_adet Sipariş edilen yemek miktarı.
     * @param kullanici_adi Sepeti oluşturan kullanıcının adı.
     * @return Yemek ekleme işleminin sonucunu içeren yanıt.
     */
    suspend fun addFoodToCart(
        yemek_adi: String,
        yemek_resim_adi: String,
        yemek_fiyat: String,
        yemek_siparis_adet: String,
        kullanici_adi: String
    ) = cartDataSource.addFoodToCart(yemek_adi, yemek_resim_adi, yemek_fiyat, yemek_siparis_adet, kullanici_adi)

    /**
     * Sepetten bir yemek siler.
     *
     * @param sepet_yemek_id Silinecek yemeğin sepet ID'si.
     * @param kullanici_adi Sepet sahibi kullanıcının adı.
     * @return Yemek silme işleminin sonucunu içeren yanıt.
     */
    suspend fun deleteFoodFromCart(sepet_yemek_id: Int, kullanici_adi: String) = cartDataSource.deleteFoodFromCart(sepet_yemek_id, kullanici_adi)
}
