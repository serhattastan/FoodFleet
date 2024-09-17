package com.cloffygames.foodfleet.data.datasource

import androidx.lifecycle.MutableLiveData
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.google.firebase.firestore.CollectionReference

/**
 * FirebaseFoodDataSource, Firebase Firestore'dan yemek verilerini ve kategorilerini çeker.
 * Firestore üzerinde yemek koleksiyonuyla ilgili işlemler sağlar.
 *
 * @param collectionFoods Firestore'daki "yemekler" koleksiyonu referansı.
 */
class FirebaseFoodDataSource(val collectionFoods: CollectionReference) {

    // Yemek verilerinin tutulacağı MutableLiveData
    val firebaseFoodList = MutableLiveData<List<FirebaseFood>>()

    // Kategorilerin tutulacağı MutableLiveData
    val firebaseCategoryList = MutableLiveData<List<String>>()

    val firebaseFoodsByCategoryList = MutableLiveData<List<FirebaseFood>>()

    /**
     * Firestore'dan tüm yemekleri alır ve firebaseFoodList içinde depolar.
     *
     * @return Firestore'dan gelen yemeklerin bulunduğu MutableLiveData.
     */
    fun getFoodsFromFireStore(): MutableLiveData<List<FirebaseFood>> {
        // Firestore'da yemekler koleksiyonuna her ekleme/güncelleme olduğunda bu listener çalışır
        collectionFoods.addSnapshotListener { value, error ->
            if (value != null) {
                val list = ArrayList<FirebaseFood>()
                val documents = value.documents
                // Gelen dökümanları listeye ekler
                for (d in documents) {
                    val food = d.toObject(FirebaseFood::class.java)
                    if (food != null) {
                        list.add(food)
                    }
                }
                firebaseFoodList.value = list // Verileri MutableLiveData'ya aktarır
            }
        }
        return firebaseFoodList
    }

    /**
     * Firestore'daki yemeklerin kategorilerini alır ve benzersiz kategorileri firebaseCategoryList'e ekler.
     *
     * @return Firestore'dan gelen benzersiz yemek kategorilerinin bulunduğu MutableLiveData.
     */
    fun getCategoriesFromFoods(): MutableLiveData<List<String>> {
        // Firestore'daki tüm yemekleri dinler ve kategorilerini toplar
        collectionFoods.addSnapshotListener { value, error ->
            if (value != null) {
                val categorySet = HashSet<String>() // Benzersiz kategoriler için set kullanılır
                val documents = value.documents
                // Her bir dökümandan kategoriyi alır ve sete ekler
                for (d in documents) {
                    val food = d.toObject(FirebaseFood::class.java)
                    if (food != null) {
                        categorySet.add(food.yemek_kategori)
                    }
                }
                firebaseCategoryList.value = ArrayList(categorySet) // Kategorileri MutableLiveData'ya aktarır
            }
        }
        return firebaseCategoryList
    }
}