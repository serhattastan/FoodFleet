package com.cloffygames.foodfleet.uix.uicomponent

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cloffygames.foodfleet.R
import com.cloffygames.foodfleet.uix.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

/**
 * LoginScreen composable fonksiyonu, kullanıcı giriş ekranını oluşturur.
 * Lottie animasyonu, e-posta ve parola giriş alanları, giriş butonları ve kayıt sayfasına yönlendirme içerir.
 *
 * @param onRegisterClick Kullanıcı kayıt ekranına yönlendirilmek istediğinde tetiklenir.
 */
@Composable
fun LoginScreen(onRegisterClick: () -> Unit, authViewModel: AuthViewModel, navController: NavController) {
    // State değişkenlerini tanımlıyoruz
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }  // Şifre görünürlüğünü kontrol eden state

    // Lottie animasyonu
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.auth_anim))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        task.addOnCompleteListener { accountTask ->
            if (accountTask.isSuccessful) {
                val account = accountTask.result
                account?.idToken?.let { token ->
                    authViewModel.signInWithGoogle(token) { success, error ->
                        if (success) {
                            // Google girişi başarılı, HomeScreen'e yönlendirme
                            navController.navigate("HomeScreen") {
                                popUpTo("LoginScreen") { inclusive = true }
                            }
                        } else {
                            errorMessage = error ?: "Google Sign-In failed"
                        }
                    }
                }
            } else {
                errorMessage = "Google Sign-In failed"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Lottie animasyon bileşeni
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .size(375.dp, 250.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Hoşgeldiniz başlığı
        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // E-posta giriş alanı
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Parola giriş alanı
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.icon_visibility_on)
                else
                    painterResource(id = R.drawable.icon_visibility_off)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = null, modifier = Modifier.size(20.dp))
                }
            },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Giriş yap butonu
        Button(
            onClick = {
                authViewModel.login(email, password) { success, error ->
                    if (success) {
                        navController.navigate("HomeScreen") {
                            popUpTo("AuthScreen") { inclusive = true }
                        }
                    } else {
                        errorMessage = error ?: "Unknown error"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = "Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Google ile giriş yap butonu
        Button(
            onClick = {
                val signInIntent = authViewModel.getGoogleSignInClient().signInIntent
                launcher.launch(signInIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4), contentColor = Color.White)
        ) {
            Text(text = "Sign in with Google", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Kayıt sayfasına yönlendirme linki
        Text(
            text = "Don't have an account? Sign up",
            modifier = Modifier.clickable { onRegisterClick() },
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}



