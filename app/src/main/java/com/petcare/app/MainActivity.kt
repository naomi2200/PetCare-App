package com.petcare.app

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.petcare.app.navigation.AppNavigation
import com.petcare.app.ui.theme.PetCareTheme
import com.petcare.app.utils.NotificationHelper

class MainActivity : ComponentActivity() {

    // Launcher para pedir permiso de notificaciones en Android 13+
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        Log.d("ReminderDiag", "MAIN: Permiso POST_NOTIFICATIONS concedido: $isGranted")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("ReminderDiag", "MAIN: onCreate iniciado")

        // Crear canal de notificaciones
        Log.d("ReminderDiag", "MAIN: Llamando a createNotificationChannel")
        NotificationHelper.createNotificationChannel(this)
        
        // Pedir permiso en Android 13+
        askNotificationPermission()

        enableEdgeToEdge()
        setContent {
            PetCareTheme {
                AppNavigation()
            }
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            Log.d("ReminderDiag", "MAIN: Chequeando permiso POST_NOTIFICATIONS. Status: $permissionStatus")
            if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
                Log.d("ReminderDiag", "MAIN: Solicitando permiso POST_NOTIFICATIONS...")
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            Log.d("ReminderDiag", "MAIN: Versión de Android < 13. No se requiere permiso POST_NOTIFICATIONS dinámico.")
        }
    }
}
