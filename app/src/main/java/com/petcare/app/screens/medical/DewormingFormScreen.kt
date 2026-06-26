package com.petcare.app.screens.medical

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.components.PetCareButton
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DewormingFormScreen(
    isEdit: Boolean = false,
    onBack: () -> Unit = {}
) {
    val dewormingColor = Color(0xFF4CAF50)
    var type by remember { mutableStateOf("") }
    var product by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var nextApp by remember { mutableStateOf("") }
    var observations by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (isEdit) "Editar desparasitación" else "Agregar desparasitación", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null, tint = TextPrimary)
                    }
                },
                actions = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.Close, contentDescription = null, tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono Central
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE8F5E9), RoundedCornerShape(24.dp)), // Verde muy suave
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = dewormingColor,
                    modifier = Modifier.size(50.dp)
                )
                // Pequeña huella superpuesta para dar el toque de la imagen
                Icon(
                    imageVector = Icons.Outlined.Pets,
                    contentDescription = null,
                    tint = dewormingColor,
                    modifier = Modifier.size(20.dp).offset(y = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Formulario
            MedicalFormField(
                label = "Tipo de desparasitación",
                value = type,
                onValueChange = { type = it },
                placeholder = "Selecciona el tipo",
                trailingIcon = Icons.Outlined.KeyboardArrowDown
            )

            Spacer(modifier = Modifier.height(20.dp))

            MedicalFormField(
                label = "Producto",
                value = product,
                onValueChange = { product = it },
                placeholder = "Ej. Pastilla / Pipeta"
            )

            Spacer(modifier = Modifier.height(20.dp))

            MedicalFormField(
                label = "Fecha de aplicación",
                value = date,
                onValueChange = { date = it },
                placeholder = "Selecciona la fecha",
                trailingIcon = Icons.Outlined.CalendarMonth
            )

            Spacer(modifier = Modifier.height(20.dp))

            MedicalFormField(
                label = "Próxima aplicación",
                value = nextApp,
                onValueChange = { nextApp = it },
                placeholder = "Selecciona la fecha",
                trailingIcon = Icons.Outlined.CalendarMonth
            )

            Spacer(modifier = Modifier.height(20.dp))

            MedicalFormField(
                label = "Observaciones",
                value = observations,
                onValueChange = { observations = it },
                placeholder = "Escribe observaciones (opcional)",
                isSingleLine = false,
                minLines = 4
            )

            Spacer(modifier = Modifier.height(40.dp))

            PetCareButton(
                text = if (isEdit) "Guardar cambios" else "Guardar desparasitación",
                onClick = { },
                containerColor = dewormingColor
            )
        }
    }
}

@Composable
private fun MedicalFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    trailingIcon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    isSingleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = TextSecondary, fontSize = 15.sp) },
            trailingIcon = trailingIcon?.let {
                { Icon(it, contentDescription = null, tint = TextSecondary) }
            },
            singleLine = isSingleLine,
            minLines = minLines,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4CAF50),
                unfocusedBorderColor = Border,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary
            )
        )
    }
}