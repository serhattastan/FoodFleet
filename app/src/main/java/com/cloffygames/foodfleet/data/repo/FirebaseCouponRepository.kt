package com.cloffygames.foodfleet.data.repo

import androidx.lifecycle.MutableLiveData
import com.cloffygames.foodfleet.data.datasource.FirebaseCouponDataSource
import com.cloffygames.foodfleet.data.entity.FirebaseCoupon

/**
 * FirebaseCouponRepository, FirebaseCouponDataSource ile veri katmanı arasında köprü görevi görür.
 * Bu sınıf, veri kaynağından alınan kuponları iş mantığı katmanına veya ViewModel'e sunar.
 *
 * @param firebaseCouponDataSource Firebase Firestore'dan kupon verilerini almak için kullanılan veri kaynağı.
 */
class FirebaseCouponRepository(
    private val firebaseCouponDataSource: FirebaseCouponDataSource
) {

    /**
     * FirebaseCouponDataSource'dan gelen kuponları MutableLiveData formatında döner.
     * Bu yöntem kupon verilerini Firestore'dan çekerek uygulama katmanına sunar.
     *
     * @return Firestore'dan gelen kuponların bulunduğu MutableLiveData.
     */
    fun getCouponsFromFireStore(): MutableLiveData<List<FirebaseCoupon>> {
        return firebaseCouponDataSource.getCouponsFromFireStore()
    }
}
