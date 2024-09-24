package com.cloffygames.foodfleet.data.entity

data class FavoriteFood(
    val yemek_id: Int = 0,  // Varsayılan değerler eklendi
    val yemek_adi: String = "",
    val yemek_resim_adi: String = "",
    val yemek_fiyat: Int = 0
) {
    // Boş bir yapıcı oluşturulmuş durumda, Firebase deserializasyon için bu yapıcıyı kullanacaktır.
    constructor() : this(0, "", "", 0)
}
