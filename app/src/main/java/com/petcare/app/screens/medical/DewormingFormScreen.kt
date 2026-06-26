package com.petcare.app.screens.medical

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.components.PetCareButton
import com.petcare.app.components.PetCareDatePickerField
import com.petcare.app.data.local.entity.MedicalRecordEntity
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DewormingFormScreen(
    petId: Int,
    existingRecord: MedicalRecordEntity? = null,
    onBack: () -> Unit,
    onSave: (MedicalRecordEntity) -> Unit
) {
    val isEditing = existingRecord != null
    val dewormingColor = Color(0xFF4CAF50)
    
    // Extraer tipo y producto del título ("Tipo - Producto")
    val titleParts = existingRecord?.title?.split(" - ")
    var type by remember { mutableStateOf(titleParts?.firstOrNull() ?: "") }
    var product by remember { mutableStateOf(titleParts?.getOrNull(1) ?: "") }
    var date by remember { mutableStateOf(existingRecord?.date ?: "") }
    
    // Extraer próxima aplicación de la descripción
    val initialNextApp = existingRecord?.description?.split(". ")?.firstOrNull { it.startsWith("Próxima aplicación:") }?.removePrefix("Próxima aplicación: ") ?: ""
    val initialObs = existingRecord?.description?.split(". ")?.lastOrNull { !it.startsWith("Próxima aplicación:") } ?: ""
    
    var nextApp by remember { mutableStateOf(initialNextApp) }
    var observations by remember { mutableStateOf(if (isEditing) initialObs else "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = if (isEditing) "Editar desparasitación" else "Nueva desparasitación", 
                        fontWeight = FontWeight.Bold, 
                        fontSize = 18.sp, 
                        color = TextPrimary
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás", tint = TextPrimary)
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
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color(0xFFE8F5E9), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shield,
                    contentDescription = null,
                    tint = dewormingColor,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            MedicalFormField("Tipo (Interna / Externa)", type, { type = it }, "Ej. Interna", dewormingColor)
            Spacer(modifier = Modifier.height(20.dp))
            MedicalFormField("Producto", product, { product = it }, "Nombre del producto", dewormingColor)
            
            Spacer(modifier = Modifier.height(20.dp))
            Text("Fecha de aplicación", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp))
            PetCareDatePickerField(value = date, onValueChange = { date = it }, placeholder = "Seleccionar fecha")
            
            Spacer(modifier = Modifier.height(20.dp))
            Text("Próxima aplicación (Opcional)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp))
            PetCareDatePickerField(value = nextApp, onValueChange = { nextApp = it }, placeholder = "Seleccionar fecha")
            
            Spacer(modifier = Modifier.height(20.dp))
            MedicalFormField("Observaciones", observations, { observations = it }, "Notas adicionales", dewormingColor, isSingleLine = false, minLines = 4)

            Spacer(modifier = Modifier.height(40.dp))

            PetCareButton(
                text = if (isEditing) "Actualizar registro" else "Guardar registro",
                onClick = {
                    if (type.isNotBlank() && date.isNotBlank()) {
                        val record = existingRecord?.copy(
                            title = "$type - $product",
                            description = "Próxima aplicación: $nextApp. $observations",
                            date = date
                        ) ?: MedicalRecordEntity(
                            petId = petId,
                            title = "$type - $product",
                            description = "Próxima aplicación: $nextApp. $observations",
                            recordType = "Desparasitación",
                            date = date
                        )
                        onSave(record)
                    }
                },
                containerColor = dewormingColor,
                modifier = Modifier.fillMaxWidth()
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
    accentColor: Color,
    isSingleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(placeholder, color = TextSecondary, fontSize = 15.sp) },
            singleLine = isSingleLine,
            minLines = minLines,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentColor,
                unfocusedBorderColor = Border,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}