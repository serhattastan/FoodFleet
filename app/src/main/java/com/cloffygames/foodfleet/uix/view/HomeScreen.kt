package com.cloffygames.foodfleet.uix.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cloffygames.foodfleet.uix.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val firebaseFoodList by viewModel.firebaseFoodList.observeAsState(emptyList())
    val firebaseCategoryList by viewModel.firebaseCategoryList.observeAsState(emptyList())
    val foodList by viewModel.foodList.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
//        firebaseFoodList.forEach { firebaseFood ->
//            Text(text = firebaseFood.yemek_adi)
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        firebaseCategoryList.forEach { category ->
//            Text(text = category)
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        foodList.forEach { food ->
//            Text(text = food.yemek_adi)
//        }
//        Spacer(modifier = Modifier.height(16.dp))
//        firebaseFoodList.forEach { firebaseFood ->
//            if (firebaseFood.yemek_kategori == "Makarna") {
//                Text(text = firebaseFood.yemek_adi)
//            }
//        }
//        firebaseCategoryList.forEach { category ->
//            firebaseFoodList.forEach { firebaseFood ->
//                if (firebaseFood.yemek_kategori == category) {
//                    Text(text = firebaseFood.yemek_adi)
//                }
//            }
//        }


    }

}