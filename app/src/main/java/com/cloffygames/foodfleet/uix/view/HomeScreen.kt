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
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Kullanıcı Adresi", // Statik olarak atanacak adres
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Food Fleet", // Uygulama adı
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground), // Uygulama simgesi
                        contentDescription = "App Icon"
                    )
                },
                actions = {
                    IconButton(onClick = {
                        // Canlı destek simgesi tıklama işlevi
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground), // Canlı destek simgesi
                            contentDescription = "Canlı Destek"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // SearchBar görünümü
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            // SearchBar'a tıklanınca başka sayfaya geçiş yapma işlemi
                            navController.navigate("search_screen")
                        },
                    shape = RoundedCornerShape(24.dp), // Daha yuvarlak köşeler
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp) // Daha belirgin bir elevation
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
                            tint = MaterialTheme.colorScheme.primary, // İkonun rengini daha belirgin yapmak için
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

            // Kategorilerin bulunduğu LazyRow
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(firebaseCategoryList) { category ->
                        // Her bir kategori için Card kullanarak gösterim
                        Card(
                            modifier = Modifier
                                .size(125.dp) // Kart boyutu
                                .clickable {
                                    // Kategoriye tıklama işlevi
                                },
                            shape = RoundedCornerShape(16.dp), // Köşeleri yuvarlaklaştır
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            // Kategori resmi ve adı
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                GlideImage(
                                    imageModel = "http://kasimadalan.pe.hu/yemekler/resimler/ayran.png",
                                    contentDescription = "Food Image",
                                    modifier = Modifier
                                        .size(150.dp, 100.dp)
                                        .background(Color.LightGray.copy(alpha = 0.3f)),
                                    loading = {
                                        ShimmerEffect(modifier = Modifier.fillMaxSize())
                                    }
                                )
                                // Glide kullanarak resim yükleme burada olacak
                                Text(
                                    text = category, // Kategori ismi
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                            }
                        }
                    }
                }
            }

            // Sırala ve Filtrele butonları
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween // Butonları yan yana koy
                ) {
                    // Sırala Butonu
                    Button(
                        onClick = {
                            // Sırala butonuna tıklama işlevi
                        },
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

                    Spacer(modifier = Modifier.width(8.dp)) // Butonlar arasında boşluk

                    // Filtrele Butonu
                    Button(
                        onClick = {
                            // Filtrele butonuna tıklama işlevi
                        },
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

            // Yana kaydırılabilir 3 satırlı grid
            item {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(foodList.chunked(2)) { rowItems -> // Her 2 öğeyi bir satır olarak chunk ediyoruz
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { food ->
                                Card(
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clickable {
                                            // Yemeğe tıklama işlevi
                                        },
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        // Yemeğin resmi
                                        GlideImage(
                                            imageModel = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}",
                                            contentDescription = food.yemek_adi,
                                            modifier = Modifier
                                                .fillMaxSize(),
                                            loading = {
                                                ShimmerEffect(modifier = Modifier.fillMaxSize()) // Shimmer effect uygulanıyor
                                            }
                                        )

                                        // Yemeğin adı
                                        Text(
                                            text = food.yemek_adi,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White,
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .padding(8.dp)
                                        )

                                        // Yemeğin fiyatı
                                        Text(
                                            text = "${food.yemek_fiyat} TL",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White,
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Kategorilere göre yemekleri listele
            items(firebaseCategoryList) { category ->
                // Kategori başlığı
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )

                // Bu kategoriye ait yemekleri filtrele
                val filteredFoodList = firebaseFoodList.filter { it.yemek_kategori == category }

                // Kategoriye ait yemekler
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(filteredFoodList) { food ->
                        Card(
                            modifier = Modifier
                                .size(120.dp)
                                .clickable {
                                    // Yemeğe tıklama işlevi
                                },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize()
                            ) {
                                // Yemeğin resmi
                                GlideImage(
                                    imageModel = food.yemek_resim_adi,
                                    contentDescription = food.yemek_adi,
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    loading = {
                                        ShimmerEffect(modifier = Modifier.fillMaxSize()) // Shimmer effect uygulanıyor
                                    }
                                )

                                // Yemeğin adı
                                Text(
                                    text = food.yemek_adi,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .padding(8.dp)
                                )

                                // Yemeğin fiyatı
                                Text(
                                    text = "${food.yemek_fiyat} TL",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
