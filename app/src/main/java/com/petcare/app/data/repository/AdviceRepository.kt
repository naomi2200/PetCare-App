package com.petcare.app.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.petcare.app.data.remote.dto.AdviceDto
import kotlinx.coroutines.tasks.await
import kotlin.random.Random

class AdviceRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    suspend fun getAdvice(): List<AdviceDto> {
        val snapshot = firestore
            .collection("advice")
            .get()
            .await()

        return snapshot.documents.map { document ->
            AdviceDto(
                id = document.id,
                title = document.getString("title") ?: "",
                description = document.getString("description") ?: "",
                category = document.getString("category") ?: "",
                species = document.getString("species") ?: "General"
            )
        }
    }

    suspend fun getAdviceBySpecies(species: String): List<AdviceDto> {
        val snapshot = firestore
            .collection("advice")
            .whereIn("species", listOf(species, "General"))
            .get()
            .await()

        return snapshot.documents.map { document ->
            AdviceDto(
                id = document.id,
                title = document.getString("title") ?: "",
                description = document.getString("description") ?: "",
                category = document.getString("category") ?: "",
                species = document.getString("species") ?: "General"
            )
        }
    }

    suspend fun getRandomAdvice(): AdviceDto? {
        val adviceList = getAdvice()

        if (adviceList.isEmpty()) return null

        return adviceList[Random.nextInt(adviceList.size)]
    }

    suspend fun getRandomAdviceBySpecies(species: String): AdviceDto? {
        val adviceList = getAdviceBySpecies(species)

        if (adviceList.isEmpty()) return null

        return adviceList[Random.nextInt(adviceList.size)]
    }
}