package com.petcare.app.screens.pets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.ui.theme.*

@Composable
fun PetListScreen() {
    // 📦 Wrapper para permitir superposición de la mascota sobre el Scaffold/Navbar
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            bottomBar = { BottomNavigationBar() },
            containerColor = Background
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))

                // 🎯 HEADER
                HeaderSection()

                Spacer(modifier = Modifier.height(28.dp))

                // 🎯 LISTADO DE MASCOTAS
                PetCard(
                    name = "Milo",
                    breed = "Golden Retriever",
                    info = "3 años • 32 kg",
                    imageRes = R.drawable.pet_login
                )

                Spacer(modifier = Modifier.height(16.dp))

                PetCard(
                    name = "Luna",
                    breed = "Gato",
                    info = "2 años • 4.2 kg",
                    imageRes = R.drawable.pet_register
                )

                Spacer(modifier = Modifier.height(16.dp))

                PetCard(
                    name = "Coco",
                    breed = "Conejo",
                    info = "1 año",
                    imageRes = R.drawable.pet_login
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 🎯 BOTÓN AGREGAR MASCOTA
                AddPetButton()

                // Espacio para evitar que el contenido final quede oculto tras la mascota
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // 🎯 MASCOTA DECORATIVA (Reposicionamiento para apoyo visual sobre el Navbar)
        Image(
            painter = painterResource(id = R.drawable.pet_list),
            contentDescription = null,
            modifier = Modifier
                .width(135.dp)
                .align(Alignment.BottomEnd)
                // Ajuste para respetar el espacio de la barra de navegación física de Android (gestos o botones)
                .navigationBarsPadding()
                // Elevación vertical corregida para apoyar la mascota sobre el navbar y evitar la zona de sistema
                .offset(y = (-45).dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "Mis Mascotas",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = "Todas tus mascotas en un solo lugar",
                fontSize = 15.sp,
                color = TextSecondary
            )
        }

        Box {
            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notificaciones",
                modifier = Modifier.size(32.dp),
                tint = TextPrimary
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.Red, CircleShape)
                    .border(2.dp, Background, CircleShape)
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
fun PetCard(name: String, breed: String, info: String, imageRes: Int) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = Surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(18.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = breed,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = info,
                    fontSize = 13.sp,
                    color = TextSecondary
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextPrimary,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun AddPetButton() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(Surface, RoundedCornerShape(20.dp))
            .border(1.dp, PrimaryPurple.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = PrimaryPurple,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Agregar nueva mascota",
                color = PrimaryPurple,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Surface,
        tonalElevation = 8.dp,
        // Ajuste para que el navbar respete correctamente la barra de navegación física de Android
        modifier = Modifier
            .navigationBarsPadding()
            .height(80.dp)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Pets, contentDescription = "Mascotas") },
            label = { Text("Mascotas") },
            selected = true,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryPurple,
                selectedTextColor = PrimaryPurple,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.AccessTime, contentDescription = "Recordatorios") },
            label = { Text("Recordatorios") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = false,
            onClick = {},
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )
    }
}