package com.cloffygames.foodfleet.uix.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.cloffygames.foodfleet.uix.uicomponent.LoginScreen
import com.cloffygames.foodfleet.uix.uicomponent.RegisterScreen
import com.cloffygames.foodfleet.uix.viewmodel.AuthViewModel

@Composable
fun AuthScreen(authViewModel: AuthViewModel, navController: NavController) {
    // Delegasyon kullanmadan manuel olarak state'i tanımlıyoruz
    val isLoginScreen = remember { mutableStateOf(true) }

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            if (isLoginScreen.value) {
                LoginScreen(onRegisterClick = { isLoginScreen.value = false }, authViewModel, navController)
            } else {
                RegisterScreen(onLoginClick = { isLoginScreen.value = true }, authViewModel, navController)
            }
        }
    }
}