package com.petcare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.petcare.app.navigation.AppNavigation // ✅ Importante para que reconozca tu enrutador
import com.petcare.app.screens.pets.PetDetailScreen
import com.petcare.app.screens.pets.PetListScreen
import com.petcare.app.ui.theme.PetCareTheme
import com.petcare.app.screens.pets.AddPetScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Mantiene el diseño extendido moderno de borde a borde
        setContent {
            PetCareTheme {
                // 🚀 CAMBIO CLAVE: Quitamos el Scaffold genérico y llamamos a tu navegación real
                AddPetScreen()
            }
        }
    }
}