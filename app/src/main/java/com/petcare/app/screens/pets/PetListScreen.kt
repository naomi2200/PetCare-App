package com.petcare.app.screens.pets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import coil.compose.AsyncImage
import com.petcare.app.R
import com.petcare.app.data.local.entity.PetEntity
import com.petcare.app.ui.theme.*

@Composable
fun PetListScreen(
    pets: List<PetEntity>,
    onAddPetClick: () -> Unit,
    onPetClick: (Int) -> Unit,
    onHomeClick: () -> Unit,
    onRemindersClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = onHomeClick,
                onPetsClick = {},
                onRemindersClick = onRemindersClick,
                onProfileClick = onProfileClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPetClick,
                containerColor = PrimaryPurple,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Mascota")
            }
        },
        containerColor = Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            PetListHeader()
            
            if (pets.isEmpty()) {
                EmptyPetsState(onAddPetClick)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(pets) { pet ->
                        PetItemCard(pet = pet, onClick = { onPetClick(pet.id) })
                    }
                }
            }
        }
    }
}

@Composable
fun PetListHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = "Mis Mascotas",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Cuida y organiza la vida de tus amigos",
            fontSize = 16.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun PetItemCard(pet: PetEntity, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen de la mascota (Real o Placeholder)
            if (pet.fotoUrl != null) {
                AsyncImage(
                    model = pet.fotoUrl,
                    contentDescription = pet.nombre,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(PrimaryPurple.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.register_pet),
                        contentDescription = null,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pet.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = pet.raza ?: pet.especie,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (pet.sexo == "Macho") Icons.Default.Male else Icons.Default.Female,
                        contentDescription = null,
                        tint = if (pet.sexo == "Macho") Color(0xFF5D7BFF) else PrimaryPink,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${pet.edad} años",
                        fontSize = 13.sp,
                        color = TextSecondary
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}

@Composable
fun EmptyPetsState(onAddPetClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.pet_list),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Aún no tienes mascotas",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Text(
            text = "Agrega a tu primer compañero para empezar a cuidarlo.",
            fontSize = 16.sp,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onAddPetClick,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPurple),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.height(56.dp).fillMaxWidth()
        ) {
            Text("Agregar mi mascota", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun BottomNavigationBar(
    onHomeClick: () -> Unit,
    onPetsClick: () -> Unit,
    onRemindersClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = Surface,
        tonalElevation = 8.dp,
        modifier = Modifier
            .navigationBarsPadding()
            .height(80.dp)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
            label = { Text("Inicio") },
            selected = false,
            onClick = onHomeClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Pets, contentDescription = "Mascotas") },
            label = { Text("Mascotas") },
            selected = true,
            onClick = onPetsClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.AccessTime, contentDescription = "Recordatorios") },
            label = { Text("Recordatorios") },
            selected = false,
            onClick = onRemindersClick
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            selected = false,
            onClick = onProfileClick
        )
    }
}
