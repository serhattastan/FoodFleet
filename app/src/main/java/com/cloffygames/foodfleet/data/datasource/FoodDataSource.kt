package com.cloffygames.foodfleet.data.datasource

import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.retrofit.FoodDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * FoodDataSource, FoodDao kullanarak yemek verilerini API'dan alır.
 * Bu sınıf, API çağrılarını gerçekleştirmek için Retrofit'in sunduğu asenkron yapıyı kullanır.
 *
 * @param fdao API ile yemek verilerini almak için kullanılan FoodDao nesnesi.
 */
class FoodDataSource(val fdao: FoodDao) {

    /**
     * API'dan tüm yemek verilerini çeker.
     * Bu işlem IO dispatcher'da arka planda gerçekleştirilir.
     *
     * @return API'dan gelen yemeklerin listesi.
     */
    suspend fun getFoods(): List<Food> = withContext(Dispatchers.IO) {
        // API çağrısını yaparak tüm yemek verilerini alır ve döner
        return@withContext fdao.getFoods().yemekler
    }
}
