package com.petcare.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// 🎨 Mapeo oficial de tu paleta PetCare en Modo Oscuro
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,     // ✅ Resuelto
    secondary = PrimaryPink,     // ✅ Resuelto
    tertiary = SuccessMint,      // ✅ Resuelto
    background = TextPrimary,    // Inversión semántica para dark mode
    surface = TextPrimary
)

// 🎨 Mapeo oficial de tu paleta PetCare en Modo Claro (Según tu UI de Login/Register)
private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,     // ✅ Resuelto
    secondary = PrimaryPink,     // ✅ Resuelto
    tertiary = SuccessMint,      // ✅ Resuelto
    background = Background,     // ✅ Resuelto
    surface = Surface,           // ✅ Resuelto
    onPrimary = Surface,
    onSecondary = Surface,
    onBackground = TextPrimary,  // ✅ Resuelto
    onSurface = TextPrimary      // ✅ Resuelto
)

@Composable
fun PetCareTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // 💡 Consejo técnico: Ponlo en 'false' para forzar que tu app use tus colores PetCare reales y no los del sistema del teléfono
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Asegúrate de que tu archivo 'Type.kt' use 'Typography' sin errores
        content = content
    )
}