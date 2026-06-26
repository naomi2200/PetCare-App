package com.petcare.app.screens.medical

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalHistoryScreen(
    onAddClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Vacunas", "Desparasitaciones", "Consultas")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Historial Médico", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = { }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Background)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAddClick(tabs[selectedTab]) },
                containerColor = when (selectedTab) {
                    1 -> Color(0xFF4CAF50)
                    2 -> PrimaryPink
                    else -> PrimaryPurple
                },
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        },
        containerColor = Background
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Tabs Custom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    val tabColor = when (index) {
                        1 -> Color(0xFF4CAF50)
                        2 -> PrimaryPink
                        else -> PrimaryPurple
                    }

                    Button(
                        onClick = { selectedTab = index },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) tabColor else Color(0xFFE8E8F3),
                            contentColor = if (isSelected) Color.White else TextSecondary
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            // Listado según Tab
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(getMockData(selectedTab)) { item ->
                    MedicalItemCard(item)
                }
            }
        }
    }
}

@Composable
fun MedicalItemCard(item: MedicalRecord) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(2.dp, RoundedCornerShape(24.dp)),
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
                    .background(item.color.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(item.icon, contentDescription = null, tint = item.color, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextPrimary)
                Text(text = item.date, fontSize = 14.sp, color = TextSecondary)
                Text(text = item.description, fontSize = 14.sp, color = TextSecondary)
            }
            if (item.status != null) {
                Badge(
                    containerColor = item.statusColor.copy(alpha = 0.1f),
                    contentColor = item.statusColor,
                    text = item.status
                )
            }
        }
    }
}

@Composable
fun Badge(containerColor: Color, contentColor: Color, text: String) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Modelos y Datos Mock
data class MedicalRecord(
    val title: String,
    val date: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val status: String? = null,
    val statusColor: Color = Color.Gray
)

private fun getMockData(tab: Int) = when (tab) {
    0 -> listOf(
        MedicalRecord("Vacuna Óctuple", "20/05/2024", "Aplicación anual", Icons.Outlined.Vaccines, PrimaryPurple, "Vigente", Color(0xFF4CAF50)),
        MedicalRecord("Vacuna Antirrábica", "20/05/2024", "Aplicación anual", Icons.Outlined.Vaccines, PrimaryPurple, "Vigente", Color(0xFF4CAF50))
    )
    1 -> listOf(
        MedicalRecord("Desparasitación Interna", "15/05/2024", "Pastilla", Icons.Outlined.HealthAndSafety, Color(0xFF4CAF50), "Vigente", Color(0xFF4CAF50)),
        MedicalRecord("Desparasitación Externa", "15/05/2024", "Pipeta", Icons.Outlined.HealthAndSafety, Color(0xFF4CAF50), "Pendiente", Color(0xFFFF9800))
    )
    else -> listOf(
        MedicalRecord("Control Veterinario", "10/03/2024", "Chequeo general", Icons.Outlined.MedicalServices, PrimaryPink)
    )
}