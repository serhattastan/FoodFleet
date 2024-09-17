package com.cloffygames.foodfleet.data.repo

import com.cloffygames.foodfleet.data.datasource.FoodDataSource
import com.cloffygames.foodfleet.data.entity.Food

/**
 * FoodRepository, veri kaynağı (FoodDataSource) ile uygulamanın diğer katmanları arasında bir köprü görevi görür.
 * Bu sınıf, yemek verilerini veri kaynağından (FoodDataSource) alır ve iş mantığı katmanına sunar.
 *
 * @param fds Yemek verilerini almak için kullanılan FoodDataSource nesnesi.
 */
class FoodRepository(val fds: FoodDataSource) {

    /**
     * Veri kaynağından (API) yemek verilerini alır.
     *
     * @return Yemeklerin listesi.
     */
    suspend fun getFoods(): List<Food> = fds.getFoods()

}
