package com.cloffygames.foodfleet.uix.view

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cloffygames.foodfleet.uix.viewmodel.AuthViewModel

@Composable
fun Transitions(authViewModel: AuthViewModel){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = if (authViewModel.isUserLoggedIn()) "HomeScreen" else "AuthScreen"){
        composable("AuthScreen"){
            AuthScreen(authViewModel, navController)
        }
        composable("HomeScreen"){
            HomeScreen()
        }

    }
}