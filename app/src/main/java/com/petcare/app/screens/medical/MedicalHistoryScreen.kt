package com.petcare.app.screens.medical

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.components.PetCareConfirmDialog
import com.petcare.app.data.local.entity.MedicalRecordEntity
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalHistoryScreen(
    records: List<MedicalRecordEntity>,
    onBackClick: () -> Unit,
    onAddClick: (String) -> Unit,
    onRecordClick: (MedicalRecordEntity) -> Unit,
    onDeleteRecord: (MedicalRecordEntity) -> Unit // Nuevo callback
) {
    var selectedFilter by remember { mutableStateOf("Todos") }
    val filters = listOf("Todos", "Vacunas", "Desparasitaciones", "Consultas")
    
    var recordToDelete by remember { mutableStateOf<MedicalRecordEntity?>(null) }

    val filteredRecords = remember(selectedFilter, records) {
        if (selectedFilter == "Todos") records
        else records.filter { 
            when (selectedFilter) {
                "Vacunas" -> it.recordType == "Vacuna"
                "Desparasitaciones" -> it.recordType == "Desparasitación"
                "Consultas" -> it.recordType == "Consulta"
                else -> true
            }
        }
    }

    // Diálogo de confirmación
    PetCareConfirmDialog(
        show = recordToDelete != null,
        title = "Eliminar registro",
        message = "¿Estás seguro de que deseas eliminar este registro médico? Esta acción no se puede deshacer.",
        onConfirm = {
            recordToDelete?.let { onDeleteRecord(it) }
            recordToDelete = null
        },
        onDismiss = { recordToDelete = null }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Historial Médico", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Background)
            )
        },
        floatingActionButton = {
            var showMenu by remember { mutableStateOf(false) }
            Box {
                FloatingActionButton(
                    onClick = { showMenu = true },
                    containerColor = PrimaryPurple,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Registro")
                }
                
                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    filters.drop(1).forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = { 
                                onAddClick(type)
                                showMenu = false 
                            }
                        )
                    }
                }
            }
        },
        containerColor = Background
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Filtros
            ScrollableTabRow(
                selectedTabIndex = filters.indexOf(selectedFilter),
                containerColor = Color.Transparent,
                edgePadding = 24.dp,
                divider = {},
                indicator = {}
            ) {
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Tab(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        text = {
                            Surface(
                                color = if (isSelected) PrimaryPurple else Color.White,
                                shape = RoundedCornerShape(20.dp),
                                border = if (isSelected) null else BorderStroke(1.dp, Border)
                            ) {
                                Text(
                                    text = filter,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                    color = if (isSelected) Color.White else TextSecondary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    )
                }
            }

            if (filteredRecords.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay registros médicos.", color = TextSecondary)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(filteredRecords) { record ->
                        MedicalRecordCard(
                            record = record, 
                            onClick = { onRecordClick(record) },
                            onDelete = { recordToDelete = record }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MedicalRecordCard(
    record: MedicalRecordEntity, 
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val (icon, color) = when (record.recordType) {
        "Vacuna" -> Icons.Outlined.Vaccines to PrimaryPurple
        "Desparasitación" -> Icons.Outlined.Shield to Color(0xFF4CAF50)
        else -> Icons.Outlined.MedicalServices to PrimaryPink
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(24.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(color.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(28.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = record.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                Text(text = record.date, fontSize = 14.sp, color = TextSecondary)
                if (!record.veterinarian.isNullOrBlank()) {
                    Text(text = "Vet: ${record.veterinarian}", fontSize = 12.sp, color = PrimaryPurple, fontWeight = FontWeight.Medium)
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete, 
                    contentDescription = "Eliminar", 
                    tint = Color.Red.copy(alpha = 0.4f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}