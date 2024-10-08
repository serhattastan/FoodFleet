package com.cloffygames.foodfleet.uix.view

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.data.entity.FavoriteFood
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.ui.theme.AddToCartButtonColor
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.ui.theme.SecondaryColor
import com.cloffygames.foodfleet.ui.theme.SecondaryTextColor
import com.cloffygames.foodfleet.uix.uicomponent.ShimmerEffect
import com.cloffygames.foodfleet.uix.viewmodel.FoodDetailViewModel
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

/**
 * FoodDetailScreen bileşeni, yiyecek detaylarını görüntüler. Kullanıcı yiyecek hakkında bilgi
 * alabilir, miktarını değiştirebilir ve sepete ekleyebilir.
 *
 * @param food Gösterilecek yiyecek bilgisi
 * @param navController Navigasyon kontrolcüsü
 * @param viewModel Yiyecek detaylarını yöneten ViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(food: Food, navController: NavController, viewModel: FoodDetailViewModel) {
    var quantity by remember { mutableIntStateOf(1) } // Miktar başlangıç değeri 1
    // Filtrelenen önerilen ürünler
    val recommendedFoods = viewModel.firebaseFoodList.observeAsState(emptyList()).value
        .filter { it.yemek_kategori in listOf("Meze", "İçecek", "Tatlı") }.shuffled()

    var userAddress by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        viewModel.getUser(
            onSuccess = { user ->
                userAddress = user.user_address
                userName = user.user_id
            },
            onFailure = { Log.e("FoodDetailScreen", "Kullanıcı bilgileri alınamadı") }
        )
    }

    // Scaffold bileşeni, ekranın üst barı ve ana içeriğini düzenler
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = food.yemek_adi, color = PrimaryTextColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryTextColor)
                    }
                },
                actions = {
                    var isFavorite by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        viewModel.checkIfFavorite(food.yemek_adi,
                            onResult = { result ->
                                isFavorite = result  // Favorilerde olup olmadığını ayarlıyoruz
                            }
                        )
                    }
                    // Favorilere ekle veya çıkar butonu
                    IconButton(
                        onClick = {
                            if (isFavorite) {
                                // Favorilerden çıkar
                                viewModel.removeFavoriteFood(food.yemek_id.toString(),
                                    onSuccess = {
                                        isFavorite = false  // Başarılı olunca favorilerden çıkarılmış olduğunu göster
                                    },
                                    onFailure = { e ->
                                        // Hata durumunda işleme kodu
                                    }
                                )
                            } else {
                                // Favorilere ekle
                                val favoriteFood = FavoriteFood(
                                    yemek_id = food.yemek_id,
                                    yemek_adi = food.yemek_adi,
                                    yemek_resim_adi = food.yemek_resim_adi,
                                    yemek_fiyat = food.yemek_fiyat.toInt()
                                )

                                viewModel.addFavoriteFood(favoriteFood,
                                    onSuccess = {
                                        isFavorite = true  // Başarılı olunca favoriye eklenmiş olduğunu göster
                                    },
                                    onFailure = { e ->
                                        // Hata durumunda işleme kodu
                                    }
                                )
                            }
                        },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, // Favori durumuna göre ikon değiştiriliyor
                            contentDescription = if (isFavorite) "Remove from Favorite" else "Add to Favorite",
                            tint = if (isFavorite) Color.Red else AddToCartButtonColor
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(BackgroundColor),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Yemek Resmi
            var imageUrl by remember { mutableStateOf("http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}") }

            GlideImage(
                imageModel = imageUrl,
                contentDescription = food.yemek_adi,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(332.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(16.dp)) // Köşeleri yuvarlatma işlemi
                    .shadow(8.dp, shape = RoundedCornerShape(16.dp)) // Gölge eklenmesi
                    .background(PrimaryColor, shape = RoundedCornerShape(16.dp)), // Arka plan rengi
                contentScale = ContentScale.Crop,
                loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) },
                failure = {
                    // Eğer resim yüklenemezse, imageUrl'yi güncelle
                    imageUrl = food.yemek_resim_adi
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Yemek Fiyatı
            Text(
                text = "₺${food.yemek_fiyat}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Yemek Adı
            Text(
                text = food.yemek_adi,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryTextColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Miktar Ayarlama Kartı
            Card(
                modifier = Modifier
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = BackgroundColor)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentWidth() // Genişlik sadece gereken kadar olacak
                        .height(20.dp) // Buton yüksekliği
                ) {
                    // Miktarı azalt butonu
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Azalt", tint = PrimaryTextColor)
                    }

                    // Miktar gösterimi
                    Text(
                        text = quantity.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = PrimaryTextColor
                    )

                    // Miktarı artır butonu
                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Arttır", tint = SecondaryColor)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sepete Ekleme Bölümü
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₺${food.yemek_fiyat * quantity}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryTextColor
                )
                Button(
                    onClick = { viewModel.addFoodToCart(food.yemek_adi, imageUrl, food.yemek_fiyat * quantity, quantity, userName) },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Sepete Ekle", color = Color.White)
                }
            }

            // Önerilen Ürünler Başlığı
            Text(
                text = "Yanında İyi Gider ;)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryTextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Önerilen Ürünler Listesi
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            ) {
                items(recommendedFoods) { recommendedFood ->
                    RecommendedFoodCard(recommendedFood, navController, viewModel, userName)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * RecommendedFoodCard bileşeni, önerilen yiyeceklerin kart biçiminde gösterimini sağlar.
 *
 * @param food Gösterilecek önerilen yiyecek bilgisi
 * @param navController Navigasyon kontrolcüsü
 * @param viewModel Yiyecek detaylarını yöneten ViewModel
 * @param userName Kullanıcı ismi
 */
@Composable
fun RecommendedFoodCard(food: FirebaseFood, navController: NavController, viewModel: FoodDetailViewModel, userName: String) {
    // Favoriye eklenip eklenmediğini kontrol eden bir state
    var isFavorite by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .size(200.dp, 166.dp)
            .padding(end = 8.dp)
            .clickable {
                val transitionFood = Food(food.yemek_id, food.yemek_adi, food.yemek_resim_adi, food.yemek_fiyat.toInt())
                val json = Uri.encode(Gson().toJson(transitionFood))
                navController.navigate("FoodDetailScreen/${json}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(116.dp)
            ) {
                GlideImage(
                    imageModel = food.yemek_resim_adi,
                    contentDescription = food.yemek_adi,
                    modifier = Modifier.fillMaxSize(),
                    loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = food.yemek_adi,
                        style = MaterialTheme.typography.labelLarge,
                        color = PrimaryTextColor
                    )
                    Text(
                        text = "${food.yemek_fiyat} TL",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryTextColor
                    )
                }

                // Favorilere ekle veya çıkar butonu
                IconButton(
                    onClick = {
                        if (isFavorite) {
                            // Favorilerden çıkar
                            viewModel.removeFavoriteFood(food.yemek_id.toString(),
                                onSuccess = {
                                    isFavorite = false  // Başarılı olunca favorilerden çıkarılmış olduğunu göster
                                },
                                onFailure = { e ->
                                    // Hata durumunda işleme kodu
                                }
                            )
                        } else {
                            // Favorilere ekle
                            val favoriteFood = FavoriteFood(
                                yemek_id = food.yemek_id,
                                yemek_adi = food.yemek_adi,
                                yemek_resim_adi = food.yemek_resim_adi,
                                yemek_fiyat = food.yemek_fiyat.toInt()
                            )

                            viewModel.addFavoriteFood(favoriteFood,
                                onSuccess = {
                                    isFavorite = true  // Başarılı olunca favoriye eklenmiş olduğunu göster
                                },
                                onFailure = { e ->
                                    // Hata durumunda işleme kodu
                                }
                            )
                        }
                    },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, // Favori durumuna göre ikon değiştiriliyor
                        contentDescription = if (isFavorite) "Remove from Favorite" else "Add to Favorite",
                        tint = if (isFavorite) Color.Red else AddToCartButtonColor
                    )
                }

                IconButton(
                    onClick = { viewModel.addFoodToCart(food.yemek_adi, food.yemek_resim_adi, food.yemek_fiyat.toInt(), 1, userName) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to Cart",
                        tint = AddToCartButtonColor
                    )
                }
            }
        }
    }
}