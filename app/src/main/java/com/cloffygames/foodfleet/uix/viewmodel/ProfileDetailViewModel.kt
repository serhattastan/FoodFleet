package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloffygames.foodfleet.data.entity.User
import com.cloffygames.foodfleet.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            userRepository.addUser(user, onSuccess, onFailure)
        }
    }

    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            userRepository.getUser(onSuccess, onFailure)
        }
    }

    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            userRepository.updateUser(user, onSuccess, onFailure)
        }
    }
}
