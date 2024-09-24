package com.cloffygames.foodfleet.uix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cloffygames.foodfleet.data.entity.OrderHistory
import com.cloffygames.foodfleet.data.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderHistoryDetailViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // Sipariş geçmişi detayını çekmek için fonksiyon
    fun getOrderHistoryDetail(
        orderId: String,
        onSuccess: (OrderHistory) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            userRepository.getOrderHistory(
                onSuccess = { orders ->
                    // İlgili sipariş geçmişi verisini bul
                    val order = orders.find { it.order_id == orderId }
                    if (order != null) {
                        onSuccess(order)
                    } else {
                        onFailure(Exception("Order not found"))
                    }
                },
                onFailure = { e ->
                    onFailure(e)
                }
            )
        }
    }
}
