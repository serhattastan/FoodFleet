package com.cloffygames.foodfleet.uix.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.data.entity.Cart
import com.cloffygames.foodfleet.ui.theme.AddToCartButtonColor
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.uix.uicomponent.ShimmerEffect
import com.cloffygames.foodfleet.uix.viewmodel.CartViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel, userName: String) {
    // Observe the cartFoodList and sort it alphabetically by 'yemek_adi'
    val cartFoodList by viewModel.cartFoods.observeAsState(emptyList())
    val sortedCartFoodList = cartFoodList.sortedBy { it.yemek_adi }

    LaunchedEffect(key1 = true) {
        viewModel.getCartFoods(userName)
    }

    Scaffold(
        topBar = {
            CartTopAppBar(navController, "Sepetim")
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .background(BackgroundColor)
                    .padding(16.dp)
            ) {
                if (sortedCartFoodList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(sortedCartFoodList) { cartItem ->
                            CartItemCard(cartItem = cartItem, viewModel = viewModel, userName = userName)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Total Price
                    TotalPriceSection(cartFoodList = sortedCartFoodList)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Confirm Cart Button
                    ConfirmCartButton {
                        // Confirm Cart Action
                    }
                } else {
                    EmptyCartMessage()
                }
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTopAppBar(navController: NavController, title: String) {
    TopAppBar(
        title = {
            Text(text = title, color = PrimaryTextColor)
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = PrimaryTextColor)
            }
        }
    )
}

@Composable
fun CartItemCard(cartItem: Cart,viewModel: CartViewModel,userName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            GlideImage(
                imageModel = cartItem.yemek_resim_adi,
                contentDescription = cartItem.yemek_adi,
                modifier = Modifier.size(80.dp),
                loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = cartItem.yemek_adi, style = MaterialTheme.typography.bodyLarge, color = PrimaryTextColor)

                // Toplam fiyat: birim fiyat * sipariş adeti
                Text(text = "Fiyat: ₺${cartItem.yemek_fiyat}", style = MaterialTheme.typography.bodyMedium)
            }
            Row {
                Text(text = "Adet: ${cartItem.yemek_siparis_adet}", style = MaterialTheme.typography.bodyMedium)

                // Miktar artırma ve azaltma butonları
                IconButton(onClick = {
                    viewModel.decreaseFoodQuantity(cartItem)

                }, enabled = cartItem.yemek_siparis_adet > 1) {
                    Icon(imageVector = Icons.Default.Remove, contentDescription = "Azalt")
                }

                IconButton(onClick = {
                    viewModel.increaseFoodQuantity(cartItem)

                }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Arttır")
                }

            }
            IconButton(onClick = {
                viewModel.deleteFoodFromCart(cartItem.sepet_yemek_id, userName)

            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Remove", tint = AddToCartButtonColor)
            }
        }
    }
}


@Composable
fun TotalPriceSection(cartFoodList: List<Cart>) {
    val totalPrice = cartFoodList.sumOf { it.yemek_fiyat }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Toplam:", style = MaterialTheme.typography.bodyLarge, color = PrimaryTextColor)
        Text(text = "₺$totalPrice", style = MaterialTheme.typography.bodyLarge, color = PrimaryTextColor)
    }
}

@Composable
fun ConfirmCartButton(onConfirm: () -> Unit) {
    Button(
        onClick = onConfirm,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = AddToCartButtonColor
        )
    ) {
        Text(text = "Sepeti Onayla", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun EmptyCartMessage() {
    Text(
        text = "Sepetiniz boş",
        modifier = Modifier.fillMaxSize(),
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = PrimaryTextColor
    )
}
