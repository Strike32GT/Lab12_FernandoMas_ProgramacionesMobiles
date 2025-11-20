package com.mas.lab12_fernandomas.firebase


import com.google.firebase.firestore.FirebaseFirestore
import com.mas.lab12_fernandomas.models.User
import kotlinx.coroutines.tasks.await


class UserRepository(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val users = db.collection("users")

    suspend fun addUser(user: User, uid: String) {
        users.document(uid).set(user).await()
    }

    suspend fun getUser(uid: String): User? {
        val snapshot = users.document(uid).get().await()
        return snapshot.toObject(User::class.java)
    }

    suspend fun updateUser(user: User, uid: String) {
        users.document(uid).set(user).await()
    }

    suspend fun deleteUser(uid: String){
        users.document(uid).delete().await()
    }
}