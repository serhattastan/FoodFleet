package com.cloffygames.foodfleet.uix.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.data.entity.OrderHistory
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.uix.viewmodel.OrderHistoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    navController: NavController,
    viewModel: OrderHistoryViewModel
) {
    var orderHistoryList by remember { mutableStateOf(emptyList<OrderHistory>()) }

    LaunchedEffect(Unit) {
        viewModel.getOrderHistory(
            onSuccess = { orders -> orderHistoryList = orders },
            onFailure = { e -> /* Handle error */ }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Sipariş Geçmişi", color = Color.White) },
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
                if (orderHistoryList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(orderHistoryList) { order ->
                            OrderHistoryCard(order = order, onClick = {
                                // Sipariş detaylarına gitmek için navController kullanılıyor
                                navController.navigate("OrderDetailScreen/${order.order_id}")
                            })
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                } else {
                    Text(
                        text = "Henüz sipariş geçmişiniz yok.",
                        color = PrimaryTextColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    )
}

@Composable
fun OrderHistoryCard(order: OrderHistory, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }  // Kartın tıklanabilir olduğunu belirtiyor
            .padding(horizontal = 8.dp)
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Sipariş Tarihi: ${order.siparis_tarihi.toDate()}",
                color = PrimaryTextColor,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Toplam Fiyat: ₺${order.toplam_fiyat}",
                color = PrimaryTextColor,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kupon Kodu: ${order.kupon_kodu}",
                color = PrimaryTextColor,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Detaylara gitmek için bir buton
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(text = "Detayları Gör", color = Color.White)
            }
        }
    }
}
