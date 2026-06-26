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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.data.local.entity.PetEntity
import com.petcare.app.data.local.entity.WeightEntryEntity
import com.petcare.app.screens.pets.BottomNavigationBar
import com.petcare.app.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightTrackingScreen(
    weights: List<WeightEntryEntity>,
    pets: List<PetEntity>,
    currentPetId: Int,
    onBackClick: () -> Unit,
    onAddWeightClick: () -> Unit,
    onPetSelected: (Int) -> Unit,
    onDeleteWeight: (WeightEntryEntity) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onRemindersClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val selectedPet = pets.find { it.id == currentPetId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Seguimiento de Peso", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Atrás", tint = TextPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Background)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                onHomeClick = onHomeClick,
                onPetsClick = { },
                onRemindersClick = onRemindersClick,
                onProfileClick = onProfileClick
            )
        },
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
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                PetSelectorDropdown(
                    selectedPetName = selectedPet?.nombre ?: "Seleccionar",
                    pets = pets,
                    onPetSelected = onPetSelected
                )
            }

            item {
                WeightChartCard(weights)
            }

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
                        modifier = Modifier.fillMaxWidth().shadow(1.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Surface)
                    ) {
                        if (weights.isEmpty()) {
                            Text(
                                "No hay registros de peso.",
                                modifier = Modifier.padding(20.dp),
                                color = TextSecondary
                            )
                        } else {
                            Column {
                                weights.forEachIndexed { index, record ->
                                    WeightHistoryItem(
                                        record = record,
                                        isLast = index == weights.size - 1,
                                        onDelete = { onDeleteWeight(record) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WeightHistoryItem(
    record: WeightEntryEntity, 
    isLast: Boolean,
    onDelete: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(Color(0xFFF0EFFF), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = null, tint = PrimaryPurple, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = record.date, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(text = "${record.weight} kg", fontSize = 14.sp, color = PrimaryPurple, fontWeight = FontWeight.Medium)
            }
            
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red.copy(alpha = 0.5f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        if (!isLast) {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp), thickness = 1.dp, color = Border.copy(alpha = 0.2f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PetSelectorDropdown(
    selectedPetName: String,
    pets: List<PetEntity>,
    onPetSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        Card(
            onClick = { expanded = true },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Surface),
            modifier = Modifier.width(180.dp).shadow(1.dp, RoundedCornerShape(24.dp))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pet_dashboard),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFF0EFFF)),
                    contentScale = ContentScale.Crop
                )
                Text(text = selectedPetName, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 16.sp, modifier = Modifier.weight(1f))
                Icon(imageVector = Icons.Outlined.KeyboardArrowDown, contentDescription = null, tint = PrimaryPurple)
            }
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Surface)) {
            pets.forEach { pet ->
                DropdownMenuItem(
                    text = { Text(pet.nombre) },
                    onClick = {
                        onPetSelected(pet.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun WeightChartCard(weights: List<WeightEntryEntity>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .shadow(2.dp, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Column(modifier = Modifier.padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 12.dp)) {
            Box(modifier = Modifier.fillMaxSize()) {
                WeightGraph(weights)
            }
        }
    }
}

@Composable
private fun WeightGraph(weights: List<WeightEntryEntity>) {
    val textMeasurer = rememberTextMeasurer()
    val axisTextStyle = TextStyle(
        color = TextSecondary.copy(alpha = 0.6f),
        fontSize = 10.sp,
        fontWeight = FontWeight.Medium
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        if (weights.isEmpty()) return@Canvas

        val paddingLeft = 35.dp.toPx()
        val paddingBottom = 25.dp.toPx()
        val paddingTop = 25.dp.toPx()
        val paddingRight = 15.dp.toPx()

        val canvasWidth = size.width - paddingLeft - paddingRight
        val canvasHeight = size.height - paddingBottom - paddingTop

        // Datos: invertimos para orden cronológico (izq -> der) y tomamos máximo 7 puntos.
        val displayWeights = weights.reversed().takeLast(7)
        
        val maxW = displayWeights.maxOf { it.weight }.toFloat()
        val minW = displayWeights.minOf { it.weight }.toFloat()
        
        val range = (maxW - minW).coerceAtLeast(1f)
        val yMax = maxW + (range * 0.3f)
        val yMin = (minW - (range * 0.3f)).coerceAtLeast(0f)
        val yRange = yMax - yMin

        // Dibujar Grid horizontal y Eje Y
        val gridLines = 5
        for (i in 0 until gridLines) {
            val ratio = i.toFloat() / (gridLines - 1)
            val y = paddingTop + canvasHeight - (ratio * canvasHeight)
            val weightVal = yMin + (ratio * yRange)

            drawLine(
                color = Color.LightGray.copy(alpha = 0.2f),
                start = Offset(paddingLeft, y),
                end = Offset(size.width - paddingRight, y),
                strokeWidth = 1.dp.toPx()
            )

            drawText(
                textMeasurer = textMeasurer,
                text = "${weightVal.toInt()}",
                style = axisTextStyle,
                topLeft = Offset(5.dp.toPx(), y - 7.dp.toPx())
            )
        }

        // Calcular coordenadas de puntos
        val points = displayWeights.mapIndexed { index, entry ->
            val x = paddingLeft + if (displayWeights.size > 1) {
                (index.toFloat() / (displayWeights.size - 1)) * canvasWidth
            } else {
                canvasWidth / 2
            }
            val y = paddingTop + canvasHeight - ((entry.weight.toFloat() - yMin) / yRange) * canvasHeight
            Offset(x, y)
        }

        // Dibujar Eje X (Fechas abreviadas)
        displayWeights.forEachIndexed { index, entry ->
            val x = points[index].x
            val dateLabel = try {
                val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = inputFormat.parse(entry.date)
                SimpleDateFormat("dd MMM", Locale.getDefault()).format(date!!)
            } catch (e: Exception) {
                entry.date.take(5)
            }

            val textLayoutResult = textMeasurer.measure(dateLabel, axisTextStyle)
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(x - textLayoutResult.size.width / 2, size.height - paddingBottom + 5.dp.toPx())
            )
        }

        // Dibujar Área sombreada con degradado
        if (points.size > 1) {
            val fillPath = Path().apply {
                moveTo(points.first().x, size.height - paddingBottom)
                points.forEach { lineTo(it.x, it.y) }
                lineTo(points.last().x, size.height - paddingBottom)
                close()
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(PrimaryPurple.copy(alpha = 0.2f), Color.Transparent),
                    startY = points.minOf { it.y },
                    endY = size.height - paddingBottom
                )
            )
        }

        // Dibujar Línea principal con curva suave
        if (points.size > 1) {
            val strokePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    val prev = points[i - 1]
                    val curr = points[i]
                    val cp1 = Offset(prev.x + (curr.x - prev.x) / 2f, prev.y)
                    val cp2 = Offset(prev.x + (curr.x - prev.x) / 2f, curr.y)
                    cubicTo(cp1.x, cp1.y, cp2.x, cp2.y, curr.x, curr.y)
                }
            }
            drawPath(
                path = strokePath,
                color = PrimaryPurple,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        } else if (points.size == 1) {
            // Caso de un solo registro: solo dibujamos el punto más tarde
        }

        // Dibujar Puntos (Borde morado, centro blanco)
        points.forEachIndexed { index, point ->
            drawCircle(
                color = PrimaryPurple,
                radius = 5.dp.toPx(),
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 3.dp.toPx(),
                center = point
            )

            // Tooltip únicamente en el último registro
            if (index == points.size - 1) {
                val weightText = "${displayWeights[index].weight} kg"
                val tooltipStyle = TextStyle(
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                val textLayout = textMeasurer.measure(weightText, tooltipStyle)
                
                val tooltipWidth = textLayout.size.width + 20.dp.toPx()
                val tooltipHeight = textLayout.size.height + 10.dp.toPx()
                
                // Alineación del tooltip (evitar que se salga del canvas por la derecha)
                val tooltipX = (point.x - tooltipWidth / 2).coerceIn(paddingLeft, size.width - tooltipWidth)
                val tooltipY = point.y - tooltipHeight - 12.dp.toPx()

                // Fondo del Tooltip
                drawRoundRect(
                    color = PrimaryPurple,
                    topLeft = Offset(tooltipX, tooltipY),
                    size = Size(tooltipWidth, tooltipHeight),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )

                // Flecha del Tooltip
                val trianglePath = Path().apply {
                    moveTo(point.x - 6.dp.toPx(), tooltipY + tooltipHeight)
                    lineTo(point.x + 6.dp.toPx(), tooltipY + tooltipHeight)
                    lineTo(point.x, tooltipY + tooltipHeight + 6.dp.toPx())
                    close()
                }
                drawPath(trianglePath, PrimaryPurple)

                // Texto del Tooltip
                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(
                        tooltipX + (tooltipWidth - textLayout.size.width) / 2,
                        tooltipY + (tooltipHeight - textLayout.size.height) / 2
                    )
                )
            }
        }
    }
}
