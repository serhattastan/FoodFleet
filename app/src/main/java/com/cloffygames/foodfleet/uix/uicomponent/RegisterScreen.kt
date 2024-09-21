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
 * RegisterScreen composable fonksiyonu, kullanıcı kayıt ekranını oluşturur.
 * Lottie animasyonu, e-posta, parola ve parola onay giriş alanları, kayıt butonu ve giriş ekranına yönlendirme içerir.
 *
 * @param onLoginClick Kullanıcı giriş ekranına yönlendirilmek istediğinde tetiklenir.
 */
@Composable
fun RegisterScreen(onLoginClick: () -> Unit, authViewModel: AuthViewModel, navController: NavController) {
    // State değişkenlerini tanımlıyoruz
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // Şifre görünürlüğünü kontrol eden state
    var confirmPasswordVisible by remember { mutableStateOf(false) } // Şifre onayının görünürlüğünü kontrol eden state
    var isEmailValid by remember { mutableStateOf(true) }  // E-posta doğrulama state
    var isPasswordValid by remember { mutableStateOf(true) }  // Şifre doğrulama state
    var isConfirmPasswordValid by remember { mutableStateOf(true) }  // Şifre eşleşme doğrulama state
    var isEmailEmpty by remember { mutableStateOf(false) } // E-posta alanı boş mu?
    var isPasswordEmpty by remember { mutableStateOf(false) } // Şifre alanı boş mu?
    var isConfirmPasswordEmpty by remember { mutableStateOf(false) } // Şifre onayı boş mu?

    // Lottie animasyonunu yüklemek ve ilerlemesini kontrol etmek için kompozisyonu oluşturur
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.register_anim))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)


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
            modifier = Modifier.size(200.dp, 200.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Kayıt başlığı
        Text(
            text = "Create an Account",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // E-posta giriş alanı
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailEmpty = email.isEmpty()
                isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            },
            label = { Text("Email") },
            isError = !isEmailValid || isEmailEmpty,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        if (isEmailEmpty) {
            Text(
                text = "Email cannot be empty",
                color = Color.Red,
                fontSize = 12.sp
            )
        } else if (!isEmailValid) {
            Text(
                text = "Invalid email format",
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Parola giriş alanı
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordEmpty = password.isEmpty()
                isPasswordValid = password.length >= 6
            },
            label = { Text("Password") },
            isError = !isPasswordValid || isPasswordEmpty,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
        if (isPasswordEmpty) {
            Text(
                text = "Password cannot be empty",
                color = Color.Red,
                fontSize = 12.sp
            )
        } else if (!isPasswordValid) {
            Text(
                text = "Password must be at least 6 characters",
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Parola onay giriş alanı
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                isConfirmPasswordEmpty = confirmPassword.isEmpty()
                isConfirmPasswordValid = password == confirmPassword
            },
            label = { Text("Confirm Password") },
            isError = !isConfirmPasswordValid || isConfirmPasswordEmpty,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                val image = if (confirmPasswordVisible)
                    painterResource(id = R.drawable.icon_visibility_on)
                else
                    painterResource(id = R.drawable.icon_visibility_off)

                IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Icon(
                        painter = image,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            shape = RoundedCornerShape(12.dp)
        )
        if (isConfirmPasswordEmpty) {
            Text(
                text = "Confirm Password cannot be empty",
                color = Color.Red,
                fontSize = 12.sp
            )
        } else if (!isConfirmPasswordValid) {
            Text(
                text = "Passwords do not match",
                color = Color.Red,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kayıt butonu
        Button(
            onClick = {
                // Alanların boş olup olmadığını kontrol ediyoruz
                isEmailEmpty = email.isEmpty()
                isPasswordEmpty = password.isEmpty()
                isConfirmPasswordEmpty = confirmPassword.isEmpty()

                if (!isEmailEmpty && !isPasswordEmpty && !isConfirmPasswordEmpty && isEmailValid && isPasswordValid && isConfirmPasswordValid) {
                    authViewModel.register(email, password) { success, error ->
                        if (success) {
                            // Kayıt başarılı olduğunda HomeScreen'e yönlendirme
                            navController.navigate("ProfileDetailScreen") {
                                popUpTo("AuthScreen") { inclusive = true }  // LoginScreen'i yığından çıkar
                                launchSingleTop = true  // Aynı ekranı üst üste açmayı engelle
                            }
                        } else {
                            errorMessage = error ?: "Unknown error"
                        }
                    }
                } else {
                    errorMessage = "Please fill in all fields correctly."
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(text = "Sign Up", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }


        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Giriş ekranına yönlendirme linki
        Text(
            text = "Already have an account? Sign in",
            modifier = Modifier.clickable { onLoginClick() },
            color = MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}



