package com.cloffygames.foodfleet.data.datasource

import com.cloffygames.foodfleet.data.entity.FavoriteFood
import com.cloffygames.foodfleet.data.entity.OrderHistory
import com.cloffygames.foodfleet.data.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UserDataSource(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {
    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        uid?.let {
            val userDocument: DocumentReference = firestore.collection("users").document(it)
            userDocument.set(user)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }

    fun getUser(onSuccess: (User) -> Unit, onFailure: (Exception) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        uid?.let {
            val userDocument = firestore.collection("users").document(it)
            userDocument.get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    user?.let { onSuccess(it) }
                }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }

    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        uid?.let {
            val userDocument: DocumentReference = firestore.collection("users").document(it)
            userDocument.set(user, SetOptions.merge()) // SetOptions.merge() ile sadece belirtilen alanları güncelle
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }


    fun getUserData(uid: String, onResult: (User?) -> Unit) {
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val user = document.toObject(User::class.java)
                onResult(user)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun addOrderHistory(order: OrderHistory, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        uid?.let {
            val orderCollection = firestore.collection("users").document(it).collection("order_history")
            val orderId = orderCollection.document().id  // Benzersiz bir sipariş ID'si oluşturulur
            val orderWithId = order.copy(order_id = orderId)
            orderCollection.document(orderId).set(orderWithId)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }

    fun addFavoriteFood(food: FavoriteFood, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        uid?.let {
            val favoritesCollection = firestore.collection("users").document(it).collection("favorites")
            favoritesCollection.document(food.yemek_id.toString()).set(food)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }


    fun getOrderHistory(onSuccess: (List<OrderHistory>) -> Unit, onFailure: (Exception) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        uid?.let {
            val orderCollection = firestore.collection("users").document(it).collection("order_history")
            orderCollection.get()
                .addOnSuccessListener { result ->
                    val orders = result.toObjects(OrderHistory::class.java)
                    onSuccess(orders)
                }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }

    fun getFavoriteFoods(onSuccess: (List<FavoriteFood>) -> Unit, onFailure: (Exception) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        uid?.let {
            val favoritesCollection = firestore.collection("users").document(it).collection("favorites")
            favoritesCollection.get()
                .addOnSuccessListener { result ->
                    val favorites = result.toObjects(FavoriteFood::class.java)
                    onSuccess(favorites)
                }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }

    fun removeFavoriteFood(yemek_id: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        uid?.let {
            val favoritesCollection = firestore.collection("users").document(it).collection("favorites")
            favoritesCollection.document(yemek_id).delete()
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }







}
