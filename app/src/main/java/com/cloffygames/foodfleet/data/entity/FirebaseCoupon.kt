package com.cloffygames.foodfleet.data.entity

/**
 * FirebaseCoupon, Firebase Firestore'dan alınan kupon verilerini temsil eden veri sınıfıdır.
 * Her bir kupon için gerekli olan temel bilgileri içerir.
 *
 * @property coupon_id Kuponun benzersiz ID'si. Varsayılan değer 0.
 * @property coupon_name Kuponun adı. Varsayılan değer boş bir string.
 * @property coupon_image Kupon ile ilişkili görselin URL'si veya dosya adı. Varsayılan değer boş bir string.
 * @property coupon_description Kuponun açıklaması. Varsayılan değer boş bir string.
 * @property coupon_discount Kuponun sunduğu indirim oranı. Varsayılan değer 0.0.
 * @property coupon_code Kuponun kullanılmak üzere girilmesi gereken kod. Varsayılan değer boş bir string.
 */
data class FirebaseCoupon(
    val coupon_id: Int = 0,                // Kuponun benzersiz ID'si
    val coupon_name: String = "",          // Kuponun adı
    val coupon_image: String = "",         // Kupona ait görsel URL'si veya dosya adı
    val coupon_description: String = "",   // Kuponun açıklaması
    val coupon_discount: Double = 0.0,     // Kuponun sunduğu indirim oranı
    val coupon_code: String = ""           // Kuponun kullanılması gereken kod
)
