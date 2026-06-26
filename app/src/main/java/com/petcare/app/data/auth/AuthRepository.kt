package com.petcare.app.data.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null
)

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun register(
        name: String,
        email: String,
        password: String,
        photoUrl: String? = null
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
            "photoUrl" to photoUrl,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("users")
            .document(uid)
            .set(userData)
            .await()
    }

    suspend fun getUserProfile(): UserProfile? {
        val uid = auth.currentUser?.uid ?: return null
        return try {
            val document = firestore.collection("users").document(uid).get().await()
            UserProfile(
                uid = uid,
                name = document.getString("name") ?: "Usuario",
                email = document.getString("email") ?: "",
                photoUrl = document.getString("photoUrl")
            )
        } catch (e: Exception) {
            null
        }
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