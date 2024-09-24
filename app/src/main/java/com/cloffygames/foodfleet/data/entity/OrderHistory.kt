package com.cloffygames.foodfleet.data.entity

import com.google.firebase.Timestamp

data class OrderHistory(
    val order_id: String = "",  // Sipariş için benzersiz bir ID
    val sepet_listesi: List<Cart> = emptyList(),
    val toplam_fiyat: Int = 0,
    val siparis_tarihi: Timestamp = Timestamp.now(),
    val kupon_kodu: String = "kullanilmadi"
) {
    // Varsayılan constructor Firebase tarafından deserialization için kullanılacak
    constructor() : this("", emptyList(), 0, Timestamp.now(), "kullanilmadi")
}
