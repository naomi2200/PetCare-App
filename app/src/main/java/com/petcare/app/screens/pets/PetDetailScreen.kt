package com.petcare.app.screens.pets

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.petcare.app.data.local.entity.PetEntity

@Composable
fun PetDetailScreen(
    pet: PetEntity?,
    onBackClick: () -> Unit,
    onDeleteClick: (PetEntity) -> Unit
) {
    Scaffold(
        topBar = {
            Text(
                text = "Detalle de mascota",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { padding ->
        if (pet == null) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Text("No se encontró la mascota.")
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(onClick = onBackClick) {
                    Text("Volver")
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = pet.nombre,
                    style = MaterialTheme.typography.headlineMedium
                )

                Text("Especie: ${pet.especie}")
                Text("Raza: ${pet.raza ?: "Sin raza"}")
                Text("Sexo: ${pet.sexo}")
                Text("Edad: ${pet.edad} años")
                Text("Peso: ${pet.peso} kg")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onBackClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }

                OutlinedButton(
                    onClick = { onDeleteClick(pet) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar mascota")
                }
            }
        }
    }
}