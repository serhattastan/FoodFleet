package com.cloffygames.foodfleet.uix.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
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
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.ui.theme.AddToCartButtonColor
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.OnSecondaryTextColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.ui.theme.SecondaryColor
import com.cloffygames.foodfleet.ui.theme.SecondaryTextColor
import com.cloffygames.foodfleet.uix.uicomponent.ShimmerEffect
import com.cloffygames.foodfleet.uix.viewmodel.FoodDetailViewModel
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodDetailScreen(food: Food, navController: NavController, viewModel: FoodDetailViewModel) {
    var quantity by remember { mutableIntStateOf(1) } // Miktar başlangıç değeri 1

    // Filtrelenen önerilen ürünler
    val recommendedFoods = viewModel.firebaseFoodList.observeAsState(emptyList()).value
        .filter { it.yemek_kategori in listOf("Meze", "İçecek", "Tatlı") }.shuffled()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Ürün Detayı", color = PrimaryTextColor) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = PrimaryTextColor)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Favorilere ekle işlemi */ }) {
                        Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = PrimaryTextColor)
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
                    .shadow(8.dp, shape = RoundedCornerShape(16.dp)) // Elevation/gölge eklenmesi
                    .background(SecondaryColor, shape = RoundedCornerShape(16.dp)), // Arka plan da aynı şekli alacak
                contentScale = ContentScale.Crop,
                loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) },
                failure = {
                    // Eğer resim yüklenemezse, imageUrl'yi yemek_resim_adi olarak güncelle
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

            Card(
                modifier = Modifier
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp), // Köşeleri yuvarlatılmış bir kart
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp), // Kartın gölgesi
                colors = CardDefaults.cardColors(containerColor = BackgroundColor) // Arka plan rengi
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .wrapContentWidth() // Genişlik sadece gereken kadar olacak
                        .height(20.dp) // Butonun yüksekliği
                ) {
                    // Azalt Butonu
                    IconButton(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Azalt", tint = PrimaryTextColor)
                    }

                    // Dikey Çizgi (Butonlar Arası)
                    Divider(
                        color = SecondaryTextColor,
                        modifier = Modifier
                            .fillMaxHeight(0.6f) // Çizginin yüksekliği (0.6f ile %60 oranında)
                            .width(1.dp)
                    )

                    // Miktar
                    Text(
                        text = quantity.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = PrimaryTextColor
                    )

                    // Dikey Çizgi (Butonlar Arası)
                    Divider(
                        color = SecondaryTextColor,
                        modifier = Modifier
                            .fillMaxHeight(0.6f) // Çizginin yüksekliği (0.6f ile %60 oranında)
                            .width(1.dp)
                    )

                    // Arttır Butonu
                    IconButton(
                        onClick = { quantity++ },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Arttır", tint = SecondaryColor)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sepet Fiyatı ve Sepete Ekle Butonu
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
                    onClick = { /* Sepete ekle işlemi */ },
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
                    RecommendedFoodCard(recommendedFood, navController)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Yeni bir önerilen ürün kartı
@Composable
fun RecommendedFoodCard(food: FirebaseFood, navController: NavController) {
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

                IconButton(
                    onClick = { /* Sepete ekle işlemi */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to Cart",
                        tint = AddToCartButtonColor
                    )
                }

                IconButton(
                    onClick = { /* Sepete ekle işlemi */ },
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


