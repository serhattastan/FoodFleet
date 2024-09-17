package com.cloffygames.foodfleet.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * RetrofitClient, Retrofit istemcisini oluşturmak için kullanılan bir yardımcı sınıftır.
 * Bu sınıf, Retrofit ile yapılacak API çağrılarına uygun bir istemci sağlar.
 */
class RetrofitClient {
    companion object {
        /**
         * Belirtilen `baseUrl` ile Retrofit istemcisini oluşturur.
         *
         * @param baseUrl API'nın temel URL'si.
         * @return Retrofit istemcisi.
         */
        fun getClient(baseUrl: String): Retrofit {
            return Retrofit
                .Builder()
                .baseUrl(baseUrl)  // API çağrıları için temel URL
                .addConverterFactory(GsonConverterFactory.create())  // JSON verilerini dönüştürmek için Gson kullanır
                .build()  // Retrofit nesnesini oluşturur
        }
    }
}
