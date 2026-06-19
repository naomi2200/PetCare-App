package com.petcare.app.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun register(
        name: String,
        email: String,
        password: String
    ) {
        val result = auth
            .createUserWithEmailAndPassword(email, password)
            .await()

        val uid = result.user?.uid
            ?: throw Exception("No se pudo obtener UID")

        val userData = hashMapOf(
            "uid" to uid,
            "name" to name,
            "email" to email,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("users")
            .document(uid)
            .set(userData)
            .await()
    }

    suspend fun login(
        email: String,
        password: String
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .await()
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}