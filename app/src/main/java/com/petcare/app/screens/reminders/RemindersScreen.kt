package com.petcare.app.screens.reminders

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material.icons.outlined.Shield
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
import com.petcare.app.screens.pets.BottomNavigationBar
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    onBackClick: () -> Unit = {},
    onAddReminderClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Próximos", "Todos", "Completados")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Recordatorios", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Background)
            )
        },
        bottomBar = { BottomNavigationBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddReminderClick,
                containerColor = PrimaryPurple,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Recordatorio")
            }
        },
        containerColor = Background
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Tabs Estilo Imagen
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Button(
                        onClick = { selectedTab = index },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) PrimaryPurple else Color(0xFFF0F0F8),
                            contentColor = if (isSelected) Color.White else TextSecondary
                        ),
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(
                    start = 24.dp,
                    top = 0.dp,
                    end = 24.dp,
                    bottom = 32.dp
                ),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(getMockReminders()) { reminder ->
                    ReminderItemCard(reminder)
                }
            }

        }
    }
}

@Composable
private fun ReminderItemCard(reminder: ReminderData) {
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
            // Icono con fondo pastel
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(reminder.color.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = reminder.icon,
                    contentDescription = null,
                    tint = reminder.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${reminder.title} - ${reminder.petName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = reminder.date,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            // Badge de tiempo restante
            Surface(
                color = reminder.statusBgColor,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = reminder.timeLeft,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    color = reminder.statusTextColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Datos Mock para visualización
private data class ReminderData(
    val title: String,
    val petName: String,
    val date: String,
    val timeLeft: String,
    val icon: ImageVector,
    val color: Color,
    val statusBgColor: Color,
    val statusTextColor: Color
)

private fun getMockReminders() = listOf(
    ReminderData(
        "Vacuna", "Milo", "20 de mayo, 2024", "Mañana",
        Icons.Outlined.Vaccines, PrimaryPurple, Color(0xFFFFF4E5), Color(0xFFFF9800)
    ),
    ReminderData(
        "Desparasitación", "Luna", "25 de mayo, 2024", "5 días",
        Icons.Outlined.Shield, Color(0xFFFF9800), Color(0xFFE3F2FD), Color(0xFF2196F3)
    ),
    ReminderData(
        "Control veterinario", "Milo", "10 de junio, 2024", "21 días",
        Icons.Outlined.Assignment, Color(0xFF4CAF50), Color(0xFFE8F5E9), Color(0xFF4CAF50)
    ),
    ReminderData(
        "Registrar peso", "Coco", "15 de junio, 2024", "28 días",
        Icons.Outlined.MonitorWeight, Color(0xFFFFC107), Color(0xFFE8F5E9), Color(0xFF4CAF50)
    )
)