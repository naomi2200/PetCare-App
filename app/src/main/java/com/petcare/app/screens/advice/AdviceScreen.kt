package com.petcare.app.screens.advice

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.DirectionsRun
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.components.PetCareBottomBar
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdviceScreen(
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Consejos y Curiosidades",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Atrás",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                )
            )
        },
        bottomBar = {
            // Reemplazo de la barra antigua por el nuevo componente reutilizable
            PetCareBottomBar(
                currentRoute = "inicio", // O la ruta que corresponda a esta sección
                onNavigate = onNavigate
            )
        },
        containerColor = Background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                AdviceFeaturedCard()
            }

            items(getAdviceCategories()) { category ->
                AdviceCategoryCard(category)
            }
        }
    }
}

@Composable
private fun AdviceFeaturedCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEBE9FF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 24.dp, top = 20.dp, bottom = 20.dp, end = 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1.2f)
                ) {
                    Text(
                        text = "¿Sabías que?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Los perros tienen alrededor de 300 millones de receptores olfativos.",
                        fontSize = 15.sp,
                        color = TextPrimary,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Image(
                    painter = painterResource(id = R.drawable.pet_advice),
                    contentDescription = null,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.BottomEnd
                )
            }
        }
    }
}

@Composable
private fun AdviceCategoryCard(category: AdviceCategory) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(category.iconBackgroundColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = null,
                    tint = category.iconTintColor,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = category.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = category.description,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private data class AdviceCategory(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val iconBackgroundColor: Color,
    val iconTintColor: Color
)

private fun getAdviceCategories(): List<AdviceCategory> = listOf(
    AdviceCategory(
        title = "Consejos de Salud",
        description = "Cuida la salud de tu mascota",
        icon = Icons.Outlined.MedicalServices,
        iconBackgroundColor = Color(0xFFE1F5FE),
        iconTintColor = Color(0xFF039BE5)
    ),
    AdviceCategory(
        title = "Alimentación",
        description = "Mejores alimentos para ellos",
        icon = Icons.Outlined.Restaurant,
        iconBackgroundColor = Color(0xFFFFF3E0),
        iconTintColor = Color(0xFFF57C00)
    ),
    AdviceCategory(
        title = "Ejercicio y Diversión",
        description = "Actividades para mantenerlos felices",
        icon = Icons.AutoMirrored.Outlined.DirectionsRun,
        iconBackgroundColor = Color(0xFFE8F5E9),
        iconTintColor = Color(0xFF388E3C)
    )
)