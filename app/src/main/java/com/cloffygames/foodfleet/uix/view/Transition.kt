package com.cloffygames.foodfleet.uix.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.uix.viewmodel.*

import com.google.gson.Gson

/**
 * Transitions composable fonksiyonu, uygulamanın navigasyon akışını yönetir.
 * Farklı ekranlar arasında geçiş yapmak için NavHost ve NavController kullanılır.
 *
 * @param authViewModel Kullanıcının giriş durumu ve kimlik doğrulama işlemleri için
 * @param homeViewModel Ana ekran verilerini yönetmek için
 * @param profileDetailViewModel Kullanıcı profil detayları için
 * @param profileViewModel Kullanıcı profil bilgilerini yönetmek için
 * @param foodDetailViewModel Seçili yiyeceklerin detaylarını göstermek için
 * @param categoryDetailScreenViewModel Kategorilere göre detayları göstermek için
 * @param cartViewModel Sepet işlemleri için
 * @param searchViewModel Arama işlemleri için
 */
@Composable
fun Transitions(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    profileDetailViewModel: ProfileDetailViewModel,
    profileViewModel: ProfileViewModel,
    foodDetailViewModel: FoodDetailViewModel,
    categoryDetailScreenViewModel: CategoryDetailScreenViewModel,
    cartViewModel: CartViewModel,
    searchViewModel: SearchViewModel
){
    // Navigasyon kontrolcüsü, uygulama içi ekranlar arasında geçişi yönetir
    val navController = rememberNavController()

    // NavHost, hangi ekranın gösterileceğini kontrol eder
    NavHost(navController = navController, startDestination = if (authViewModel.isUserLoggedIn()) "HomeScreen" else "AuthScreen"){

        // AuthScreen: Giriş veya kayıt ekranı
        composable("AuthScreen"){
            AuthScreen(authViewModel, navController)
        }

        // HomeScreen: Ana ekran
        composable("HomeScreen"){
            HomeScreen(homeViewModel, navController)
        }

        // ProfileDetailScreen: Kullanıcı profil detayları
        composable("ProfileDetailScreen"){
            ProfileDetailScreen(profileDetailViewModel, navController)
        }

        // ProfileScreen: Kullanıcı profil ekranı
        composable("ProfileScreen"){
            ProfileScreen(navController, profileViewModel)
        }

        // FoodDetailScreen: Yiyecek detay ekranı, seçilen yiyeceğin bilgilerini gösterir
        composable(
            "FoodDetailScreen/{food}",
            arguments = listOf(
                navArgument("food"){
                    type = NavType.StringType // Parametre tipi string
                }
            )
        ){
            // Gönderilen yiyecek bilgisini JSON formatında al ve deserialize et
            val json = it.arguments?.getString("food")
            val food = Gson().fromJson(json, Food::class.java)
            FoodDetailScreen(food, navController, foodDetailViewModel)
        }

        // CategoryDetailScreen: Kategori detay ekranı, belirli bir kategoriye ait yiyecekleri gösterir
        composable(
            "CategoryDetailScreen/{categoryName}",
            arguments = listOf(
                navArgument("categoryName"){
                    type = NavType.StringType
                }
            )
        ){
            val categoryName = it.arguments?.getString("categoryName")
            CategoryDetailScreen(categoryName.toString(), navController, categoryDetailScreenViewModel)
        }

        // CartScreen: Sepet ekranı, kullanıcının seçtiği ürünleri ve kullanıcı adı ile görüntüler
        composable(
            "CartScreen/{userName}",
            arguments = listOf(
                navArgument("userName"){
                    type = NavType.StringType
                }
            )
        ){
            val userName = it.arguments?.getString("userName")
            CartScreen(navController, cartViewModel, userName.toString())
        }

        // SearchScreen: Arama ekranı, kullanıcının yiyecek araması yapmasını sağlar
        composable("SearchScreen"){
            SearchScreen(navController, searchViewModel)
        }
    }
}