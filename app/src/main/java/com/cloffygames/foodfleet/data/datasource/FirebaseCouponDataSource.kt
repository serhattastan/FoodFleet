package com.cloffygames.foodfleet.data.datasource

import androidx.lifecycle.MutableLiveData
import com.cloffygames.foodfleet.data.entity.FirebaseCoupon
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

/**
 * FirebaseCouponDataSource, Firebase Firestore'dan kupon verilerini çeken veri kaynağıdır.
 * Bu sınıf, Firestore'daki kupon verilerini alarak MutableLiveData içinde tutar.
 *
 * @param collectionCoupons Firestore'daki kuponlar koleksiyon referansı.
 */
class FirebaseCouponDataSource @Inject constructor(
    private val collectionCoupons: CollectionReference
) {

    // Kuponların listesi için MutableLiveData
    private val firebaseCouponList = MutableLiveData<List<FirebaseCoupon>>()

    /**
     * Firestore'dan kuponları çeker ve kuponları MutableLiveData içinde döner.
     * Firestore üzerinde değişiklik olduğunda anlık güncellemeler sağlar.
     *
     * @return Firestore'dan gelen kuponların bulunduğu MutableLiveData.
     */
    fun getCouponsFromFireStore(): MutableLiveData<List<FirebaseCoupon>> {
        // Firestore koleksiyonundaki verilerde değişiklik olduğunda dinleyici çalışır
        collectionCoupons.addSnapshotListener { value, error ->
            if (value != null) {
                val list = ArrayList<FirebaseCoupon>()  // Kuponları tutacak geçici liste
                val documents = value.documents
                // Her bir dökümanı FirebaseCoupon sınıfına dönüştürüp listeye ekler
                for (document in documents) {
                    val coupon = document.toObject(FirebaseCoupon::class.java)
                    if (coupon != null) {
                        list.add(coupon)
                    }
                }
                // Güncellenen listeyi MutableLiveData'ya atar
                firebaseCouponList.value = list
            }
        }
        return firebaseCouponList
    }
}
