package com.petcare.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.petcare.app.PetCareApplication
import com.petcare.app.screens.auth.LoginScreen
import com.petcare.app.screens.auth.RegisterScreen
import com.petcare.app.screens.pets.AddPetScreen
import com.petcare.app.screens.pets.PetDetailScreen
import com.petcare.app.screens.pets.PetListScreen
import com.petcare.app.viewmodel.AuthViewModel
import com.petcare.app.viewmodel.PetViewModel
import com.petcare.app.viewmodel.PetViewModelFactory

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as PetCareApplication

    val authViewModel: AuthViewModel = viewModel()

    val petViewModel: PetViewModel = viewModel(
        factory = PetViewModelFactory(application.petRepository)
    )

    val petUiState by petViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.PetList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onLoginClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.PetList.route) {
            val userId = authViewModel.getCurrentUserId() ?: "demo_user"

            LaunchedEffect(userId) {
                petViewModel.loadPets(userId)
            }

            PetListScreen(
                pets = petUiState.pets,
                onAddPetClick = {
                    navController.navigate(Screen.AddPet.route)
                },
                onPetClick = { petId ->
                    navController.navigate(Screen.PetDetail.createRoute(petId))
                }
            )
        }

        composable(Screen.AddPet.route) {
            val userId = authViewModel.getCurrentUserId() ?: "demo_user"

            AddPetScreen(
                userId = userId,
                onBackClick = {
                    navController.popBackStack()
                },
                onSavePet = { pet ->
                    petViewModel.addPet(pet)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.PetDetail.route,
            arguments = listOf(
                navArgument("petId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0

            LaunchedEffect(petId) {
                petViewModel.loadPetById(petId)
            }

            PetDetailScreen(
                pet = petUiState.selectedPet,
                onBackClick = {
                    navController.popBackStack()
                },
                onDeleteClick = { pet ->
                    petViewModel.deletePet(pet)
                    navController.popBackStack()
                }
            )
        }
    }
}