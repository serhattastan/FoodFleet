package com.cloffygames.foodfleet.data.entity

/**
 * CartResponse sınıfı, sunucudan gelen sepet verilerine karşılık olarak kullanılan veri modelidir.
 * Bu sınıf, sunucu cevabında başarı durumunu ve sepetteki yemeklerin listesini içerir.
 *
 * @property success Sunucu cevabının başarılı olup olmadığını belirten bayrak (1 = Başarılı, 0 = Başarısız).
 * @property sepet_yemekler Sepetteki yemekleri içeren liste.
 */
data class CartResponse(
    val success: Int,                // İşlemin başarılı olup olmadığını gösteren bayrak (1 = başarılı, 0 = başarısız)
    val sepet_yemekler: List<Cart>   // Sepetteki yemeklerin listesi
)
