package com.mas.lab12_fernandomas.firebase

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class AuthRepository (
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
){
    suspend fun login(email:String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email,password).await()
            true
        }catch (e: Exception){
            false
        }
    }

    fun logout(){
        auth.signOut()
    }

    fun getCurrentUserId(): String ? {
        return auth.currentUser?.uid
    }
}