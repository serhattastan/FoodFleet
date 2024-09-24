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
    var selectedSortOption by remember { mutableStateOf("Tarih (Yeni - Eski)") }

    LaunchedEffect(Unit) {
        viewModel.getOrderHistory(
            onSuccess = { orders ->
                orderHistoryList = sortOrders(orders, selectedSortOption)
            },
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
                SortOptionDropdown(selectedSortOption) { selectedOption ->
                    selectedSortOption = selectedOption
                    // Sipariş listesini seçilen sıralama yöntemine göre güncelle
                    orderHistoryList = sortOrders(orderHistoryList, selectedSortOption)
                }

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

// Dropdown ile sıralama seçeneği sunuyoruz
@Composable
fun SortOptionDropdown(selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Tarih (Yeni - Eski)", "Tarih (Eski - Yeni)", "Sepet Tutarı (Yüksek - Düşük)", "Sepet Tutarı (Düşük - Yüksek)")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text(text = selectedOption, color = Color.White)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

// Siparişleri sıralamak için bir fonksiyon
fun sortOrders(orders: List<OrderHistory>, selectedSortOption: String): List<OrderHistory> {
    return when (selectedSortOption) {
        "Tarih (Yeni - Eski)" -> orders.sortedByDescending { it.siparis_tarihi }
        "Tarih (Eski - Yeni)" -> orders.sortedBy { it.siparis_tarihi }
        "Sepet Tutarı (Yüksek - Düşük)" -> orders.sortedByDescending { it.toplam_fiyat }
        "Sepet Tutarı (Düşük - Yüksek)" -> orders.sortedBy { it.toplam_fiyat }
        else -> orders
    }
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
