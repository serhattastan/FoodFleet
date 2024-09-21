package com.cloffygames.foodfleet.uix.view

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Sort
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.R
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.uix.uicomponent.ShimmerEffect
import com.cloffygames.foodfleet.uix.viewmodel.HomeViewModel
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    // ViewModel'den verileri gözlemleyin
    val firebaseFoodList by viewModel.firebaseFoodList.observeAsState(emptyList())
    val firebaseCategoryList by viewModel.firebaseCategoryList.observeAsState(emptyList())
    val foodList by viewModel.foodList.observeAsState(emptyList())
    val firebaseCouponList by viewModel.firebaseCouponList.observeAsState(emptyList())

    Scaffold(
        topBar = { HomeTopAppBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // SearchBar görünümü
            item {
                SearchBar(navController = navController)
            }

            // Kategoriler
            item {
                CategoryList(firebaseCategoryList)
            }

            // Sırala ve Filtrele butonları
            item {
                SortAndFilterButtons()
            }

            // Yana kaydırılabilir grid
            item {
                FoodGrid(foodList = foodList)
            }

            // Kategorilere göre yemekleri listele
            items(firebaseCategoryList) { category ->
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
                val filteredFoodList = firebaseFoodList.filter { it.yemek_kategori == category }
                CategoryFoodList(filteredFoodList)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar() {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = "Kullanıcı Adresi",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Food Fleet",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "App Icon"
            )
        },
        actions = {
            IconButton(onClick = { /* Canlı destek simgesi işlevi */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Canlı Destek"
                )
            }
        }
    )
}

@Composable
fun SearchBar(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate("search_screen")
            },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = "Yemek ara...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun CategoryList(categories: List<String>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryCard(category = category)
        }
    }
}

@Composable
fun CategoryCard(category: String) {
    Card(
        modifier = Modifier
            .size(125.dp)
            .clickable { /* Kategoriye tıklama işlevi */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GlideImage(
                imageModel = "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png",
                contentDescription = "Food Image",
                modifier = Modifier.size(150.dp, 100.dp),
                loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
            )
            Text(
                text = category,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(4.dp)
            )
        }
    }
}

@Composable
fun SortAndFilterButtons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = { /* Sırala butonuna tıklama işlevi */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                imageVector = Icons.Default.Sort,
                contentDescription = "Sırala Icon",
                modifier = Modifier.padding(end = 4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sırala")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { /* Filtrele butonuna tıklama işlevi */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            )
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filtrele Icon",
                modifier = Modifier.padding(end = 4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Filtrele")
        }
    }
}

@Composable
fun FoodGrid(foodList: List<Food>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(foodList.chunked(2)) { rowItems ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowItems.forEach { food ->
                    FoodCard(food = food)
                }
            }
        }
    }
}

@Composable
fun FoodCard(food: Food) {
    Card(
        modifier = Modifier
            .size(160.dp, 155.dp) // Kart boyutu küçültüldü
            .clickable { /* Yemeğe tıklama işlevi */ }.padding(top = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Yemeğin görseli
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp) // Görsel yüksekliği ayarlandı
            ) {
                GlideImage(
                    imageModel = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}",
                    contentDescription = food.yemek_adi,
                    modifier = Modifier.fillMaxSize(),
                    loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
                )
            }

            // Yemek ismi, fiyatı ve sepete ekleme butonu bir Row içinde
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp), // Yatay padding azaltıldı
                horizontalArrangement = Arrangement.SpaceBetween, // Elemanları yatayda ayır
                verticalAlignment = Alignment.CenterVertically // Dikeyde hizalama
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = food.yemek_adi,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "${food.yemek_fiyat} TL",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                // Sepete ekleme butonu
                IconButton(
                    onClick = { /* Sepete ekle işlemi */ },
                    modifier = Modifier.size(24.dp) // Buton boyutu küçültüldü
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to Cart",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun FoodCardFirebase(food: FirebaseFood) {
    Card(
        modifier = Modifier
            .size(160.dp, 155.dp) // Kart boyutu küçültüldü
            .clickable { /* Yemeğe tıklama işlevi */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Yemeğin görseli
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp) // Görsel yüksekliği ayarlandı
            ) {
                GlideImage(
                    imageModel = food.yemek_resim_adi,
                    contentDescription = food.yemek_adi,
                    modifier = Modifier.fillMaxSize(),
                    loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
                )
            }

            // Yemek ismi, fiyatı ve sepete ekleme butonu bir Row içinde
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp), // Yatay padding azaltıldı
                horizontalArrangement = Arrangement.SpaceBetween, // Elemanları yatayda ayır
                verticalAlignment = Alignment.CenterVertically // Dikeyde hizalama
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = food.yemek_adi,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "${food.yemek_fiyat} TL",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }

                // Sepete ekleme butonu
                IconButton(
                    onClick = { /* Sepete ekle işlemi */ },
                    modifier = Modifier.size(24.dp) // Buton boyutu küçültüldü
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to Cart",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}




@Composable
fun CategoryFoodList(filteredFoodList: List<FirebaseFood>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp), // Kartlar arasındaki boşluklar azaltıldı
        horizontalArrangement = Arrangement.spacedBy(8.dp) // Boşluk miktarı azaltıldı
    ) {
        items(filteredFoodList) { food ->
            FoodCardFirebase(food = food)
        }
    }
}
