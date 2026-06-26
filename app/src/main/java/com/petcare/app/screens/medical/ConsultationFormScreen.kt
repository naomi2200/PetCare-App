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
fun ConsultationFormScreen(
    petId: Int,
    existingRecord: MedicalRecordEntity? = null,
    onBack: () -> Unit,
    onSave: (MedicalRecordEntity) -> Unit
) {
    val isEditing = existingRecord != null
    
    // Al editar, intentamos parsear la descripción estructurada
    val initialDescription = existingRecord?.description ?: ""
    val parts = initialDescription.split("\n")
    val initialDiagnosis = parts.find { it.startsWith("Dx: ") }?.removePrefix("Dx: ") ?: initialDescription
    val initialTreatment = parts.find { it.startsWith("Tratamiento: ") }?.removePrefix("Tratamiento: ") ?: ""
    val initialObs = parts.find { it.startsWith("Obs: ") }?.removePrefix("Obs: ") ?: ""

    var reason by remember { mutableStateOf(existingRecord?.title ?: "") }
    var vet by remember { mutableStateOf(existingRecord?.veterinarian ?: "") }
    var diagnosis by remember { mutableStateOf(initialDiagnosis) }
    var treatment by remember { mutableStateOf(initialTreatment) }
    var date by remember { mutableStateOf(existingRecord?.date ?: "") }
    var observations by remember { mutableStateOf(initialObs) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = if (isEditing) "Editar consulta" else "Nueva consulta", 
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
                    .background(Color(0xFFFFEEF0), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.MedicalServices,
                    contentDescription = null,
                    tint = PrimaryPink,
                    modifier = Modifier.size(50.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            MedicalFormField("Motivo de la consulta", reason, { reason = it }, "Ej: Control anual", PrimaryPink)
            Spacer(modifier = Modifier.height(20.dp))
            MedicalFormField("Veterinario", vet, { vet = it }, "Nombre del veterinario", PrimaryPink)
            Spacer(modifier = Modifier.height(20.dp))
            MedicalFormField("Diagnóstico", diagnosis, { diagnosis = it }, "Resultado de la revisión", PrimaryPink)
            Spacer(modifier = Modifier.height(20.dp))
            MedicalFormField("Tratamiento", treatment, { treatment = it }, "Medicamentos o pasos a seguir", PrimaryPink)
            
            Spacer(modifier = Modifier.height(20.dp))
            Text("Fecha", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp))
            PetCareDatePickerField(value = date, onValueChange = { date = it }, placeholder = "Seleccionar fecha")
            
            Spacer(modifier = Modifier.height(20.dp))
            MedicalFormField("Observaciones", observations, { observations = it }, "Notas adicionales", PrimaryPink, isSingleLine = false, minLines = 3)

            Spacer(modifier = Modifier.height(40.dp))

            PetCareButton(
                text = if (isEditing) "Actualizar consulta" else "Guardar consulta",
                onClick = {
                    if (reason.isNotBlank() && date.isNotBlank()) {
                        val structuredDesc = "Dx: $diagnosis\nTratamiento: $treatment\nObs: $observations"
                        val record = existingRecord?.copy(
                            title = reason,
                            veterinarian = vet.ifBlank { null },
                            description = structuredDesc,
                            date = date
                        ) ?: MedicalRecordEntity(
                            petId = petId,
                            title = reason,
                            veterinarian = vet.ifBlank { null },
                            description = structuredDesc,
                            recordType = "Consulta",
                            date = date
                        )
                        onSave(record)
                    }
                },
                containerColor = PrimaryPink,
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