package com.cloffygames.foodfleet.uix.view

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.launch

/**
 * CartScreen bileşeni, kullanıcı sepetinde bulunan yiyeceklerin listesini gösterir.
 * Sepete eklenen yiyeceklerin miktarı ayarlanabilir ve kupon kodları uygulanabilir.
 *
 * @param navController Ekranlar arasında geçişi sağlayan NavController
 * @param viewModel Sepet işlemlerini yöneten ViewModel
 * @param userName Kullanıcı ismi
 */
@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel, userName: String) {
    val cartFoodList by viewModel.cartFoods.observeAsState(emptyList())
    val firebaseCouponList by viewModel.firebaseCouponList.observeAsState(emptyList())
    val sortedCartFoodList = cartFoodList.sortedBy { it.yemek_adi }

    var couponCode by remember { mutableStateOf("") } // Kupon kodu state
    var appliedCoupon by remember { mutableStateOf<FirebaseCoupon?>(null) } // Uygulanan kupon state

    // SnackbarHostState oluşturuyoruz
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.getCartFoods(userName) // userName kullanılıyor
    }

    // Scaffold ile sepet ekranı düzenleniyor
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },  // Scaffold'a snackbarHost ekledik
        modifier = Modifier.background(BackgroundColor),
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
                    // Sepetteki yiyeceklerin listesi
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(sortedCartFoodList) { cartItem ->
                            CartItemCard(cartItem = cartItem, viewModel = viewModel, userName = userName)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Kupon Kodu Girişi
                    CouponCodeInput(
                        couponCode = couponCode,
                        onCouponCodeChange = { couponCode = it },
                        onApplyCoupon = {
                            val coupon = firebaseCouponList.find { it.coupon_code == couponCode }
                            appliedCoupon = coupon
                        },
                        appliedCoupon = appliedCoupon
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Toplam Fiyat Bölümü
                    TotalPriceSection(cartFoodList = sortedCartFoodList, appliedCoupon = appliedCoupon)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sepeti Onayla Butonu
                    ConfirmCartButton(
                        cartFoodList = sortedCartFoodList,
                        appliedCoupon = appliedCoupon,
                        onConfirm = {
                            viewModel.addOrderHistory(
                                cartFoodList = sortedCartFoodList,
                                appliedCoupon = appliedCoupon,
                                onSuccess = {
                                    // Başarılı olunca Snackbar ile mesaj göster
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Sipariş başarıyla kaydedildi.")
                                    }
                                    navController.navigate("HomeScreen")
                                },
                                onFailure = { e ->
                                    // Hata durumunda Snackbar ile mesaj göster
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Hata: ${e.message}")
                                    }
                                }
                            )
                        },
                        onDismiss = {
                            // Eğer kullanıcı onay vermezse işlemi iptal et
                        },
                        viewModel = viewModel,
                        userName = userName
                    )
                } else {
                    EmptyCartMessage()
                }
            }
        }
    )
}



/**
 * Kupon kodu giriş alanı. Kullanıcı kupon kodunu girip uygulayabilir.
 *
 * @param couponCode Kupon kodu
 * @param onCouponCodeChange Kupon kodu değişikliklerini yöneten fonksiyon
 * @param onApplyCoupon Kupon uygulama işlemini başlatan fonksiyon
 * @param appliedCoupon Uygulanan kupon bilgisi
 */
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
                containerColor = BackgroundColor,

            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onApplyCoupon,
            modifier = Modifier.fillMaxWidth(),
            enabled = appliedCoupon == null,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryColor,
                contentColor = Color.White
            )
        ) {
            Text(text = "Kuponu Uygula")
        }

        if (appliedCoupon != null) {
            Text(
                text = "Uygulanan Kupon: ${appliedCoupon.coupon_name}",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextColor
            )
        }
    }
}

/**
 * CartTopAppBar, sepet ekranının üst barını oluşturur.
 *
 * @param navController Navigasyon kontrolcüsü
 * @param title Üst bar başlığı
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTopAppBar(navController: NavController, title: String) {
    TopAppBar(
        colors = (TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)),
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

/**
 * CartItemCard bileşeni, sepetteki her yiyeceği kart biçiminde gösterir.
 *
 * @param cartItem Sepetteki yiyecek bilgisi
 * @param viewModel Sepet işlemlerini yöneten ViewModel
 * @param userName Kullanıcı ismi
 */
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

/**
 * Toplam fiyat bölümü. Uygulanan kupon varsa indirim hesaplanır.
 *
 * @param cartFoodList Sepetteki yiyeceklerin listesi
 * @param appliedCoupon Uygulanan kupon (varsa)
 */
@Composable
fun TotalPriceSection(cartFoodList: List<Cart>, appliedCoupon: FirebaseCoupon?) {
    val totalPrice = cartFoodList.sumOf { it.yemek_fiyat }
    val discount = appliedCoupon?.coupon_discount ?: 0.0
    var finalPrice = totalPrice - discount
    if (finalPrice < 0) {
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

/**
 * Sepeti onaylama butonu.
 *
 * @param onConfirm Sepeti onaylama işlemini başlatan fonksiyon
 */
@Composable
fun ConfirmCartButton(
    cartFoodList: List<Cart>,
    appliedCoupon: FirebaseCoupon?,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: CartViewModel,
    userName: String
) {
    var showDialog by remember { mutableStateOf(false) } // Dialog görünürlülük durumu

    Button(
        onClick = { showDialog = true },  // Butona tıklanırsa dialog açılır
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = AddToCartButtonColor,
            contentColor = Color.White
        )
    ) {
        Text(text = "Sepeti Onayla", style = MaterialTheme.typography.bodyLarge)
    }

    // Onay diyaloğu
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Siparişi Onayla") },
            text = { Text("Sepeti onaylamak istediğinize emin misiniz?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAllFoodsFromCart(userName, cartFoodList)
                        showDialog = false
                        onConfirm() // Sepeti onaylayınca siparişi kaydetme işlemi
                    }
                ) {
                    Text("Evet")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }) {
                    Text("Hayır")
                }
            }
        )
    }
}


/**
 * Boş sepet mesajı. Eğer kullanıcı sepeti boş ise bu mesaj görüntülenir.
 */
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