package com.cloffygames.foodfleet.uix.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cloffygames.foodfleet.R
import com.cloffygames.foodfleet.data.entity.User
import com.cloffygames.foodfleet.ui.theme.ExitButtonColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.ui.theme.SecondaryColor
import com.cloffygames.foodfleet.uix.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileViewModel) {
    val coroutineScope = rememberCoroutineScope()

    // Kullanıcı bilgileri için state tanımlamaları
    var userName by remember { mutableStateOf("") }
    var userSurname by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var userAddress by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // Lottie animasyonunu yükle ve sonsuz döngüde oynat
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.profile_anim))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    // Kullanıcı bilgilerini çekme
    LaunchedEffect(Unit) {
        viewModel.getUser(
            onSuccess = { user ->
                userName = user.user_name
                userSurname = user.user_surname
                userId = user.user_id
                userAddress = user.user_address
                isLoading = false
            },
            onFailure = { /* Hata işleme kodu */ }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profil", color = Color.White) },  // Başlık rengini tema dosyasından al
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            tint = Color.White  // Geri ikonunun rengi tema ile uyumlu
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = PrimaryColor)  // Üst barın arka plan rengini ayarla
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally  // Merkezde hizala
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = PrimaryColor  // Yükleniyor göstergesinin rengi tema ile uyumlu olsun
                    )
                } else {
                    // Kullanıcı bilgilerini içeren TextField'lar
                    LottieAnimation(
                        composition = composition,
                        progress = progress,
                        modifier = Modifier
                            .size(375.dp, 250.dp) // Animasyonun boyutu
                    )
                    TextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Ad", color = PrimaryTextColor) },  // Tema ile uyumlu metin rengi
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedLabelColor = PrimaryTextColor,
                            focusedBorderColor = PrimaryColor
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = userSurname,
                        onValueChange = { userSurname = it },
                        label = { Text("Soyad", color = PrimaryTextColor) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedLabelColor = PrimaryTextColor,
                            focusedBorderColor = PrimaryColor
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = userId,
                        onValueChange = { userId = it },
                        label = { Text("Kullanıcı Adı (@örnek)", color = PrimaryTextColor) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedLabelColor = PrimaryTextColor,
                            focusedBorderColor = PrimaryColor
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = userAddress,
                        onValueChange = { userAddress = it },
                        label = { Text("Adres", color = PrimaryTextColor) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedLabelColor = PrimaryTextColor,
                            focusedBorderColor = PrimaryColor
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val updatedUser = User(
                                user_name = userName,
                                user_surname = userSurname,
                                user_id = userId,
                                user_address = userAddress
                            )
                            coroutineScope.launch {
                                viewModel.updateUser(
                                    updatedUser,
                                    onSuccess = { /* Başarılı güncelleme işlemi */ },
                                    onFailure = { /* Hata işleme */ }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
                    ) {
                        Text("Kaydet", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        viewModel.logout {
                            navController.navigate("AuthScreen")  // Çıkış sonrası yönlendirme
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(containerColor = ExitButtonColor)
                ) {
                    Text("Çıkış Yap", color = Color.White)
                }
            }
        }
    )
}
