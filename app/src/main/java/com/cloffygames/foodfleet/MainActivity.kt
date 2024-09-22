package com.cloffygames.foodfleet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.cloffygames.foodfleet.ui.theme.FoodFleetTheme
import com.cloffygames.foodfleet.uix.view.Transitions
import com.cloffygames.foodfleet.uix.viewmodel.AuthViewModel
import com.cloffygames.foodfleet.uix.viewmodel.CategoryDetailScreenViewModel
import com.cloffygames.foodfleet.uix.viewmodel.FoodDetailViewModel
import com.cloffygames.foodfleet.uix.viewmodel.HomeViewModel
import com.cloffygames.foodfleet.uix.viewmodel.ProfileDetailViewModel
import com.cloffygames.foodfleet.uix.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val authViewModel : AuthViewModel by viewModels()
    val homeViewModel : HomeViewModel by viewModels()
    val profileDetailViewModel : ProfileDetailViewModel by viewModels()
    val profileViewModel : ProfileViewModel by viewModels()
    val foodDetailViewModel : FoodDetailViewModel by viewModels()
    val categoryDetailScreenViewModel : CategoryDetailScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodFleetTheme {
                Transitions(authViewModel, homeViewModel, profileDetailViewModel, profileViewModel, foodDetailViewModel, categoryDetailScreenViewModel)
            }
        }
    }
}