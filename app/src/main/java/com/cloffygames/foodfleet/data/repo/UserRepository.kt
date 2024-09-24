package com.cloffygames.foodfleet.data.repo

import com.cloffygames.foodfleet.data.datasource.UserDataSource
import com.cloffygames.foodfleet.data.entity.FavoriteFood
import com.cloffygames.foodfleet.data.entity.OrderHistory
import com.cloffygames.foodfleet.data.entity.User

class UserRepository(private val userDataSource: UserDataSource) {
    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) = userDataSource.addUser(user, onSuccess, onFailure)

    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) = userDataSource.getUser(onSuccess, onFailure)

    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) = userDataSource.updateUser(user, onSuccess, onFailure)

    fun getUserData(uid: String, onResult: (User?) -> Unit) = userDataSource.getUserData(uid, onResult)

    fun addOrderHistory(order: OrderHistory, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) =
        userDataSource.addOrderHistory(order, onSuccess, onFailure)

    fun addFavoriteFood(food: FavoriteFood, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) =
        userDataSource.addFavoriteFood(food, onSuccess, onFailure)

    fun getOrderHistory(onSuccess: (List<OrderHistory>) -> Unit, onFailure: (Exception) -> Unit) =
        userDataSource.getOrderHistory(onSuccess, onFailure)

    fun getFavoriteFoods(onSuccess: (List<FavoriteFood>) -> Unit, onFailure: (Exception) -> Unit) =
        userDataSource.getFavoriteFoods(onSuccess, onFailure)

    fun removeFavoriteFood(yemek_adi: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) =
        userDataSource.removeFavoriteFood(yemek_adi, onSuccess, onFailure)


}