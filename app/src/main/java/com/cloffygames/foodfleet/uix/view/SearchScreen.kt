package com.cloffygames.foodfleet.uix.view

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.ui.theme.AddToCartButtonColor
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.ui.theme.SecondaryTextColor
import com.cloffygames.foodfleet.uix.uicomponent.ShimmerEffect
import com.cloffygames.foodfleet.uix.viewmodel.SearchViewModel
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController, viewModel: SearchViewModel) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val combinedFoodList = remember { mutableStateListOf<Food>() }

    // Combine firebaseFoodList and foodList
    val firebaseFoods by viewModel.firebaseFoodList.observeAsState(emptyList())
    val foods by viewModel.foodList.observeAsState(emptyList())

    val firebaseCategoryList by viewModel.firebaseCategoryList.observeAsState(emptyMap())

    LaunchedEffect(firebaseFoods, foods) {
        combinedFoodList.clear()
        combinedFoodList.addAll(firebaseFoods.map { firebaseFood ->
            Food(
                yemek_id = firebaseFood.yemek_id,
                yemek_adi = firebaseFood.yemek_adi,
                yemek_resim_adi = firebaseFood.yemek_resim_adi,
                yemek_fiyat = firebaseFood.yemek_fiyat.toInt()
            )
        })
        combinedFoodList.addAll(foods)
        viewModel.getFoods()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor),
                title = {
                    Column {
                        Text(
                            text = "Arama",
                            style = MaterialTheme.typography.titleMedium,
                            color = PrimaryTextColor
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = PrimaryTextColor
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Profil simgesi işlevi */ }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Favoriler",
                            tint = AddToCartButtonColor
                        )
                    }
                    IconButton(onClick = { navController.navigate("CartScreen") }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Sepet",
                            tint = AddToCartButtonColor
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(BackgroundColor)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                // Search bar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                    colors = CardDefaults.cardColors(containerColor = BackgroundColor)
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
                            tint = PrimaryColor,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        TextField(
                            value = searchQuery,
                            onValueChange = { newValue -> searchQuery = newValue },
                            placeholder = { Text("Yemek, mutfak veya restoran arayın", color = SecondaryTextColor) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                containerColor = BackgroundColor,
                                focusedIndicatorColor = PrimaryColor,
                                unfocusedIndicatorColor = SecondaryTextColor
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Filtered food list
                val filteredFoodList = combinedFoodList.filter {
                    it.yemek_adi.contains(searchQuery.text, ignoreCase = true)
                }

                Text(
                    text = "Yemekler",
                    style = MaterialTheme.typography.titleMedium,
                    color = PrimaryTextColor,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxHeight(0.5f)
                ) {
                    items(filteredFoodList) { food ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clickable {
                                    val transitionFood = Food(food.yemek_id, food.yemek_adi, food.yemek_resim_adi, food.yemek_fiyat)
                                    val json = Uri.encode(Gson().toJson(transitionFood))
                                    navController.navigate("FoodDetailScreen/${json}")
                                },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            colors = CardDefaults.cardColors(containerColor = BackgroundColor)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                var imageUrl by remember { mutableStateOf("http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}") }

                                // searchQuery değiştiğinde imageUrl'yi sıfırlamak için LaunchedEffect kullanıyoruz
                                LaunchedEffect(searchQuery) {
                                    imageUrl = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}"
                                }
                                GlideImage(
                                    imageModel = imageUrl,
                                    contentDescription = food.yemek_adi,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(200.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    loading = {
                                        ShimmerEffect(modifier = Modifier.fillMaxSize())
                                    },
                                    failure = {
                                        // Eğer resim yüklenemezse, imageUrl'yi yemek_resim_adi olarak güncelle
                                        imageUrl = food.yemek_resim_adi
                                    }
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = food.yemek_adi,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = PrimaryTextColor
                                    )
                                    Text(
                                        text = "${food.yemek_fiyat} TL",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = SecondaryTextColor
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Category list based on search query
                val filteredCategoryList = firebaseFoods
                    .filter { it.yemek_kategori.contains(searchQuery.text, ignoreCase = true) }
                    .map { it.yemek_kategori }
                    .distinct()

                if (filteredCategoryList.isNotEmpty()) {
                    Text(
                        text = "Kategoriler",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryTextColor,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(filteredCategoryList) { category ->
                            Card(
                                modifier = Modifier
                                    .width(125.dp)
                                    .height(125.dp)
                                    .clickable {
                                        navController.navigate("CategoryDetailScreen/$category")
                                    },
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                colors = CardDefaults.cardColors(containerColor = BackgroundColor)
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    GlideImage(
                                        imageModel = firebaseCategoryList[category],
                                        contentDescription = category,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(100.dp),
                                        loading = {
                                            ShimmerEffect(modifier = Modifier.fillMaxSize())
                                        }
                                    )
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(4.dp),
                                        color = PrimaryTextColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

