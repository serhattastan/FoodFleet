package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloffygames.foodfleet.data.entity.OrderHistory
import com.cloffygames.foodfleet.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    /**
     * Sipariş geçmişini Firebase'den alır.
     *
     * @param onSuccess Sipariş geçmişini başarılı şekilde aldıktan sonra tetiklenir.
     * @param onFailure Bir hata oluşursa bu fonksiyon tetiklenir.
     */
    fun getOrderHistory(
        onSuccess: (List<OrderHistory>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Firebase'den sipariş geçmişini alır
                userRepository.getOrderHistory(
                    onSuccess = { orders ->
                        onSuccess(orders)
                    },
                    onFailure = { error ->
                        onFailure(error)
                    }
                )
            } catch (e: Exception) {
                onFailure(e)
            }
        }
    }
}