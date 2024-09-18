package com.cloffygames.foodfleet.uix.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.uix.viewmodel.HomeViewModel
import com.skydoves.landscapist.glide.GlideImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val firebaseFoodList by viewModel.firebaseFoodList.observeAsState(emptyList())
    val firebaseCategoryList by viewModel.firebaseCategoryList.observeAsState(emptyList())
    val foodList by viewModel.foodList.observeAsState(emptyList())
    val firebaseCouponList by viewModel.firebaseCouponList.observeAsState(emptyList())

    // Pager state
    val pagerState = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f, pageCount = { foodList.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Carousel pager for top section with soft design
        HorizontalPager(
            state = pagerState,  // Pager state
            verticalAlignment = Alignment.CenterVertically,
            reverseLayout = false,
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) { page ->
            val foodItem = foodList[page]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.1f))
                    .padding(16.dp)
            ) {
                Text(
                    text = foodItem.yemek_adi,
                    color = Color.Gray,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                GlideImage(
                    imageModel = "http://kasimadalan.pe.hu/yemekler/resimler/${foodItem.yemek_resim_adi}",
                    contentDescription = "Food Image",
                    modifier = Modifier
                        .size(250.dp, 166.dp)
                        .padding(vertical = 8.dp)
                        .background(Color.LightGray.copy(alpha = 0.3f))
                )
                Button(
                    onClick = { /*TODO: Add buy action*/ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.LightGray.copy(alpha = 0.8f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(text = "Buy Now", fontWeight = FontWeight.Medium)
                }
            }
        }

        // Spacer to separate pager and indicator
        Spacer(modifier = Modifier.height(16.dp))

        // Pager indicator
        PagerIndicator(
            totalPages = foodList.size,
            currentPage = pagerState.currentPage
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Category listing with food items under each category
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(firebaseCategoryList.size) { index ->
                val category = firebaseCategoryList[index]
                Text(
                    text = category,
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                LazyRow {
                    items(firebaseFoodList.size) { foodIndex ->
                        val firebaseFood = firebaseFoodList[foodIndex]
                        if (firebaseFood.yemek_kategori == category) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 8.dp)
                                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.05f))
                                    .padding(12.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = firebaseFood.yemek_adi,
                                    color = Color.DarkGray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Light,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                GlideImage(
                                    imageModel = firebaseFood.yemek_resim_adi,
                                    contentDescription = "Food Image",
                                    modifier = Modifier
                                        .size(150.dp, 100.dp)
                                        .background(Color.LightGray.copy(alpha = 0.3f))
                                )
                                Text(
                                    text = "${firebaseFood.yemek_fiyat} ₺",
                                    color = Color.Gray,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PagerIndicator(totalPages: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        for (i in 0 until totalPages) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (i == currentPage) Color(0xFFFFA726) else Color(0xFFE0E0E0))
            )
        }
    }
}
