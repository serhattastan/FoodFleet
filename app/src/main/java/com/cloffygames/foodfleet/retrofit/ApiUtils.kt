package com.cloffygames.foodfleet.retrofit

/**
 * ApiUtils sınıfı, uygulamanın Retrofit API istemcisi ile etkileşim kurmasını sağlayan yardımcı sınıftır.
 * API çağrılarını gerçekleştirmek için gerekli olan temel URL ve diğer ayarları içerir.
 */
class ApiUtils {
    companion object {
        // API'nın temel URL'si
        val BASE_URL = "http://kasimadalan.pe.hu/"

        // API'nın spesifik endpoint'ini temsil eden URL
        val API_URL = "yemekler/tumYemekleriGetir.php"

        /**
         * FoodDao'yu sağlayan Retrofit istemcisini döner.
         *
         * @return Retrofit kullanarak oluşturulan FoodDao nesnesi.
         */
        fun getFoodDao(): FoodDao {
            // Retrofit istemcisi oluşturulur ve FoodDao arayüzü kullanıma sunulur
            return RetrofitClient.getClient(BASE_URL).create(FoodDao::class.java)
        }
    }
}
