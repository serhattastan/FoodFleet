package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloffygames.foodfleet.data.entity.FirebaseFood
import com.cloffygames.foodfleet.data.entity.Food
import com.cloffygames.foodfleet.data.repo.FirebaseFoodRepository
import com.cloffygames.foodfleet.data.repo.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(val firebaseFoodRepository: FirebaseFoodRepository, val frepo: FoodRepository) : ViewModel() {

    val firebaseFoodList: LiveData<List<FirebaseFood>> = firebaseFoodRepository.getFoodsFromFireStore()
    val firebaseCategoryList: LiveData<List<String>> = firebaseFoodRepository.getCategoriesFromFoods()
//    val firebaseFoodsByCategoryList = MutableLiveData<List<FirebaseFood>>()

    val foodList = MutableLiveData<List<Food>>()

    init {
        getFoods()
    }

    fun getFoods(){
        CoroutineScope(Dispatchers.Main).launch {
            foodList.value = frepo.getFoods()
        }
    }

}
