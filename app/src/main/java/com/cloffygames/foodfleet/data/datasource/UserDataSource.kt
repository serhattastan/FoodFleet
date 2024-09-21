package com.cloffygames.foodfleet.data.datasource

import com.cloffygames.foodfleet.data.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

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

    // Kullanıcı bilgilerini güncellemek için bir metod
    fun updateUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val uid = firebaseAuth.currentUser?.uid
        uid?.let {
            val userDocument: DocumentReference = firestore.collection("users").document(it)
            userDocument.update(
                mapOf(
                    "user_name" to user.user_name,
                    "user_surname" to user.user_surname,
                    "user_address" to user.user_address,
                )
            ).addOnSuccessListener { onSuccess() }
                .addOnFailureListener { e -> onFailure(e) }
        }
    }
}
