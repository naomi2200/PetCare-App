package com.petcare.app.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.petcare.app.ui.theme.PrimaryPurple
import com.petcare.app.ui.theme.TextPrimary

@Composable
fun PetCareConfirmDialog(
    show: Boolean,
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title, fontWeight = FontWeight.Bold, color = TextPrimary) },
            text = { Text(text = message, color = TextPrimary) },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("Eliminar", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancelar", color = PrimaryPurple)
                }
            },
            containerColor = Color.White
        )
    }
}