package com.petcare.app.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun PetCareWaveFooter(
    drawableResId: Int,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = drawableResId),
        contentDescription = "PetCare Footer Wave",
        contentScale = ContentScale.FillWidth,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}