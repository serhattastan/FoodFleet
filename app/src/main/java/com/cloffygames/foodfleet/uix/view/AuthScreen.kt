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

/**
 * AuthScreen, kullanıcı giriş ve kayıt ekranları arasında geçiş yapmayı sağlar.
 * Kullanıcı, LoginScreen veya RegisterScreen arasında geçiş yapabilir.
 *
 * @param authViewModel Kullanıcı kimlik doğrulama işlemlerini yönetmek için kullanılan ViewModel
 * @param navController Uygulama içi ekranlar arasında geçiş yapmak için kullanılan NavController
 */
@Composable
fun AuthScreen(authViewModel: AuthViewModel, navController: NavController) {
    // Giriş ekranı mı yoksa kayıt ekranı mı gösterileceğini kontrol eden state
    val isLoginScreen = remember { mutableStateOf(true) }

    // Scaffold bileşeni, üst, alt çubuklar gibi sabit içerikleri ve ekranın ana içeriğini düzenler.
    Scaffold { paddingValues ->
        // Column, alt alta sıralanacak bileşenler için bir düzen sağlar.
        Column(modifier = Modifier.padding(paddingValues)) {
            // Eğer isLoginScreen true ise LoginScreen'i gösterir, aksi takdirde RegisterScreen'i gösterir
            if (isLoginScreen.value) {
                LoginScreen(
                    onRegisterClick = { isLoginScreen.value = false }, // Kayıt ekranına geçiş
                    authViewModel = authViewModel,
                    navController = navController
                )
            } else {
                RegisterScreen(
                    onLoginClick = { isLoginScreen.value = true }, // Giriş ekranına geri dön
                    authViewModel = authViewModel,
                    navController = navController
                )
            }
        }
    }
}
