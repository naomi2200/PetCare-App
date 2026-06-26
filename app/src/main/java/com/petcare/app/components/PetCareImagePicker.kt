package com.petcare.app.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.petcare.app.ui.theme.PrimaryPurple

@Composable
fun PetCareImagePicker(
    imageUri: Uri?,
    onImageSelected: (Uri?) -> Unit,
    size: Dp = 120.dp
) {
    // Lanzador para abrir la galería de fotos
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        onImageSelected(uri)
    }

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White)
            .clickable {
                // Abre el selector de medios (solo imágenes)
                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            // Si hay imagen seleccionada, la muestra con Coil
            AsyncImage(
                model = imageUri,
                contentDescription = "Imagen seleccionada",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Si no hay imagen, muestra el icono de "Añadir foto"
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AddAPhoto,
                    contentDescription = null,
                    tint = PrimaryPurple.copy(alpha = 0.6f),
                    modifier = Modifier.size(size / 3)
                )
            }
        }

        // Indicador visual pequeño de "+" en la esquina
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(size / 4)
                .background(PrimaryPurple, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.AddAPhoto, // O un simple Add
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(size / 8)
            )
        }
    }
}