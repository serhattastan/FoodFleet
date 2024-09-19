package com.cloffygames.foodfleet.retrofit

/**
 * ApiUtils sınıfı, uygulamanın Retrofit API istemcisi ile etkileşim kurmasını sağlayan yardımcı sınıftır.
 * API çağrılarını gerçekleştirmek için gerekli olan temel URL ve diğer ayarları içerir.
 * Bu sınıf, API isteklerini yapmak için kullanılan Retrofit DAO'ları sağlar.
 */
class ApiUtils {
    companion object {
        // API'nın temel URL'si
        val BASE_URL = "http://kasimadalan.pe.hu/"

        /**
         * Yemek verilerini almak için FoodDao'yu sağlayan Retrofit istemcisini döner.
         *
         * @return Retrofit kullanarak oluşturulan FoodDao nesnesi.
         */
        fun getFoodDao(): FoodDao {
            // Retrofit istemcisi oluşturulur ve FoodDao arayüzü kullanıma sunulur
            return RetrofitClient.getClient(BASE_URL).create(FoodDao::class.java)
        }

        /**
         * Sepet işlemleri için CartDao'yu sağlayan Retrofit istemcisini döner.
         *
         * @return Retrofit kullanarak oluşturulan CartDao nesnesi.
         */
        fun getCartDao(): CartDao {
            // Retrofit istemcisi oluşturulur ve CartDao arayüzü kullanıma sunulur
            return RetrofitClient.getClient(BASE_URL).create(CartDao::class.java)
        }
    }
}
