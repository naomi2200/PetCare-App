package com.petcare.app.screens.weight

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.screens.pets.BottomNavigationBar
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightTrackingScreen(
    onBackClick: () -> Unit = {},
    onAddWeightClick: () -> Unit = {}
) {
    var selectedPet by remember { mutableStateOf("Milo") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Seguimiento de Peso",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Background)
            )
        },
        bottomBar = { BottomNavigationBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddWeightClick,
                containerColor = PrimaryPurple,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.padding(bottom = 16.dp, end = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Peso")
            }
        },
        containerColor = Background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. Selector de Mascota
            item {
                PetSelectorDropdown(
                    selectedPet = selectedPet,
                    onPetSelected = { selectedPet = it }
                )
            }

            // 2. Gráfico de Peso
            item {
                WeightChartCard()
            }

            // 3. Historial de Registros
            item {
                Column {
                    Text(
                        text = "Historial de registros",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(1.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface)
                    ) {
                        val history = getMockWeightHistory()
                        Column {
                            history.forEachIndexed { index, record ->
                                WeightHistoryItem(
                                    record = record,
                                    isLast = index == history.size - 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeightHistoryItem(record: WeightRecord, isLast: Boolean) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono con fondo lila suave
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF0EFFF), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = PrimaryPurple,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Fecha
            Text(
                text = record.date,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            // Peso destacado en púrpura
            Text(
                text = "${record.weight} kg",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryPurple
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Chevron (Flecha de navegación)
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                contentDescription = null,
                tint = TextSecondary,
                modifier = Modifier.size(16.dp)
            )
        }

        if (!isLast) {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                thickness = 1.dp,
                color = Border.copy(alpha = 0.2f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PetSelectorDropdown(
    selectedPet: String,
    onPetSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val pets = listOf("Milo", "Luna", "Coco")

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            onClick = { expanded = true },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Surface),
            modifier = Modifier
                .width(160.dp)
                .shadow(1.dp, RoundedCornerShape(24.dp))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pet_dashboard),
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0EFFF)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = selectedPet,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowDown,
                    contentDescription = null,
                    tint = PrimaryPurple
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Surface)
        ) {
            pets.forEach { pet ->
                DropdownMenuItem(
                    text = { Text(pet) },
                    onClick = {
                        onPetSelected(pet)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun WeightChartCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .shadow(2.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(text = "kg", color = TextSecondary, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                // Eje Y Labels
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 24.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("40", "35", "30", "25", "20", "15", "10").forEach { yLabel ->
                        Text(text = yLabel, color = TextSecondary, fontSize = 12.sp)
                    }
                }

                // Área de dibujo del gráfico
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 28.dp)
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        // Líneas de cuadrícula horizontales
                        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            repeat(7) {
                                HorizontalDivider(color = Border.copy(alpha = 0.3f), thickness = 1.dp)
                            }
                        }

                        WeightGraph()

                        // Tooltip destacado 32kg
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-10).dp, y = 35.dp)
                        ) {
                            Surface(
                                color = PrimaryPurple,
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "32 kg",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Eje X Labels (Meses)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        listOf("Ene", "Feb", "Mar", "Abr", "May", "Jun").forEach { month ->
                            Text(text = month, color = TextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeightGraph() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Puntos de datos normalizados para la visualización
        val points = listOf(
            Offset(0f, height * 0.85f),
            Offset(width * 0.2f, height * 0.7f),
            Offset(width * 0.4f, height * 0.58f),
            Offset(width * 0.6f, height * 0.62f),
            Offset(width * 0.8f, height * 0.4f),
            Offset(width, height * 0.15f)
        )

        // Dibujar el degradado bajo la línea
        val fillPath = Path().apply {
            moveTo(points[0].x, height)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(width, height)
            close()
        }
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(PrimaryPurple.copy(alpha = 0.25f), Color.Transparent)
            )
        )

        // Dibujar la línea principal
        val strokePath = Path().apply {
            moveTo(points[0].x, points[0].y)
            for (i in 1 until points.size) {
                lineTo(points[i].x, points[i].y)
            }
        }
        drawPath(
            path = strokePath,
            color = PrimaryPurple,
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )

        // Dibujar los nodos (círculos)
        points.forEach { point ->
            drawCircle(color = Color.White, radius = 6.dp.toPx(), center = point)
            drawCircle(color = PrimaryPurple, radius = 6.dp.toPx(), center = point, style = Stroke(width = 3.dp.toPx()))
        }
    }
}

private data class WeightRecord(val date: String, val weight: Int)

private fun getMockWeightHistory() = listOf(
    WeightRecord("10 Jun 2024", 32),
    WeightRecord("10 May 2024", 29),
    WeightRecord("10 Abr 2024", 27),
    WeightRecord("10 Mar 2024", 25)
)
