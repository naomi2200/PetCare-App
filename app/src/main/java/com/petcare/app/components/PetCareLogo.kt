package com.petcare.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.petcare.app.R

@Composable
fun PetCareLogo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logo_petcare),
        contentDescription = "PetCare Logo",
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    )
}