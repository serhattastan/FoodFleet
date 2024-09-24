package com.cloffygames.foodfleet.uix.uicomponent

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.SecondaryColor
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
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Lottie animasyonunu yükle ve sonsuz döngüde oynat
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.auth_anim))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    // Google sign-in için launcher oluşturuluyor
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
                            // Kullanıcıyı kontrol et
                            authViewModel.getCurrentUser()?.let { firebaseUser ->
                                authViewModel.checkIfUserExists(firebaseUser.uid) { userExists ->
                                    if (userExists) {
                                        // Kullanıcı mevcutsa HomeScreen'e yönlendir
                                        navController.navigate("HomeScreen") {
                                            popUpTo("LoginScreen") { inclusive = true }
                                        }
                                    } else {
                                        // Kullanıcı mevcut değilse ProfileDetailScreen'e yönlendir
                                        navController.navigate("ProfileDetailScreen") {
                                            popUpTo("LoginScreen") { inclusive = true }
                                        }
                                    }
                                }
                            }
                        } else {
                            errorMessage = error ?: "Google Sign-In Başarısız!"
                        }
                    }
                }
            } else {
                errorMessage = "Google Sign-In Başarısız!"
            }
        }
    }

    // UI bileşenlerini barındıran kolon düzeni
    Column(
        modifier = Modifier
            .fillMaxSize() // Tüm ekranı kapla
            .padding(16.dp)
            .background(BackgroundColor), // Ekranın kenarlarından 16dp boşluk bırak
        horizontalAlignment = Alignment.CenterHorizontally, // Yatayda ortala
        verticalArrangement = Arrangement.Center // Dikeyde ortala
    ) {
        // Lottie animasyon bileşeni
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .size(375.dp, 250.dp) // Animasyonun boyutu
        )

        Spacer(modifier = Modifier.height(24.dp)) // 24dp boşluk

        // Hoşgeldiniz başlığı
        Text(
            text = "Hoşgeldin!",
            fontWeight = FontWeight.Bold, // Kalın font
            fontSize = 24.sp,
            color = PrimaryColor // Tema renk dosyasından ana renk
        )

        Spacer(modifier = Modifier.height(16.dp)) // 16dp boşluk

        // E-posta giriş alanı
        OutlinedTextField(
            value = email,
            onValueChange = { email = it }, // Kullanıcı e-posta girdiğinde değeri güncelle
            label = { Text("Email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), // E-posta klavyesi
            modifier = Modifier.fillMaxWidth(), // Tüm genişliği kapla
            shape = RoundedCornerShape(12.dp), // Köşeleri yuvarlat
        )

        Spacer(modifier = Modifier.height(12.dp)) // 12dp boşluk

        // Parola giriş alanı
        OutlinedTextField(
            value = password,
            onValueChange = { password = it }, // Kullanıcı parola girdiğinde değeri güncelle
            label = { Text("Parola") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), // Parola klavyesi
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(), // Şifre görünürlüğü
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                // Şifre görünür/gizli ikonu
                val image = if (passwordVisible)
                    painterResource(id = R.drawable.icon_visibility_on)
                else
                    painterResource(id = R.drawable.icon_visibility_off)

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = image, contentDescription = null, modifier = Modifier.size(20.dp))
                }
            },
            shape = RoundedCornerShape(12.dp),
        )

        Spacer(modifier = Modifier.height(24.dp)) // 24dp boşluk

        // Giriş yap butonu
        Button(
            onClick = {
                authViewModel.login(email, password) { success, error ->
                    if (success) {
                        // Giriş başarılıysa ana ekrana yönlendirme
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
                .height(45.dp), // Butonun yüksekliği
            shape = RoundedCornerShape(12.dp), // Köşeleri yuvarlat
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor) // Tema ana rengi
        ) {
            Text(text = "Giriş Yap", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        // Hata mesajı gösterimi
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp)) // 12dp boşluk
            Text(text = errorMessage, color = Color.Red) // Hata mesajını kırmızı renkle göster
        }

        Spacer(modifier = Modifier.height(12.dp)) // 12dp boşluk

        // Google ile giriş yap butonu
        Button(
            onClick = {
                val signInIntent = authViewModel.getGoogleSignInClient().signInIntent
                launcher.launch(signInIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp), // Butonun yüksekliği
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4285F4), contentColor = Color.White) // Google buton rengi
        ) {
            Text(text = "Google ile Giriş Yap", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp)) // 24dp boşluk

        // Kayıt sayfasına yönlendirme linki
        Text(
            text = "Hesabınız yok mu? Kayıt olun.",
            modifier = Modifier.clickable { onRegisterClick() }, // Kayıt sayfasına yönlendirme
            color = SecondaryColor, // Tema ikincil rengi
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
