package com.cloffygames.foodfleet.retrofit

import com.cloffygames.foodfleet.data.entity.CRUDResponse
import com.cloffygames.foodfleet.data.entity.CartResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * CartDao, yemek sepeti işlemleri için Retrofit arayüzünü tanımlar.
 * Sunucuya istek göndererek sepet verilerini yönetir ve yanıtları alır.
 */
interface CartDao {

    /**
     * Kullanıcının sepetindeki yemekleri getirir.
     * Sepetteki yemekler sunucudan CartResponse formatında döner.
     *
     * @param kullanici_adi Sepet sahibi kullanıcının adı.
     * @return Kullanıcının sepetindeki yemekleri içeren CartResponse.
     */
    @POST("yemekler/sepettekiYemekleriGetir.php")
    @FormUrlEncoded
    suspend fun getCartFoods(
        @Field("kullanici_adi") kullanici_adi: String
    ): CartResponse

    /**
     * Kullanıcının sepetine yemek ekler.
     * Yemek ekleme işlemi sonucunda sunucudan CRUDResponse döner.
     *
     * @param yemek_adi Eklenecek yemeğin adı.
     * @param yemek_resim_adi Yemeğin resim dosyasının adı.
     * @param yemek_fiyat Yemeğin fiyatı.
     * @param yemek_siparis_adet Sipariş edilen yemek miktarı.
     * @param kullanici_adi Sepet sahibi kullanıcının adı.
     * @return İşlemin başarılı olup olmadığını gösteren CRUDResponse.
     */
    @POST("yemekler/sepeteYemekEkle.php")
    @FormUrlEncoded
    suspend fun addFoodToCart(
        @Field("yemek_adi") yemek_adi: String,
        @Field("yemek_resim_adi") yemek_resim_adi: String,
        @Field("yemek_fiyat") yemek_fiyat: Int,
        @Field("yemek_siparis_adet") yemek_siparis_adet: Int,
        @Field("kullanici_adi") kullanici_adi: String
    ): CRUDResponse

    /**
     * Kullanıcının sepetinden yemek siler.
     * Yemek silme işlemi sonucunda sunucudan CRUDResponse döner.
     *
     * @param sepet_yemek_id Silinecek yemeğin sepet ID'si.
     * @param kullanici_adi Sepet sahibi kullanıcının adı.
     * @return İşlemin başarılı olup olmadığını gösteren CRUDResponse.
     */
    @POST("yemekler/sepettenYemekSil.php")
    @FormUrlEncoded
    suspend fun deleteFoodFromCart(
        @Field("sepet_yemek_id") sepet_yemek_id: Int,
        @Field("kullanici_adi") kullanici_adi: String
    ): CRUDResponse
}
