package com.cloffygames.foodfleet.data.datasource

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.storage.FirebaseStorage

/**
 * FirebaseFoodDataSource, Firebase Firestore'dan yemek verilerini ve kategorilerini çeker.
 * Firestore üzerinde yemek koleksiyonuyla ilgili işlemler sağlar.
 *
 * @param collectionFoods Firestore'daki "yemekler" koleksiyonu referansı.
 */
class FirebaseFoodDataSource(private val collectionFoods: CollectionReference) {

    // Yemek verilerinin tutulacağı MutableLiveData
    private val firebaseFoodList = MutableLiveData<List<FirebaseFood>>()

    // Kategorilerin tutulacağı MutableLiveData
    private val firebaseCategoryList = MutableLiveData<Map<String, String>>()

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
    fun getCategoriesWithImageUrls(): MutableLiveData<Map<String, String>> {
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

                // Kategorileri işleyip resim URL'lerini alacağız
                val categoryImageMap = mutableMapOf<String, String>()

                for (category in categorySet) {
                    // Boşlukları "_" ile değiştir ve Türkçe karakterleri temizle
                    val formattedCategory = formatCategoryName(category)

                    // Resim adını Firebase Storage'da kullanarak URL'yi al
                    val storageRef = FirebaseStorage.getInstance().reference
                    val imageRef = storageRef.child("/$formattedCategory.png")

                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Anahtar: Kategori ismi, Değer: Resim URL'si
                        categoryImageMap[category] = uri.toString()
                        // MutableLiveData'yı güncelle
                        firebaseCategoryList.value = categoryImageMap
                    }.addOnFailureListener {
                        Log.e("Firebase Storage", "Resim yüklenemedi: $formattedCategory")
                    }
                }
            }
        }
        return firebaseCategoryList
    }

    // Kategori adını Türkçe karakterlerden ve boşluklardan arındıran fonksiyon
    fun formatCategoryName(category: String): String {
        // Türkçe karakterleri dönüştür
        val turkishChars = mapOf(
            'ç' to 'c', 'Ç' to 'C',
            'ğ' to 'g', 'Ğ' to 'G',
            'ı' to 'i', 'İ' to 'I',
            'ö' to 'o', 'Ö' to 'O',
            'ş' to 's', 'Ş' to 'S',
            'ü' to 'u', 'Ü' to 'U'
        )

        var formattedCategory = category

        // Boşlukları "_" ile değiştir
        formattedCategory = formattedCategory.replace(" ", "_")

        // Türkçe karakterleri İngilizce karşılıklarıyla değiştir
        for ((turkishChar, englishChar) in turkishChars) {
            formattedCategory = formattedCategory.replace(turkishChar, englishChar)
        }

        return formattedCategory
    }

}