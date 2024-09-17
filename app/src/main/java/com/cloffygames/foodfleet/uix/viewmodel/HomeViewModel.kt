package com.cloffygames.foodfleet.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.repo.FirebaseFoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val firebaseFoodRepository: FirebaseFoodRepository) : ViewModel() {

    val firebaseFoodList: LiveData<List<FirebaseFood>> = firebaseFoodRepository.getFoodsFromFireStore()
    val firebaseCategoryList: LiveData<List<String>> = firebaseFoodRepository.getCategoriesFromFoods()

    fun fetchAllFirebaseFoods() {
        firebaseFoodRepository.getFoodsFromFireStore()
    }

    fun fetchFirebaseFoodsByCategory(category: String) {
        firebaseFoodRepository.getFoodsByCategory(category)
    }

    fun fetchFirebaseFoodCategories() {
        firebaseFoodRepository.getCategoriesFromFoods()
    }
}
