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
fun VaccineFormScreen(
    petId: Int,
    existingRecord: MedicalRecordEntity? = null,
    onBack: () -> Unit,
    onSave: (MedicalRecordEntity) -> Unit
) {
    val isEditing = existingRecord != null
    
    var name by remember { mutableStateOf(existingRecord?.title ?: "") }
    var date by remember { mutableStateOf(existingRecord?.date ?: "") }
    
    // Extraer datos de la descripción estructurada de forma segura
    val initialNextDose = remember(existingRecord) {
        existingRecord?.description?.split(". ")?.firstOrNull { it.startsWith("Próxima dosis:") }?.removePrefix("Próxima dosis: ") ?: ""
    }
    val initialObs = remember(existingRecord) {
        existingRecord?.description?.split(". ")?.lastOrNull { !it.startsWith("Próxima dosis:") } ?: ""
    }
    
    var nextDose by remember { mutableStateOf(initialNextDose) }
    var observations by remember { mutableStateOf(if (isEditing) initialObs else "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = if (isEditing) "Editar vacuna" else "Agregar vacuna", 
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
                    .background(Color(0xFFF0EFFF), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Vaccines,
                    contentDescription = null,
                    tint = PrimaryPurple,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            VaccineFormField("Nombre de la vacuna", name, { name = it }, "Ej. Vacuna Óctuple")
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Fecha de aplicación", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
                PetCareDatePickerField(value = date, onValueChange = { date = it }, placeholder = "Seleccionar fecha")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Próxima dosis (Opcional)", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 8.dp))
                PetCareDatePickerField(value = nextDose, onValueChange = { nextDose = it }, placeholder = "Seleccionar fecha")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            VaccineFormField("Observaciones", observations, { observations = it }, "Notas opcionales", isSingleLine = false, minLines = 4)

            Spacer(modifier = Modifier.height(40.dp))

            PetCareButton(
                text = if (isEditing) "Actualizar vacuna" else "Guardar vacuna",
                onClick = {
                    if (name.isNotBlank() && date.isNotBlank()) {
                        val structuredDescription = "Próxima dosis: $nextDose. $observations"
                        val record = existingRecord?.copy(
                            title = name.trim(),
                            description = structuredDescription,
                            date = date
                        ) ?: MedicalRecordEntity(
                            petId = petId,
                            title = name.trim(),
                            description = structuredDescription,
                            recordType = "Vacuna",
                            date = date
                        )
                        onSave(record)
                    }
                },
                containerColor = PrimaryPurple,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun VaccineFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
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
                focusedBorderColor = PrimaryPurple,
                unfocusedBorderColor = Border,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )
    }
}