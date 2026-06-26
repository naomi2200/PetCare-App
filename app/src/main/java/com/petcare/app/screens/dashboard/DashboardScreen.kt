package com.petcare.app.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.data.local.entity.PetEntity
import com.petcare.app.data.local.entity.ReminderEntity
import com.petcare.app.data.remote.dto.AdviceDto
import com.petcare.app.screens.pets.BottomNavigationBar
import com.petcare.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun DashboardScreen(
    userName: String,
    pets: List<PetEntity>,
    reminders: List<ReminderEntity>,
    dailyAdvice: AdviceDto?,
    lastWeight: String,
    pendingVaccines: Int,
    onPetListClick: () -> Unit,
    onRemindersClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddPetClick: () -> Unit,
    onAdviceClick: () -> Unit,
    onMedicalClick: () -> Unit,
    onWeightClick: () -> Unit
) {
    Scaffold(
        bottomBar = { 
            BottomNavigationBar(
                onHomeClick = { /* Actual */ },
                onPetsClick = onPetListClick,
                onRemindersClick = onRemindersClick,
                onProfileClick = onProfileClick
            ) 
        },
        containerColor = Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item { DashboardTopBar(reminders.count { !it.isCompleted }) }
            
            val mainPetName = pets.firstOrNull()?.nombre ?: "tu mascota"
            item { WelcomeSection(userName, mainPetName) }
            
            item { 
                StatsGridSection(
                    petCount = pets.size,
                    pendingVaccines = pendingVaccines,
                    pendingReminders = reminders.count { !it.isCompleted },
                    lastWeight = lastWeight,
                    onPetsClick = onPetListClick,
                    onRemindersClick = onRemindersClick,
                    onWeightClick = onWeightClick,
                    onMedicalClick = onMedicalClick
                ) 
            }
            item { AdviceCardSection(dailyAdvice, onAdviceClick) }
            item { UpcomingEventsSection(reminders, onRemindersClick) }
            item { QuickActionsSection(onAddPetClick, onMedicalClick, onRemindersClick, onWeightClick) }
        }
    }
}

@Composable
private fun DashboardTopBar(pendingCount: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_petcare),
            contentDescription = "PetCare Logo",
            modifier = Modifier.height(34.dp),
            contentScale = ContentScale.Fit
        )
        Box {
            IconButton(onClick = { }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            if (pendingCount > 0) {
                Surface(
                    color = Color.Red,
                    shape = CircleShape,
                    modifier = Modifier.size(16.dp).align(Alignment.TopEnd).offset(x = (-4).dp, y = 4.dp)
                ) {
                    Text(pendingCount.toString(), color = Color.White, fontSize = 10.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun WelcomeSection(userName: String, petName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "¡Hola $userName! 👋",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                lineHeight = 34.sp
            )
            Text(
                text = "¿Qué tal está $petName hoy?",
                fontSize = 16.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
        
        Box(
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pet_dashboard),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                contentScale = ContentScale.Fit
            )
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 5.dp, y = 5.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = PrimaryPink,
                    modifier = Modifier.padding(6.dp).size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun StatsGridSection(
    petCount: Int,
    pendingVaccines: Int,
    pendingReminders: Int,
    lastWeight: String,
    onPetsClick: () -> Unit,
    onRemindersClick: () -> Unit,
    onWeightClick: () -> Unit,
    onMedicalClick: () -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(Modifier.weight(1f), Icons.Outlined.Pets, petCount.toString(), "Mascotas registradas", "Ver todas", Color(0xFFF3EFFF), PrimaryPurple, onPetsClick)
            StatCard(Modifier.weight(1f), Icons.Outlined.Vaccines, pendingVaccines.toString(), "Vacunas pendientes", "Ver detalle", Color(0xFFFFF0F3), PrimaryPink, onMedicalClick)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(Modifier.weight(1f), Icons.Outlined.CalendarToday, pendingReminders.toString(), "Recordatorios pendientes", "Ver recordatorios", Color(0xFFFFF9F0), Color(0xFFFFA000), onRemindersClick)
            StatCard(Modifier.weight(1f), Icons.Outlined.MonitorWeight, lastWeight, "Último peso registrado", "Ver historial", Color(0xFFF0FFF4), Color(0xFF2E7D32), onWeightClick)
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier, icon: ImageVector, value: String, label: String, actionText: String, bgColor: Color, tint: Color, onClick: () -> Unit) {
    Card(
        modifier = modifier
            .shadow(1.dp, RoundedCornerShape(24.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(bgColor, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = tint, modifier = Modifier.size(22.dp))
                }
                Text(
                    text = value,
                    fontSize = if (value.length > 5) 18.sp else 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimary
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = label,
                fontSize = 13.sp,
                color = TextSecondary,
                lineHeight = 16.sp,
                minLines = 2,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = actionText,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = tint
            )
        }
    }
}

@Composable
private fun AdviceCardSection(advice: AdviceDto?, onAdviceClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .shadow(1.dp, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F3FF)),
        shape = RoundedCornerShape(24.dp)
    ) {
        Row(modifier = Modifier.padding(24.dp)) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Pets, null, tint = PrimaryPurple, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Consejo del día", fontWeight = FontWeight.Bold, color = PrimaryPurple, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = advice?.description ?: "Cargando consejos útiles para el bienestar de tu mascota...",
                    fontSize = 14.sp,
                    color = TextPrimary,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onAdviceClick,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text("Ver más consejos", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Outlined.ArrowForward, null, modifier = Modifier.size(16.dp))
                }
            }
            Image(
                painter = painterResource(id = R.drawable.pet2_dashboard),
                contentDescription = null,
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.Bottom),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun UpcomingEventsSection(reminders: List<ReminderEntity>, onSeeAll: () -> Unit) {
    // Filtramos para mostrar solo pendientes (isCompleted = false) y que no sean de fechas pasadas
    val upcoming = reminders.filter { 
        val days = calculateDaysRemaining(it.reminderDate)
        !it.isCompleted && days >= 0
    }.sortedBy { 
        // Mejoramos la ordenación parseando la fecha para que sea cronológica real
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        try { sdf.parse(it.reminderDate)?.time ?: 0L } catch(e: Exception) { 0L }
    }.take(3)

    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Próximos eventos", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text("Ver todos", color = PrimaryPurple, fontSize = 13.sp, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onSeeAll() })
        }
        Spacer(modifier = Modifier.height(16.dp))
        
        if (upcoming.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No tienes recordatorios próximos", color = TextSecondary, fontSize = 14.sp)
            }
        } else {
            upcoming.forEach { reminder ->
                EventRow(reminder)
            }
        }
    }
}

@Composable
private fun EventRow(reminder: ReminderEntity) {
    val daysRemaining = calculateDaysRemaining(reminder.reminderDate)
    val badgeText = when {
        daysRemaining == 0L -> "Hoy"
        else -> "$daysRemaining días"
    }
    val badgeColor = if (daysRemaining == 0L) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
    val badgeTextColor = if (daysRemaining == 0L) Color.Red else Color(0xFF2E7D32)

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(46.dp).background(Color(0xFFF3EFFF), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = when(reminder.reminderType) {
                    "Vacuna" -> Icons.Outlined.Vaccines
                    "Peso" -> Icons.Outlined.MonitorWeight
                    else -> Icons.Outlined.Notifications
                },
                contentDescription = null,
                tint = PrimaryPurple,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(reminder.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
            Text(reminder.reminderDate, fontSize = 13.sp, color = TextSecondary)
        }
        Box(modifier = Modifier.background(badgeColor, RoundedCornerShape(8.dp)).padding(horizontal = 10.dp, vertical = 4.dp)) {
            Text(badgeText, color = badgeTextColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

private fun calculateDaysRemaining(dateStr: String): Long {
    return try {
        // Usamos SimpleDateFormat con la localización por defecto (Hora Local)
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val reminderDate = sdf.parse(dateStr) ?: return 0
        
        // Obtenemos "hoy" a las 00:00:00 en hora LOCAL
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time
        
        val diff = reminderDate.time - today.time
        TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    } catch (e: Exception) { 0 }
}

@Composable
private fun QuickActionsSection(onAddPet: () -> Unit, onMedical: () -> Unit, onReminders: () -> Unit, onWeight: () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
        Text("Acciones rápidas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            QuickActionBtn(Icons.Filled.Add, "Nueva\nmascota", PrimaryPurple, onAddPet)
            QuickActionBtn(Icons.Outlined.MedicalServices, "Historial\nmédico", PrimaryPink, onMedical)
            QuickActionBtn(Icons.Outlined.Notifications, "Recordatorios", Color(0xFFFFA000), onReminders)
            QuickActionBtn(Icons.Outlined.MonitorWeight, "Seguimiento\nde peso", SuccessMint, onWeight)
        }
    }
}

@Composable
private fun QuickActionBtn(icon: ImageVector, label: String, tint: Color, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp).clickable { onClick() }) {
        Surface(
            modifier = Modifier.size(64.dp),
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = tint, modifier = Modifier.size(26.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(label, fontSize = 12.sp, color = TextPrimary, textAlign = TextAlign.Center, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}
