package com.cloffygames.foodfleet.uix.view

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.uix.viewmodel.AuthViewModel
import com.cloffygames.foodfleet.uix.viewmodel.CategoryDetailScreenViewModel
import com.cloffygames.foodfleet.uix.viewmodel.FoodDetailViewModel
import com.cloffygames.foodfleet.uix.viewmodel.HomeViewModel
import com.cloffygames.foodfleet.uix.viewmodel.ProfileDetailViewModel
import com.cloffygames.foodfleet.uix.viewmodel.ProfileViewModel
import com.google.gson.Gson

@Composable
fun Transitions(
    authViewModel: AuthViewModel,
    homeViewModel: HomeViewModel,
    profileDetailViewModel: ProfileDetailViewModel,
    profileViewModel: ProfileViewModel,
    foodDetailViewModel: FoodDetailViewModel,
    categoryDetailScreenViewModel: CategoryDetailScreenViewModel
){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = if (authViewModel.isUserLoggedIn()) "HomeScreen" else "AuthScreen"){
        composable("AuthScreen"){
            AuthScreen(authViewModel, navController)
        }
        composable("HomeScreen"){
            HomeScreen(homeViewModel, navController)
        }
        composable("ProfileDetailScreen"){
            ProfileDetailScreen(profileDetailViewModel, navController)
        }
        composable("ProfileScreen"){
            ProfileScreen(navController, profileViewModel)
        }
        composable(
            "FoodDetailScreen/{food}",
            arguments = listOf(
                navArgument("food"){
                    type = NavType.StringType
                }
            )
        ){
            val json = it.arguments?.getString("food")
            val food = Gson().fromJson(json, Food::class.java)
            FoodDetailScreen(food, navController, foodDetailViewModel)
        }

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



    }
}