package com.petcare.app.screens.reminders

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
import com.petcare.app.data.local.entity.ReminderEntity
import com.petcare.app.screens.pets.BottomNavigationBar
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemindersScreen(
    reminders: List<ReminderEntity>,
    onBackClick: () -> Unit = {},
    onAddReminderClick: () -> Unit = {},
    onReminderClick: (ReminderEntity) -> Unit = {},
    onDeleteReminder: (ReminderEntity) -> Unit = {},
    onMarkAsCompleted: (ReminderEntity) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onPetsClick: () -> Unit = {},
    onRemindersClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Próximos", "Todos", "Completados")
    
    // Estado para el diálogo de eliminación
    var reminderToDelete by remember { mutableStateOf<ReminderEntity?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    val filteredReminders = remember(selectedTab, reminders) {
        when (selectedTab) {
            0 -> reminders.filter { !it.isCompleted }
            2 -> reminders.filter { it.isCompleted }
            else -> reminders
        }
    }

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
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = onHomeClick,
                onPetsClick = onPetsClick,
                onRemindersClick = onRemindersClick,
                onProfileClick = onProfileClick
            )
        },
        floatingActionButton = {
            if (selectedTab != 2) {
                FloatingActionButton(
                    onClick = onAddReminderClick,
                    containerColor = PrimaryPurple,
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Recordatorio")
                }
            }
        },
        containerColor = Background
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTab == index
                        val containerColor = if (isSelected) PrimaryPurple else Color(0xFFF0F0F8)
                        val contentColor = if (isSelected) Color.White else TextSecondary

                        Button(
                            onClick = { selectedTab = index },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = containerColor,
                                contentColor = contentColor
                            ),
                            shape = RoundedCornerShape(24.dp),
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Text(text = title, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }

                if (filteredReminders.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay recordatorios.", color = TextSecondary)
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(start = 24.dp, end = 24.dp, bottom = 32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(filteredReminders, key = { it.id }) { reminder ->
                            ReminderItemCard(
                                reminder = reminder,
                                onClick = { onReminderClick(reminder) },
                                onDelete = { 
                                    reminderToDelete = reminder
                                    showDeleteDialog = true 
                                },
                                onComplete = { onMarkAsCompleted(reminder) }
                            )
                        }
                    }
                }
            }

            // Diálogo de Confirmación
            PetCareConfirmDialog(
                show = showDeleteDialog,
                title = "Eliminar recordatorio",
                message = "¿Estás seguro de que deseas eliminar este recordatorio? Esta acción no se puede deshacer.",
                onConfirm = {
                    reminderToDelete?.let { onDeleteReminder(it) }
                    showDeleteDialog = false
                    reminderToDelete = null
                },
                onDismiss = {
                    showDeleteDialog = false
                    reminderToDelete = null
                }
            )
        }
    }
}

@Composable
private fun ReminderItemCard(
    reminder: ReminderEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onComplete: () -> Unit
) {
    val (icon, color) = when (reminder.reminderType) {
        "Vacuna" -> Icons.Outlined.Vaccines to PrimaryPurple
        "Desparasitación" -> Icons.Outlined.Shield to Color(0xFFFF9800)
        "Peso" -> Icons.Outlined.MonitorWeight to Color(0xFFFFC107)
        else -> Icons.Outlined.Assignment to Color(0xFF4CAF50)
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
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(color.copy(alpha = 0.1f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(26.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = reminder.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
                Text(
                    text = "${reminder.reminderDate} ${reminder.reminderTime ?: ""}",
                    fontSize = 13.sp,
                    color = TextSecondary
                )
                
                // Texto de anticipación
                Text(
                    text = "Aviso: ${getAnticipationText(reminder.anticipationMinutes)}",
                    fontSize = 12.sp,
                    color = PrimaryPurple,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            if (!reminder.isCompleted) {
                IconButton(onClick = onComplete) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = "Completar", tint = Color.LightGray)
                }
            } else {
                Icon(Icons.Outlined.CheckCircle, contentDescription = "Completado", tint = Color(0xFF4CAF50))
            }
            
            IconButton(onClick = onDelete) {
                Icon(Icons.Outlined.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.6f))
            }
        }
    }
}

private fun getAnticipationText(minutes: Int): String {
    return when (minutes) {
        0 -> "En el momento exacto"
        15 -> "15 minutos antes"
        30 -> "30 minutos antes"
        60 -> "1 hora antes"
        1440 -> "1 día antes"
        else -> "$minutes minutos antes"
    }
}
