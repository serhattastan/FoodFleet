package com.cloffygames.foodfleet.data.repo

import com.cloffygames.foodfleet.data.datasource.UserDataSource
import com.cloffygames.foodfleet.data.entity.User

class UserRepository(private val userDataSource: UserDataSource) {
    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) = userDataSource.addUser(user, onSuccess, onFailure)

    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) = userDataSource.getUser(onSuccess, onFailure)

    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) = userDataSource.updateUser(user, onSuccess, onFailure)

    fun getUserData(uid: String, onResult: (User?) -> Unit) = userDataSource.getUserData(uid, onResult)

}