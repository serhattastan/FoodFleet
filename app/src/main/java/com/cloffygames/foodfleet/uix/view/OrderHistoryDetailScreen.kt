package com.cloffygames.foodfleet.uix.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.data.entity.Cart
import com.cloffygames.foodfleet.data.entity.OrderHistory
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.uix.uicomponent.ShimmerEffect
import com.cloffygames.foodfleet.uix.viewmodel.OrderHistoryDetailViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryDetailScreen(
    navController: NavController,
    orderId: String,
    viewModel: OrderHistoryDetailViewModel
) {
    var orderHistory by remember { mutableStateOf<OrderHistory?>(null) }
    val coroutineScope = rememberCoroutineScope()

    // Sipariş geçmişi detaylarını çekiyoruz
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            viewModel.getOrderHistoryDetail(
                orderId = orderId,
                onSuccess = { order -> orderHistory = order },
                onFailure = { /* Hata işleme */ }
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sipariş Detayları", color = Color.White) },
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
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                orderHistory?.let { order ->
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth().height(500.dp),
                    ) {
                        // Sepet içeriklerini listeliyoruz
                        items(order.sepet_listesi) { cartItem ->
                            CartItemDetailCard(cartItem = cartItem)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Toplam Fiyat
                    Text(
                        text = "Toplam Fiyat: ₺${order.toplam_fiyat}",
                        color = PrimaryTextColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )

                    // Sipariş Tarihi
                    Text(
                        text = "Sipariş Tarihi: ${order.siparis_tarihi.toDate()}",
                        color = PrimaryTextColor,
                        modifier = Modifier.padding(8.dp)
                    )

                    // Kupon Kodu
                    Text(
                        text = "Kupon Kodu: ${order.kupon_kodu}",
                        color = PrimaryTextColor,
                        modifier = Modifier.padding(8.dp)
                    )
                } ?: run {
                    Text(
                        text = "Sipariş detayları yükleniyor...",
                        color = PrimaryTextColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    )
}

// Sepet içerisindeki her bir yemek için kart bileşeni
@Composable
fun CartItemDetailCard(cartItem: Cart) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            GlideImage(
                imageModel = cartItem.yemek_resim_adi,
                contentDescription = cartItem.yemek_adi,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit,
                loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
            )
            Text(
                text = "Yemek Adı: ${cartItem.yemek_adi}",
                color = PrimaryTextColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Fiyat: ₺${cartItem.yemek_fiyat}",
                color = PrimaryTextColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Adet: ${cartItem.yemek_siparis_adet}",
                color = PrimaryTextColor
            )
        }
    }
}