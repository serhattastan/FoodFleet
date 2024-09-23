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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.data.entity.Cart
import com.cloffygames.foodfleet.data.entity.FirebaseCoupon
import com.cloffygames.foodfleet.ui.theme.AddToCartButtonColor
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.ui.theme.SecondaryColor
import com.cloffygames.foodfleet.ui.theme.SecondaryTextColor
import com.cloffygames.foodfleet.uix.uicomponent.ShimmerEffect
import com.cloffygames.foodfleet.uix.viewmodel.CartViewModel
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel, userName: String) {
    val cartFoodList by viewModel.cartFoods.observeAsState(emptyList())
    val firebaseCouponList by viewModel.firebaseCouponList.observeAsState(emptyList())
    val sortedCartFoodList = cartFoodList.sortedBy { it.yemek_adi }

    // State to hold the entered coupon code
    var couponCode by remember { mutableStateOf("") }
    // State to hold the applied coupon
    var appliedCoupon by remember { mutableStateOf<FirebaseCoupon?>(null) }

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

                    // Coupon Code Input
                    CouponCodeInput(
                        couponCode = couponCode,
                        onCouponCodeChange = { couponCode = it },
                        onApplyCoupon = {
                            val coupon = firebaseCouponList.find { it.coupon_code == couponCode }
                            if (coupon != null) {
                                appliedCoupon = coupon
                            } else {
                                appliedCoupon = null // No valid coupon found
                            }
                        },
                        appliedCoupon = appliedCoupon
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Total Price
                    TotalPriceSection(cartFoodList = sortedCartFoodList, appliedCoupon = appliedCoupon)

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
fun CouponCodeInput(
    couponCode: String,
    onCouponCodeChange: (String) -> Unit,
    onApplyCoupon: () -> Unit,
    appliedCoupon: FirebaseCoupon?
) {
    Column {
        TextField(
            value = couponCode,
            onValueChange = onCouponCodeChange,
            label = { Text("Kupon Kodu") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onApplyCoupon,
            modifier = Modifier.fillMaxWidth(),
            enabled = appliedCoupon == null,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor
            )
        ) {
            Text(text = "Kuponu Uygula")
        }

        if (appliedCoupon != null) {
            Text(
                text = "Uygulanan Kupon: ${appliedCoupon.coupon_name}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
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
fun CartItemCard(cartItem: Cart, viewModel: CartViewModel, userName: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 10.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Yemeğin resmi
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

            Spacer(modifier = Modifier.height(8.dp))

            // Yemeğin bilgileri
            Text(
                text = cartItem.yemek_adi,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = PrimaryTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "₺${cartItem.yemek_fiyat}",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = SecondaryTextColor
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Adet: ${cartItem.yemek_siparis_adet}",
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryTextColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Artırma/azaltma ve silme butonları
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Artırma ve azaltma butonları
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.decreaseFoodQuantity(cartItem) },
                        enabled = cartItem.yemek_siparis_adet > 1,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Azalt",
                            tint = if (cartItem.yemek_siparis_adet > 1) PrimaryColor else SecondaryColor
                        )
                    }

                    Text(
                        text = "${cartItem.yemek_siparis_adet}",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )

                    IconButton(
                        onClick = { viewModel.increaseFoodQuantity(cartItem) },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Arttır",
                            tint = PrimaryColor
                        )
                    }
                }

                // Silme butonu
                IconButton(
                    onClick = { viewModel.deleteFoodFromCart(cartItem.sepet_yemek_id, userName) },
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
}





@Composable
fun TotalPriceSection(cartFoodList: List<Cart>, appliedCoupon: FirebaseCoupon?) {
    val totalPrice = cartFoodList.sumOf { it.yemek_fiyat }
    val discount = appliedCoupon?.coupon_discount ?: 0.0
    var finalPrice = totalPrice - discount
    if(finalPrice < 0) {
        finalPrice = 0.0
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Toplam:", style = MaterialTheme.typography.bodyLarge, color = PrimaryTextColor)
        Text(text = "₺${String.format("%.2f", finalPrice)}", style = MaterialTheme.typography.bodyLarge, color = PrimaryTextColor)
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