package com.petcare.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetCareDatePickerField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Seleccionar fecha"
) {
    var showDatePicker by remember { mutableStateOf(false) }

    // Estado del DatePicker (Material 3)
    val datePickerState = rememberDatePickerState()

    // Formateador de fecha solicitado: dd/MM/yyyy
    val formatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }

    // Diálogo del DatePicker
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // Convertir milisegundos a fecha formateada
                            val date = Date(millis)
                            // Ajuste de zona horaria para evitar desfases de un día
                            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                            calendar.time = date
                            onValueChange(formatter.format(calendar.time))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar", color = PrimaryPurple, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = PrimaryPurple,
                    todayContentColor = PrimaryPurple,
                    todayDateBorderColor = PrimaryPurple
                )
            )
        }
    }

    // Campo de texto (Solo lectura)
    OutlinedTextField(
        value = value,
        onValueChange = { },
        readOnly = true,
        placeholder = { Text(placeholder, color = TextSecondary, fontSize = 15.sp) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDatePicker = true },
        enabled = false, // Evita que aparezca el teclado
        shape = RoundedCornerShape(12.dp),
        trailingIcon = {
            Icon(
                imageVector = Icons.Outlined.CalendarToday,
                contentDescription = "Calendario",
                tint = PrimaryPurple
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = TextPrimary,
            disabledBorderColor = Border,
            disabledPlaceholderColor = TextSecondary,
            disabledTrailingIconColor = PrimaryPurple,
            disabledContainerColor = Color.Transparent
        )
    )
}