package com.cloffygames.foodfleet.data.entity

import com.google.firebase.Timestamp

data class OrderHistory(
    val order_id: String = "",  // Sipariş için benzersiz bir ID
    val sepet_listesi: List<Cart>,
    val toplam_fiyat: Int,
    val siparis_tarihi: Timestamp = Timestamp.now(),  // Timestamp ile tarih ve saat tutma
    val kupon_kodu: String
)