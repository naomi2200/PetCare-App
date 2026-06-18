package com.petcare.app.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.petcare.app.screens.auth.login.LoginScreen
import com.petcare.app.screens.auth.register.RegisterScreen
import com.petcare.app.screens.dashboard.DashboardScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            LoginScreen()
        }

        composable(Screen.Register.route) {
            RegisterScreen()
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen()
        }
    }
}