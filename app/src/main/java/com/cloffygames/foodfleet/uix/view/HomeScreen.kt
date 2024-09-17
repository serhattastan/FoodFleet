package com.cloffygames.foodfleet.uix.view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.cloffygames.foodfleet.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController
) {
    val firebaseFoodList by viewModel.firebaseFoodList.observeAsState(emptyList())
    val firehaseCategoryList by viewModel.firebaseCategoryList.observeAsState(emptyList())

}