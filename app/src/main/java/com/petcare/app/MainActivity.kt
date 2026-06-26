package com.petcare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.petcare.app.navigation.AppNavigation
import com.petcare.app.screens.auth.RegisterScreen
import com.petcare.app.screens.profile.ProfileScreen
import com.petcare.app.ui.theme.PetCareTheme
import com.petcare.app.utils.NotificationHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)

        enableEdgeToEdge()

        setContent {
            PetCareTheme {
                ProfileScreen()
            }
        }
    }
}