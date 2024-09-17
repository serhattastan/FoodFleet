package com.cloffygames.foodfleet.data.entity

/**
 * FirebaseFood, Firebase Firestore'dan alınan yemek verilerini temsil eden veri sınıfıdır.
 * Her bir yemek için gerekli olan temel bilgileri içerir.
 *
 * @property yemek_id Yemek için benzersiz ID. Varsayılan değer 0.
 * @property yemek_adi Yemeğin adı. Varsayılan değer boş bir string.
 * @property yemek_resim_adi Yemeğe ait resim dosya adı. Varsayılan değer boş bir string.
 * @property yemek_kategori Yemeğin ait olduğu kategori. Varsayılan değer boş bir string.
 * @property yemek_fiyat Yemeğin fiyatı. Varsayılan değer 0.0.
 */
data class FirebaseFood(
    val yemek_id: Int = 0,               // Yemeğin benzersiz ID'si
    val yemek_adi: String = "",          // Yemeğin adı
    val yemek_resim_adi: String = "",    // Yemeğe ait resim dosya adı
    val yemek_kategori: String = "",     // Yemeğin kategorisi
    val yemek_fiyat: Double = 0.0        // Yemeğin fiyatı
)