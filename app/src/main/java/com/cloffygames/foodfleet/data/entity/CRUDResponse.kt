package com.cloffygames.foodfleet.data.entity

/**
 * CRUDResponse sınıfı, CRUD (Create, Read, Update, Delete) işlemlerine verilen sunucu yanıtını temsil eder.
 * Bu sınıf, sunucu tarafından gönderilen başarı durumunu ve işlemin sonucuyla ilgili mesajı içerir.
 *
 * @property success Sunucu cevabının başarılı olup olmadığını belirten bayrak (1 = Başarılı, 0 = Başarısız).
 * @property message Sunucudan dönen işlemin sonucuyla ilgili mesaj.
 */
data class CRUDResponse(
    val success: Int,    // İşlemin başarılı olup olmadığını gösteren bayrak (1 = başarılı, 0 = başarısız)
    val message: String  // İşlem sonucuyla ilgili sunucudan gelen mesaj
)
