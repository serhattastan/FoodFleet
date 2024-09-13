package com.cloffygames.foodfleet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.cloffygames.foodfleet.ui.theme.FoodFleetTheme
import com.cloffygames.foodfleet.uix.view.Transitions
import com.cloffygames.foodfleet.uix.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val authViewModel : AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FoodFleetTheme {
                Transitions(authViewModel)
            }
        }
    }
}