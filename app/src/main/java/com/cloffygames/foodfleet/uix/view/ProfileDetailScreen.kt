package com.cloffygames.foodfleet.uix.view

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.cloffygames.foodfleet.data.entity.User
import com.cloffygames.foodfleet.ui.theme.BackgroundColor
import com.cloffygames.foodfleet.uix.viewmodel.ProfileDetailViewModel

@Composable
fun ProfileDetailScreen(viewModel: ProfileDetailViewModel, navController: NavController) {
    // Kullanıcı bilgileri için state'ler
    var userName by remember { mutableStateOf(TextFieldValue("")) }
    var userSurname by remember { mutableStateOf(TextFieldValue("")) }
    var userId by remember { mutableStateOf(TextFieldValue("")) }
    var userAddress by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Complete Your Profile",
            fontSize = 24.sp,
            color = Color(0xFF333333),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Kullanıcı ismi için TextField
        TextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("First Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Kullanıcı soyismi için TextField
        TextField(
            value = userSurname,
            onValueChange = { userSurname = it },
            label = { Text("Last Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Kullanıcı ID için TextField (user_id @serhat123 formatında)
        TextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID (e.g. @serhat123)") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        // Kullanıcı adresi için TextField
        TextField(
            value = userAddress,
            onValueChange = { userAddress = it },
            label = { Text("Address") },
            leadingIcon = { Icon(Icons.Default.Place, contentDescription = null) },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
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
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Save Details", color = Color.White, fontSize = 18.sp)
        }
    }
}
