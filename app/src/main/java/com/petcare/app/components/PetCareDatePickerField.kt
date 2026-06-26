package com.petcare.app.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
    label: String = "",
    placeholder: String = "Seleccionar fecha"
) {
    var showDatePicker by remember { mutableStateOf(false) }
    
    // Configuramos el formateador para que trabaje SIEMPRE en UTC
    // Esto evita que el desfase horario cambie el día seleccionado.
    val formatter = remember { 
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    val initialDateMillis = remember(value) {
        try {
            if (value.isNotEmpty()) {
                formatter.parse(value)?.time
            } else null
        } catch (e: Exception) {
            null
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            // El DatePicker de Material 3 devuelve milisegundos en UTC (00:00:00)
                            val date = Date(millis)
                            onValueChange(formatter.format(date))
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

    Column {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
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
}