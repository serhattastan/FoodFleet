package com.cloffygames.foodfleet.uix.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.cloffygames.foodfleet.R
import com.cloffygames.foodfleet.data.entity.User
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.ui.theme.PrimaryColor
import com.cloffygames.foodfleet.ui.theme.PrimaryTextColor
import com.cloffygames.foodfleet.uix.viewmodel.ProfileDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDetailScreen(viewModel: ProfileDetailViewModel, navController: NavController) {
    // Kullanıcı bilgileri için state'ler
    var userName by remember { mutableStateOf(TextFieldValue("")) }
    var userSurname by remember { mutableStateOf(TextFieldValue("")) }
    var userId by remember { mutableStateOf(TextFieldValue("")) }
    var userAddress by remember { mutableStateOf(TextFieldValue("")) }

    // Lottie animasyonunu yükle ve sonsuz döngüde oynat
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.profile_icon_anim))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)  // Tema dosyasından arka plan rengi
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .size(375.dp, 250.dp) // Animasyonun boyutu
        )

        Text(
            text = "Complete Your Profile",
            fontSize = 24.sp,
            color = PrimaryTextColor,  // Tema dosyasındaki metin rengi
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Kullanıcı ismi için TextField
        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("First Name", color = PrimaryTextColor) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = PrimaryTextColor,
                focusedBorderColor = PrimaryColor
            )
        )

        // Kullanıcı soyismi için TextField
        TextField(
            value = userSurname,
            onValueChange = { userSurname = it },
            label = { Text("Last Name", color = PrimaryTextColor) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = PrimaryTextColor,
                focusedBorderColor = PrimaryColor
            )
        )

        // Kullanıcı ID için TextField (user_id @serhat123 formatında)
        TextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID (e.g. @serhat123)", color = PrimaryTextColor) },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = PrimaryTextColor,
                focusedBorderColor = PrimaryColor
            )
        )

        // Kullanıcı adresi için TextField
        TextField(
            value = userAddress,
            onValueChange = { userAddress = it },
            label = { Text("Address", color = PrimaryTextColor) },
            leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedLabelColor = PrimaryTextColor,
                focusedBorderColor = PrimaryColor
            )
        )

        // Kaydet Butonu
        Button(
            onClick = {
                val user = User(
                    user_name = userName.text,
                    user_surname = userSurname.text,
                    user_id = userId.text,  // @serhat123 gibi format bekleniyor
                    user_address = userAddress.text
                )
                viewModel.addUser(user, {
                    // Başarılı işlem sonrası HomeScreen'e yönlendirme
                    Toast.makeText(context, "User details saved successfully!", Toast.LENGTH_LONG).show()
                    navController.navigate("HomeScreen")
                }, { e ->
                    // Hata durumunda mesaj gösterme
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryColor)
        ) {
            Text(text = "Save Details", color = Color.White, fontSize = 18.sp)
        }
    }
}
