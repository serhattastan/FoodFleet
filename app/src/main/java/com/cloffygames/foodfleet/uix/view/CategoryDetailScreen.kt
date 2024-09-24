package com.cloffygames.foodfleet.uix.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.data.entity.FavoriteFood
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.ui.theme.AddToCartButtonColor
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.ui.theme.SecondaryTextColor
import com.cloffygames.foodfleet.uix.uicomponent.ShimmerEffect
import com.cloffygames.foodfleet.uix.viewmodel.CategoryDetailScreenViewModel
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

/**
 * CategoryDetailScreen bileşeni, belirli bir kategorideki yiyeceklerin listesini gösterir.
 * Kullanıcı bir yiyeceğe tıklayarak detaylarını görüntüleyebilir.
 *
 * @param category Kategori ismi
 * @param navController Ekranlar arası geçişi yönetmek için kullanılan NavController
 * @param viewModel Kategoriye ait yiyeceklerin verilerini yöneten ViewModel
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    category: String,
    navController: NavController,
    viewModel: CategoryDetailScreenViewModel
) {
    // Gözlemlenen veri listeleri
    val foodList = viewModel.firebaseFoodList.observeAsState(emptyList()).value
        .filter { it.yemek_kategori == category }

    // Kullanıcı bilgilerini almak için state tanımları
    var userName by remember { mutableStateOf("") }

    // Kullanıcı verilerini almak için LaunchedEffect kullanılıyor
    LaunchedEffect(Unit) {
        viewModel.getUser(
            onSuccess = { user ->
                userName = user.user_id
            },
            onFailure = { /* Hata işleme kodu */ }
        )
    }

    // Scaffold bileşeni, üst bar ve içerik alanını düzenler
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = category, color = PrimaryTextColor) }, // Üst bar başlığı kategori ismi
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryTextColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)
            )
        }
    ) { paddingValues ->
        if (foodList.isNotEmpty()) {
            // Yiyecekleri listeleyen bir LazyColumn
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(BackgroundColor),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(foodList) { food ->
                    // Her bir yiyecek için CategoryDetailCard bileşeni
                    CategoryDetailCard(food, navController, userName, viewModel)
                }
            }
        } else {
            // Eğer listede yiyecek yoksa gösterilecek boş durum mesajı
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)
            ) {
                Text(
                    text = "No items available in $category",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryTextColor
                )
            }
        }
    }
}

/**
 * CategoryDetailCard bileşeni, bir yiyecek kartını kategorilerde listelemeye uygun olarak gösterir.
 *
 * @param food Gösterilecek yiyecek bilgisi
 * @param navController Ekranlar arası geçişi yönetmek için kullanılan NavController
 * @param userName Kullanıcı ismi
 * @param viewModel Kategori ekranı işlemlerini yöneten ViewModel
 */
@Composable
fun CategoryDetailCard(
    food: FirebaseFood,
    navController: NavController,
    userName: String,
    viewModel: CategoryDetailScreenViewModel
) {
    // Favoriye eklenip eklenmediğini kontrol eden bir state
    var isFavorite by remember { mutableStateOf(false) }

    // Favorilerde olup olmadığını kontrol etmek için LaunchedEffect
    LaunchedEffect(Unit) {
        viewModel.checkIfFavorite(food.yemek_adi,
            onResult = { result ->
                isFavorite = result  // Favorilerde olup olmadığını ayarlıyoruz
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(225.dp)
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .shadow(elevation = 4.dp)
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
            // Yiyecek Resmi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                GlideImage(
                    imageModel = food.yemek_resim_adi,
                    contentDescription = food.yemek_adi,
                    modifier = Modifier.fillMaxSize(),
                    loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
                )
            }

            // Yiyecek Adı ve Fiyatı
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

                // Sepete ekle butonu
                IconButton(
                    onClick = {
                        viewModel.addFoodToCart(food.yemek_adi, food.yemek_resim_adi, food.yemek_fiyat.toInt(), 1, userName)
                    },
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
