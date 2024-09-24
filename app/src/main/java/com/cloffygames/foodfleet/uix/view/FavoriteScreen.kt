package com.cloffygames.foodfleet.uix.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.data.entity.FavoriteFood
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.ui.theme.AddToCartButtonColor
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.uix.viewmodel.FavoriteViewModel
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: FavoriteViewModel
) {
    // Favori yiyecekleri saklamak için bir state
    var favoriteFoodList by remember { mutableStateOf(emptyList<FavoriteFood>()) }

    // Favori yemekleri getiriyoruz
    LaunchedEffect(Unit) {
        viewModel.getFavoriteFoods(
            onSuccess = { foods -> favoriteFoodList = foods },
            onFailure = { e -> /* Hata durumunu yönet */ }
        )
    }

    // Scaffold ile ekranın genel düzeni
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorilerim", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = PrimaryColor)
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(BackgroundColor)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (favoriteFoodList.isNotEmpty()) {
                    // Favori yemeklerin listesi
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(favoriteFoodList) { food ->
                            FavoriteFoodCard(food = food, onClick = {
                                // Yemek detaylarına gitmek için navController kullanılıyor
                                val transitionFood = Food(food.yemek_id, food.yemek_adi, food.yemek_resim_adi, food.yemek_fiyat.toInt())
                                val json = Uri.encode(Gson().toJson(transitionFood))
                                navController.navigate("FoodDetailScreen/${json}")
                            }, viewModel = viewModel, onRemoveSuccess = {
                                // Favori yemek silindikten sonra listeyi güncelle
                                viewModel.getFavoriteFoods(
                                    onSuccess = { updatedFoods -> favoriteFoodList = updatedFoods },
                                    onFailure = { e -> /* Hata durumunu yönet */ }
                                )
                            })
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                } else {
                    // Eğer favori yemek yoksa gösterilecek mesaj
                    Text(
                        text = "Henüz favorilere eklediğiniz bir yemek yok.",
                        color = PrimaryTextColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )
}

// Favori yemek kartı tasarımı
@Composable
fun FavoriteFoodCard(food: FavoriteFood, onClick: () -> Unit, viewModel: FavoriteViewModel, onRemoveSuccess: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Yemek resmi
            GlideImage(
                imageModel = food.yemek_resim_adi,
                contentDescription = food.yemek_adi,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Yemek bilgileri
            Column {
                Text(
                    text = food.yemek_adi,
                    color = PrimaryTextColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Fiyat: ₺${food.yemek_fiyat}",
                    color = PrimaryTextColor,
                    fontWeight = FontWeight.Normal
                )
            }
            // Silme butonu
            IconButton(
                onClick = {
                    viewModel.removeFavoriteFood(food.yemek_id.toString(),
                        onSuccess = {
                            onRemoveSuccess()  // Silme işlemi başarılı olunca listeyi güncelle
                        },
                        onFailure = { e ->
                            // Hata durumunu yönet
                        }
                    )
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Remove",
                    tint = AddToCartButtonColor
                )
            }
        }
    }
}
