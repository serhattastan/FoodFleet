package com.cloffygames.foodfleet.data.repo

import androidx.lifecycle.MutableLiveData
import com.cloffygames.foodfleet.data.datasource.FirebaseFoodDataSource
import com.cloffygames.foodfleet.data.entity.FirebaseFood

/**
 * FirebaseFoodRepository, uygulama ile veri kaynağı (FirebaseFoodDataSource) arasındaki iletişimi sağlar.
 * Firebase'den gelen yemek verilerini iş mantığı katmanına sunar.
 *
 * @param fds FirebaseFoodDataSource veri kaynağı.
 */
class FirebaseFoodRepository(
    private var fds: FirebaseFoodDataSource
) {

    /**
     * Firebase'den tüm yemekleri çeker.
     *
     * @return MutableLiveData içinde yemek listesi döner.
     */
    fun getFoodsFromFireStore(): MutableLiveData<List<FirebaseFood>> = fds.getFoodsFromFireStore()

    /**
     * Firebase'deki yemeklerin kategorilerini çeker.
     *
     * @return MutableLiveData içinde benzersiz kategoriler listesi döner.
     */
    fun getCategoriesFromFoods(): MutableLiveData<List<String>> = fds.getCategoriesFromFoods()
}