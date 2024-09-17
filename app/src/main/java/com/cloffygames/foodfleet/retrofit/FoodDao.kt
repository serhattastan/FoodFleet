package com.cloffygames.foodfleet.retrofit

import com.cloffygames.foodfleet.data.entity.FoodResponse
import retrofit2.http.GET

/**
 * FoodDao, Retrofit ile yapılan API çağrılarını tanımlayan bir arayüzdür.
 * Bu arayüz, yemeklerle ilgili verilerin sunucudan alınmasını sağlar.
 */
interface FoodDao {

    /**
     * Tüm yemekleri sunucudan çeken API çağrısı.
     * Bu API, belirtilen endpoint'e (yemekler/tumYemekleriGetir.php) GET isteği yapar.
     *
     * @return Sunucudan dönen FoodResponse (yemek verilerini içeren yanıt).
     */
    @GET("yemekler/tumYemekleriGetir.php")
    suspend fun getFoods(): FoodResponse
}
