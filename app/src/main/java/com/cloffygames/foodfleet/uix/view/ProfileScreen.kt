package com.cloffygames.foodfleet.uix.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.data.entity.User
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
                title = { Text("Profil") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Geri")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Uygulama logosuna tıklama işlemi */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Uygulama Logosu")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    TextField(
                        value = userName,
                        onValueChange = { userName = it },
                        label = { Text("Ad") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = userSurname,
                        onValueChange = { userSurname = it },
                        label = { Text("Soyad") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = userId,
                        onValueChange = { userId = it },
                        label = { Text("Kullanıcı Adı (@örnek)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = userAddress,
                        onValueChange = { userAddress = it },
                        label = { Text("Adres") },
                        modifier = Modifier.fillMaxWidth()
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Kaydet")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        viewModel.logout {
                            navController.navigate("AuthScreen") // Çıkış sonrası yönlendirme
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text("Çıkış Yap")
                }
            }
        }
    )
}
