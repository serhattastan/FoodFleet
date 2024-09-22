package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import com.cloffygames.foodfleet.data.repo.FirebaseFoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryDetailScreenViewModel @Inject constructor(private val firebaseFoodRepository: FirebaseFoodRepository): ViewModel() {
    val firebaseFoodList = firebaseFoodRepository.getFoodsFromFireStore()
}