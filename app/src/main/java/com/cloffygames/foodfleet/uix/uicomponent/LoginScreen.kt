package com.cloffygames.foodfleet.uix.uicomponent

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

    // Lottie animasyonunu yüklemek ve ilerlemesini kontrol etmek için kompozisyonu oluşturur
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.auth_anim))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),  // Genel boşluklar
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Lottie animasyon bileşeni
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .size(375.dp, 250.dp)  // Animasyon boyutları
        )

        Spacer(modifier = Modifier.height(24.dp))  // Animasyon ile başlık arasındaki boşluk

        // Hoşgeldiniz başlığı
        Text(
            text = "Welcome!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp  // Başlık yazı boyutu
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))  // Başlık ile giriş alanları arasındaki boşluk

        // E-posta giriş alanı
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),  // Klavye tipi: E-posta
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)  // Yuvarlatılmış kenarlar
        )

        Spacer(modifier = Modifier.height(12.dp))  // E-posta ve şifre alanı arasındaki boşluk

        // Parola giriş alanı
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),  // Klavye tipi: Parola
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),  // Şifreyi gizle/göster
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.icon_visibility_on)
                else
                    painterResource(id = R.drawable.icon_visibility_off)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = image,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)  // İkon boyutunu ayarla
                    )
                }
            },
            shape = RoundedCornerShape(12.dp)  // Yuvarlatılmış kenarlar
        )

        Spacer(modifier = Modifier.height(24.dp))  // Şifre alanı ile butonlar arasındaki boşluk

        // Giriş yap butonu
        Button(
            onClick = {
                authViewModel.login(email, password) { success, error ->
                    if (success) {
                        // Başarılı girişte HomeScreen'e yönlendirme, geri dönülemez şekilde
                        navController.navigate("HomeScreen") {
                            popUpTo("AuthScreen") { inclusive = true }  // LoginScreen'i yığından çıkar
                            launchSingleTop = true  // Aynı ekranı üst üste açmayı engelle
                        }
                    } else {
                        errorMessage = error ?: "Unknown error"
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),  // Buton boyutları
            shape = RoundedCornerShape(12.dp),  // Yuvarlatılmış kenarlar
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary  // Buton arka plan rengi
            )
        ) {
            Text(text = "Sign In", fontSize = 16.sp, fontWeight = FontWeight.Bold)  // Buton yazı stili
        }

        // Error mesajı gösterme
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))  // Boşluk
            Text(text = errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(12.dp))  // Giriş butonu ile Google butonu arasındaki boşluk

        // Google ile giriş yap butonu
        Button(
            onClick = { /* TODO: Google ile giriş işlemi işlenir */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),  // Buton boyutları
            shape = RoundedCornerShape(12.dp),  // Yuvarlatılmış kenarlar
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4285F4),  // Google mavi rengi
                contentColor = Color.White  // Yazı rengi: Beyaz
            )
        ) {
            Text(text = "Sign in with Google", fontSize = 16.sp, fontWeight = FontWeight.Bold)  // Buton yazı stili
        }

        Spacer(modifier = Modifier.height(24.dp))  // Google butonu ile kayıt linki arasındaki boşluk

        // Kayıt sayfasına yönlendirme linki
        Text(
            text = "Don't have an account? Sign up",
            modifier = Modifier.clickable { onRegisterClick() },  // Tıklanabilir metin
            color = MaterialTheme.colorScheme.primary,  // Metin rengi
            fontSize = 14.sp,  // Metin boyutu
            fontWeight = FontWeight.Medium
        )
    }
}


