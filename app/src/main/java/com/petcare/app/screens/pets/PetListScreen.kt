package com.petcare.app.screens.pets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petcare.app.data.local.entity.PetEntity

@Composable
fun PetListScreen(
    pets: List<PetEntity>,
    onAddPetClick: () -> Unit,
    onPetClick: (Int) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPetClick) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Mis mascotas",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (pets.isEmpty()) {
                Text(
                    text = "Aún no tienes mascotas registradas.",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pets) { pet ->
                        PetCard(
                            pet = pet,
                            onClick = { onPetClick(pet.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PetCard(
    pet: PetEntity,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = pet.nombre,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "${pet.especie} • ${pet.raza ?: "Sin raza"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Edad: ${pet.edad} años | Peso: ${pet.peso} kg",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}