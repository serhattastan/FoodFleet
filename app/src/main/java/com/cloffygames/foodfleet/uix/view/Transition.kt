package com.cloffygames.foodfleet.uix.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cloffygames.foodfleet.uix.viewmodel.AuthViewModel
import com.cloffygames.foodfleet.uix.viewmodel.HomeViewModel
import com.cloffygames.foodfleet.uix.viewmodel.ProfileDetailViewModel
import com.cloffygames.foodfleet.uix.viewmodel.ProfileViewModel

@Composable
fun Transitions(authViewModel: AuthViewModel, homeViewModel: HomeViewModel, profileDetailViewModel: ProfileDetailViewModel, profileViewModel: ProfileViewModel){
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

    }
}