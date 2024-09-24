package com.cloffygames.foodfleet.data.entity

/**
 * Cart sınıfı, kullanıcının sepetinde bulunan yemeklerin verilerini tutar.
 * Bu sınıf, sepetle ilgili bilgileri taşımak için kullanılır.
 *
 * @property sepet_yemek_id Sepetteki yemeğin benzersiz ID'si.
 * @property yemek_adi Sepetteki yemeğin adı.
 * @property yemek_resim_adi Yemeğin görsel dosya adı veya URL'si.
 * @property yemek_fiyat Sepetteki yemeğin birim fiyatı.
 * @property yemek_siparis_adet Sepette sipariş edilen yemeğin miktarı.
 * @property kullanici_adi Sepeti oluşturan kullanıcının adı.
 */
data class Cart(
    val sepet_yemek_id: Int = 0,          // Sepetteki yemeğin benzersiz ID'si
    val yemek_adi: String = "",            // Yemeğin adı
    val yemek_resim_adi: String = "",      // Yemeğin görsel dosya adı veya URL'si
    val yemek_fiyat: Int = 0,             // Yemeğin birim fiyatı
    val yemek_siparis_adet: Int = 0,      // Sipariş edilen yemeğin miktarı
    val kullanici_adi: String = ""         // Sepeti oluşturan kullanıcının adı
) {
    // Varsayılan yapıcı (constructor) Firebase deserialization için gereklidir
    constructor() : this(0, "", "", 0, 0, "")
}

