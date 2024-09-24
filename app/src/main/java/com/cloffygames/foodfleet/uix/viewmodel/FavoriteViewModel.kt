package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloffygames.foodfleet.data.entity.FavoriteFood
import com.cloffygames.foodfleet.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Favori yemekleri getirir
    fun getFavoriteFoods(
        onSuccess: (List<FavoriteFood>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                userRepository.getFavoriteFoods(onSuccess, onFailure)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    // Favorilere yeni bir yemek ekler
    fun addFavoriteFood(
        food: FavoriteFood,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                userRepository.addFavoriteFood(food, onSuccess, onFailure)
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }

    fun checkIfFavorite(yemek_adi: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            userRepository.getFavoriteFoods(
                onSuccess = { favoriteFoods ->
                    val isFavorite = favoriteFoods.any { it.yemek_adi == yemek_adi }
                    onResult(isFavorite)
                },
                onFailure = { e ->
                    onResult(false)
                }
            )
        }
    }

    fun removeFavoriteFood(yemek_adi: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            userRepository.removeFavoriteFood(yemek_adi, onSuccess, onFailure)
        }
    }


}