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
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.petcare.app.R
import com.petcare.app.data.remote.dto.AdviceDto
import com.petcare.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdviceScreen(
    adviceList: List<AdviceDto>,
    dailyAdvice: AdviceDto? = null,
    onBackClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Consejos y Curiosidades",
                        fontSize = 20.sp,
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Background
                )
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
            // Consejo Destacado (Daily Advice)
            item {
                AdviceFeaturedCard(dailyAdvice ?: adviceList.firstOrNull())
            }

            // Título de sección
            item {
                Text(
                    text = "Explorar consejos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Lista de consejos desde Firestore
            if (adviceList.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(40.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = PrimaryPurple)
                    }
                }
            } else {
                items(adviceList) { advice ->
                    AdviceItemCard(advice)
                }
            }
        }
    }
}

@Composable
private fun AdviceFeaturedCard(advice: AdviceDto?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEBE9FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 24.dp, top = 20.dp, bottom = 20.dp, end = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.3f)) {
                Surface(
                    color = PrimaryPurple,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "TIP DEL DÍA",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = advice?.title ?: "¿Sabías que?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = advice?.description ?: "Cargando el mejor consejo para tu mascota...",
                    fontSize = 14.sp,
                    color = TextPrimary,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 4
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

@Composable
private fun AdviceItemCard(advice: AdviceDto) {
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
                    .size(50.dp)
                    .background(Color(0xFFF3F2FF), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (advice.category == "Salud") Icons.Outlined.Pets else Icons.Outlined.Lightbulb,
                    contentDescription = null,
                    tint = PrimaryPurple,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = advice.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = advice.description,
                    fontSize = 13.sp,
                    color = TextSecondary,
                    lineHeight = 18.sp,
                    maxLines = 2
                )
            }
        }
    }
}