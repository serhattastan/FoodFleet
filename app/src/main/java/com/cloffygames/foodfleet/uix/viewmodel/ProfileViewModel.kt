package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloffygames.foodfleet.data.entity.User
import com.cloffygames.foodfleet.data.repo.AuthenticationRepository
import com.cloffygames.foodfleet.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userRepository: UserRepository, private val authenticationRepository: AuthenticationRepository): ViewModel() {

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

    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            authenticationRepository.logout(onSuccess)
        }
    }

}