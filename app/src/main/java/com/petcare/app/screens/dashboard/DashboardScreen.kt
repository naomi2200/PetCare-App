package com.petcare.app.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.ui.theme.*

@Composable
fun DashboardScreen() {
    Scaffold(
        bottomBar = { DashboardBottomNavigation() },
        containerColor = Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item { DashboardTopBar() }
            item { WelcomeSection() }
            item { SummaryCardsSection() }
            item { AdviceCardSection() }
            item { UpcomingEventsSection() }
            item { QuickActionsSection() }
        }
    }
}

@Composable
private fun DashboardTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
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
                    contentDescription = "Notificaciones",
                    tint = TextPrimary,
                    modifier = Modifier.size(28.dp)
                )
            }
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(Color(0xFFFF4D4D), CircleShape)
                    .align(Alignment.TopEnd)
                    .offset(x = (-2).dp, y = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "3",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun WelcomeSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "¡Hola Adriana!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(text = " 👋", fontSize = 26.sp)
            }
            Text(
                text = "¿Qué tal está Milo hoy?",
                fontSize = 16.sp,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
        }
        
        Box(contentAlignment = Alignment.Center) {
            // Brillo de fondo
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(PrimaryPurple.copy(alpha = 0.12f), Color.Transparent)
                        )
                    )
            )
            Image(
                painter = painterResource(id = R.drawable.pet_dashboard),
                contentDescription = "Milo",
                modifier = Modifier.size(140.dp),
                contentScale = ContentScale.Fit
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .shadow(6.dp, CircleShape)
                    .background(Color.White, CircleShape)
                    .align(Alignment.TopEnd)
                    .offset(x = 12.dp, y = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = null,
                    tint = PrimaryPink,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun SummaryCardsSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Pets,
                value = "3",
                label = "Mascotas registradas",
                actionText = "Ver todas",
                iconBgColor = Color(0xFFF3EFFF),
                iconTint = PrimaryPurple
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.MedicalServices,
                value = "2",
                label = "Vacunas pendientes",
                actionText = "Ver detalle",
                iconBgColor = Color(0xFFFFF0F3),
                iconTint = PrimaryPink
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SummaryCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.CalendarMonth,
                value = "5",
                label = "Recordatorios pendientes",
                actionText = "Ver recordatorios",
                iconBgColor = Color(0xFFFFF9F0),
                iconTint = Color(0xFFFFA000)
            )
            SummaryCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.MonitorWeight,
                value = "32",
                unit = " kg",
                label = "Último peso registrado",
                actionText = "Ver historial",
                iconBgColor = Color(0xFFF0FFF4),
                iconTint = Color(0xFF2E7D32)
            )
        }
    }
}

@Composable
private fun SummaryCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    value: String,
    unit: String = "",
    label: String,
    actionText: String,
    iconBgColor: Color,
    iconTint: Color
) {
    Card(
        modifier = modifier.shadow(2.dp, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(iconBgColor, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(text = value, fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                    if (unit.isNotEmpty()) {
                        Text(text = unit, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary, modifier = Modifier.padding(bottom = 2.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = label, fontSize = 14.sp, color = TextSecondary, lineHeight = 18.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = actionText, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = iconTint)
        }
    }
}

@Composable
private fun AdviceCardSection() {
    Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(1.dp, RoundedCornerShape(24.dp)),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F3FF)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(modifier = Modifier.padding(24.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.Pets, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Consejo del día", fontWeight = FontWeight.Bold, color = PrimaryPurple, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "“Los perros necesitan al menos 60 minutos de ejercicio diario para mantenerse saludables y felices.”",
                        fontSize = 14.sp,
                        color = TextPrimary,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Outlined.CloudQueue, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Obtenido desde la API", fontSize = 11.sp, color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(44.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        Text(text = "Ver más consejos", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
                // Espacio para la mascota que sobresale
                Spacer(modifier = Modifier.width(100.dp))
            }
        }
        Image(
            painter = painterResource(id = R.drawable.pet2_dashboard),
            contentDescription = null,
            modifier = Modifier
                .size(130.dp)
                .align(Alignment.BottomEnd)
                .offset(x = 0.dp, y = 15.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun UpcomingEventsSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Próximos eventos", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Text(text = "Ver todos", color = PrimaryPurple, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        EventItem(Icons.Outlined.MedicalServices, "Vacuna Milo", "20 de mayo, 2024", "2 días", Color(0xFFF3EFFF), PrimaryPurple)
        EventItem(Icons.Outlined.MonitorWeight, "Registrar peso de Luna", "22 de mayo, 2024", "4 días", Color(0xFFF0FFF4), Color(0xFF2E7D32))
        EventItem(Icons.Outlined.Shield, "Desparasitación Coco", "25 de mayo, 2024", "7 días", Color(0xFFFFF9F0), Color(0xFFFFA000))
    }
}

@Composable
private fun EventItem(icon: ImageVector, title: String, date: String, days: String, iconBg: Color, iconTint: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(46.dp).background(iconBg, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextPrimary)
            Text(text = date, fontSize = 13.sp, color = TextSecondary)
        }
        // Badge verde universal para todos los eventos según la imagen
        Box(
            modifier = Modifier.background(Color(0xFFE8F5E9), RoundedCornerShape(8.dp)).padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Text(text = days, color = Color(0xFF2E7D32), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun QuickActionsSection() {
    Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp)) {
        Text(text = "Acciones rápidas", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            QuickActionItem(Icons.Filled.Add, "Nueva\nmascota", PrimaryPurple)
            QuickActionItem(Icons.Outlined.MedicalServices, "Historial\nmédico", PrimaryPink)
            QuickActionItem(Icons.Outlined.Notifications, "Recordatorios", Color(0xFFFFA000))
            QuickActionItem(Icons.Outlined.MonitorWeight, "Seguimiento\nde peso", SuccessMint)
        }
    }
}

@Composable
private fun QuickActionItem(icon: ImageVector, label: String, tint: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(80.dp)) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .shadow(4.dp, RoundedCornerShape(20.dp), spotColor = Color.Black.copy(alpha = 0.1f))
                .background(Surface, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(26.dp))
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = label, fontSize = 12.sp, color = TextPrimary, textAlign = TextAlign.Center, lineHeight = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun DashboardBottomNavigation() {
    NavigationBar(
        containerColor = Surface,
        tonalElevation = 10.dp,
        modifier = Modifier.height(80.dp)
    ) {
        val items = listOf(
            Triple("Inicio", Icons.Outlined.Home, true),
            Triple("Mascotas", Icons.Outlined.Pets, false),
            Triple("Recordatorios", Icons.Outlined.CalendarMonth, false),
            Triple("Perfil", Icons.Outlined.PersonOutline, false)
        )
        items.forEach { (label, icon, selected) ->
            NavigationBarItem(
                icon = { Icon(icon, contentDescription = null, modifier = Modifier.size(26.dp)) },
                label = { Text(label, fontSize = 11.sp, fontWeight = FontWeight.Medium) },
                selected = selected,
                onClick = { },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryPurple,
                    selectedTextColor = PrimaryPurple,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}