package com.cloffygames.foodfleet.uix.view

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.data.entity.FirebaseCoupon
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.ui.theme.AddToCartButtonColor
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.OnPrimaryTextColor
import com.cloffygames.foodfleet.ui.theme.OnSecondaryTextColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.ui.theme.SecondaryColor
import com.cloffygames.foodfleet.ui.theme.SecondaryTextColor
import com.cloffygames.foodfleet.ui.theme.ShimmerBaseColor
import com.cloffygames.foodfleet.uix.uicomponent.ShimmerEffect
import com.cloffygames.foodfleet.uix.viewmodel.HomeViewModel
import com.google.gson.Gson
import com.skydoves.landscapist.glide.GlideImage

/**
 * HomeScreen bileşeni, ana sayfa ekranının oluşturulmasını sağlar. Bu ekran,
 * kullanıcıya kategori, yiyecek ve kuponların gösterilmesini içerir.
 *
 * @param viewModel Ana ekranın ViewModel'i
 * @param navController Uygulama içinde diğer ekranlara geçişi sağlayan NavController
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    // ViewModel'deki veriler izleniyor
    val firebaseFoodList by viewModel.firebaseFoodList.observeAsState(emptyList())
    val firebaseCategoryList by viewModel.firebaseCategoryList.observeAsState(emptyMap())
    val foodList by viewModel.foodList.observeAsState(emptyList())
    val firebaseCouponList by viewModel.firebaseCouponList.observeAsState(emptyList())

    // Kullanıcı bilgilerini almak için state tanımları
    var userAddress by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }

    // Kullanıcı verilerini almak için LaunchedEffect kullanılıyor
    LaunchedEffect(Unit) {
        viewModel.getUser(
            onSuccess = { user ->
                userAddress = user.user_address
                userName = user.user_id
            },
            onFailure = { /* Hata işleme kodu */ }
        )
    }

    // Scaffold, üstte AppBar ve içerik alanını düzenler
    Scaffold(
        topBar = { HomeTopAppBar(navController, userAddress, userName) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(BackgroundColor)
        ) {
            // Search bar öğesi
            item {
                SearchBar(navController = navController)
                VerticalDivider() // Dikey ayraç
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Kuponların gösterilmesi için bir pager
            item {
                CouponPager(firebaseCouponList)
            }

            // Kategori listesi
            item {
                CategoryList(firebaseCategoryList, navController)
            }

            // Sırala ve filtrele butonları
            item {
                SortAndFilterButtons()
            }

            // Yana kaydırılabilir grid görünüm
            item {
                FoodGrid(foodList = foodList, navController = navController, viewModel = viewModel, userName = userName)
            }

            // Kategorilere göre yiyecekleri listele
            items(firebaseCategoryList.keys.toList()) { category ->
                val filteredFoodList = firebaseFoodList.filter { it.yemek_kategori == category }
                CategoryFoodList(filteredFoodList, category, navController, viewModel, userName)
            }
        }
    }
}

/**
 * HomeTopAppBar bileşeni, ana sayfanın üst barını oluşturur.
 * Kullanıcı adresini ve Food Fleet başlığını gösterir.
 * Profil ve sepet simgelerine tıklanabilir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(navController: NavController, userAddress: String, userName: String) {
    TopAppBar(
        colors = (TopAppBarDefaults.topAppBarColors(containerColor = BackgroundColor)),
        title = {
            Column {
                Text(
                    text = userAddress,
                    style = MaterialTheme.typography.labelLarge,
                    color = PrimaryTextColor
                )
                Text(
                    text = "Food Fleet",
                    style = MaterialTheme.typography.labelSmall,
                    color = PrimaryTextColor
                )
            }
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ManageAccounts,
                contentDescription = "Profil Simgesi",
                tint = PrimaryTextColor,
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                    .size(32.dp)
                    .clickable { navController.navigate("ProfileScreen") }
            )
        },
        actions = {
            IconButton(onClick = { /* Favorilere gitme işlevi */ }) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favoriler Icon",
                    tint = AddToCartButtonColor
                )
            }
            IconButton(onClick = { navController.navigate("CartScreen/$userName") }) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Sepet Icon",
                    tint = AddToCartButtonColor
                )
            }
        }
    )
}

/**
 * CouponPager bileşeni, kuponları yana kaydırılabilir bir yapı ile gösterir.
 *
 * @param firebaseCouponList Gösterilecek kuponların listesi
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CouponPager(firebaseCouponList: List<FirebaseCoupon>) {
    var currentPage by remember { mutableIntStateOf(0) }
    val lazyListState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxWidth()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            state = lazyListState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState)
        ) {
            itemsIndexed(firebaseCouponList) { _, coupon ->
                Card(
                    modifier = Modifier
                        .height(180.dp)
                        .fillParentMaxWidth()
                        .padding(horizontal = 2.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        GlideImage(
                            imageModel = coupon.coupon_image,
                            contentDescription = coupon.coupon_name,
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(1.8f),
                            loading = {
                                ShimmerEffect(modifier = Modifier.fillMaxSize())
                            }
                        )
                    }
                }
            }
        }
        PagerIndicator(totalPages = firebaseCouponList.size, currentPage = currentPage)

        LaunchedEffect(lazyListState) {
            snapshotFlow { lazyListState.firstVisibleItemIndex }
                .collect { currentPage = it }
        }
    }
}

/**
 * PagerIndicator bileşeni, kupon pager'ının sayfa göstergelerini oluşturur.
 *
 * @param totalPages Toplam sayfa sayısı
 * @param currentPage Mevcut sayfa
 */
@Composable
fun PagerIndicator(totalPages: Int, currentPage: Int) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        for (i in 0 until totalPages) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (i == currentPage) PrimaryColor else ShimmerBaseColor)
            )
        }
    }
}

/**
 * SearchBar bileşeni, arama çubuğunu oluşturur ve tıklanınca arama ekranına yönlendirir.
 */
@Composable
fun SearchBar(navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate("SearchScreen")
            },
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
            Text(
                text = "Yemek ara...",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryTextColor
            )
        }
    }
}

/**
 * CategoryList bileşeni, kategorileri yatay kaydırılabilir bir listede gösterir.
 *
 * @param categories Kategori isimleri ve görsellerinin haritası
 * @param navController Navigasyon kontrolcüsü
 */
@Composable
fun CategoryList(categories: Map<String, String>, navController: NavController) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories.keys.toList()) { category ->
            CategoryCard(category = category, imageUrl = categories[category], navController = navController)
        }
    }
}

/**
 * CategoryCard bileşeni, her bir kategoriyi kart olarak gösterir.
 *
 * @param category Kategori ismi
 * @param imageUrl Kategori görsel URL'si
 * @param navController Navigasyon kontrolcüsü
 */
@Composable
fun CategoryCard(category: String, imageUrl: String?, navController: NavController) {
    Card(
        modifier = Modifier
            .width(125.dp)
            .height(125.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColor)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GlideImage(
                imageModel = imageUrl,
                contentDescription = "Food Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable {
                        navController.navigate("CategoryDetailScreen/$category")
                    },
                loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
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

/**
 * SortAndFilterButtons bileşeni, sıralama ve filtreleme butonlarını gösterir.
 */
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
                containerColor = BackgroundColor
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Sırala Icon",
                modifier = Modifier.padding(end = 4.dp),
                tint = OnPrimaryTextColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Sırala", color = OnPrimaryTextColor)
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(
            onClick = { /* Filtrele butonuna tıklama işlevi */ },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = SecondaryColor
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filtrele Icon",
                modifier = Modifier.padding(end = 4.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Filtrele", color = OnSecondaryTextColor)
        }
    }
}

/**
 * FoodGrid bileşeni, yiyecekleri yana kaydırılabilir grid biçiminde gösterir.
 *
 * @param foodList Yiyeceklerin listesi
 * @param navController Navigasyon kontrolcüsü
 * @param viewModel Ana ekranın ViewModel'i
 * @param userName Kullanıcı ismi
 */
@Composable
fun FoodGrid(foodList: List<Food>, navController: NavController, viewModel: HomeViewModel, userName: String) {
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
                    FoodCard(food = food, navController = navController, viewModel, userName)
                }
            }
        }
    }
}

/**
 * FoodCard bileşeni, her bir yiyecek öğesini kart olarak gösterir.
 *
 * @param food Yiyecek bilgisi
 * @param navController Navigasyon kontrolcüsü
 * @param viewModel Ana ekranın ViewModel'i
 * @param userName Kullanıcı ismi
 */
@Composable
fun FoodCard(food: Food, navController: NavController, viewModel: HomeViewModel, userName : String) {
    Card(
        modifier = Modifier
            .size(160.dp, 155.dp)
            .clickable {
                val json = Gson().toJson(food)
                navController.navigate("FoodDetailScreen/${json}"
                ) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                GlideImage(
                    imageModel = "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}",
                    contentDescription = food.yemek_adi,
                    modifier = Modifier.fillMaxSize(),
                    loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
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

                IconButton(
                    onClick = { /* Favorilere ekle işlemi */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to Favorite",
                        tint = AddToCartButtonColor
                    )
                }

                IconButton(
                    onClick = { viewModel.addFoodToCart(food.yemek_adi, "http://kasimadalan.pe.hu/yemekler/resimler/${food.yemek_resim_adi}", food.yemek_fiyat, 1, userName) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to Cart",
                        tint = AddToCartButtonColor
                    )
                }
            }
        }
    }
}

/**
 * FoodCardFirebase bileşeni, Firebase'deki yiyecekleri kart olarak gösterir.
 *
 * @param food Firebase'den gelen yiyecek bilgisi
 * @param navController Navigasyon kontrolcüsü
 * @param viewModel Ana ekranın ViewModel'i
 * @param userName Kullanıcı ismi
 */
@Composable
fun FoodCardFirebase(food: FirebaseFood, navController: NavController, viewModel: HomeViewModel, userName : String) {
    Card(
        modifier = Modifier
            .size(225.dp, 155.dp)
            .clickable {
                val transitionFood = Food(food.yemek_id, food.yemek_adi, food.yemek_resim_adi, food.yemek_fiyat.toInt())
                val json = Uri.encode(Gson().toJson(transitionFood))
                navController.navigate("FoodDetailScreen/${json}")
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundColor)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
            ) {
                GlideImage(
                    imageModel = food.yemek_resim_adi,
                    contentDescription = food.yemek_adi,
                    modifier = Modifier.fillMaxSize(),
                    loading = { ShimmerEffect(modifier = Modifier.fillMaxSize()) }
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = food.yemek_adi,
                        style = MaterialTheme.typography.labelLarge,
                        color = PrimaryTextColor
                    )
                    Text(
                        text = "${food.yemek_fiyat} TL",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryTextColor
                    )
                }

                IconButton(
                    onClick = { /* Sepete ekle işlemi */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "Add to Cart",
                        tint = AddToCartButtonColor
                    )
                }

                IconButton(
                    onClick = { viewModel.addFoodToCart(food.yemek_adi, food.yemek_resim_adi, food.yemek_fiyat.toInt(), 1, userName) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Add to Cart",
                        tint = AddToCartButtonColor
                    )
                }
            }
        }
    }
}

/**
 * CategoryFoodList bileşeni, belirli bir kategorideki yiyecekleri listeler.
 *
 * @param filteredFoodList Kategorideki yiyeceklerin listesi
 * @param category Kategori ismi
 * @param navController Navigasyon kontrolcüsü
 * @param viewModel Ana ekranın ViewModel'i
 * @param userName Kullanıcı ismi
 */
@Composable
fun CategoryFoodList(filteredFoodList: List<FirebaseFood>, category: String, navController: NavController, viewModel: HomeViewModel, userName : String) {
    Column {
        Text(
            text = category,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp, end = 16.dp),
            color = PrimaryTextColor,
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredFoodList) { food ->
                FoodCardFirebase(food = food, navController = navController, viewModel = viewModel, userName = userName)
            }
        }
    }
}