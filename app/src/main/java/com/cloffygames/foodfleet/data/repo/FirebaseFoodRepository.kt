package com.cloffygames.foodfleet.data.repo

import com.cloffygames.foodfleet.data.datasource.FirebaseFoodDataSource
import javax.inject.Inject

/**
 * FirebaseFoodRepository, uygulama ile veri kaynağı (FirebaseFoodDataSource) arasındaki iletişimi sağlar.
 * Firebase'den gelen yemek verilerini iş mantığı katmanına sunar.
 *
 * @param fds FirebaseFoodDataSource veri kaynağı.
 */
class FirebaseFoodRepository @Inject constructor(
    var fds: FirebaseFoodDataSource
) {

    /**
     * Firebase'den tüm yemekleri çeker.
     *
     * @return MutableLiveData içinde yemek listesi döner.
     */
    fun getFoodsFromFireStore() = fds.getFoodsFromFireStore()

    /**
     * Verilen kategoriye göre yemekleri çeker.
     *
     * @param category Aranan yemek kategorisi.
     * @return MutableLiveData içinde belirtilen kategoriye ait yemek listesi döner.
     */
    fun getFoodsByCategory(category: String) = fds.getFoodsByCategory(category)

    /**
     * Firebase'deki yemeklerin kategorilerini çeker.
     *
     * @return MutableLiveData içinde benzersiz kategoriler listesi döner.
     */
    fun getCategoriesFromFoods() = fds.getCategoriesFromFoods()
}