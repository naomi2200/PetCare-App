package com.petcare.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.petcare.app.PetCareApplication
import com.petcare.app.screens.advice.AdviceScreen
import com.petcare.app.screens.auth.LoginScreen
import com.petcare.app.screens.auth.RegisterScreen
import com.petcare.app.screens.dashboard.DashboardScreen
import com.petcare.app.screens.medical.ConsultationFormScreen
import com.petcare.app.screens.medical.DewormingFormScreen
import com.petcare.app.screens.medical.MedicalHistoryScreen
import com.petcare.app.screens.medical.VaccineFormScreen
import com.petcare.app.screens.pets.AddPetScreen
import com.petcare.app.screens.pets.PetDetailScreen
import com.petcare.app.screens.pets.PetListScreen
import com.petcare.app.screens.profile.ProfileScreen
import com.petcare.app.screens.reminders.ReminderFormScreen
import com.petcare.app.screens.reminders.RemindersScreen
import com.petcare.app.screens.weight.AddWeightScreen
import com.petcare.app.screens.weight.WeightTrackingScreen
import com.petcare.app.utils.ReminderScheduler
import com.petcare.app.viewmodel.*
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val application = context.applicationContext as PetCareApplication
    val reminderScheduler = ReminderScheduler(context)

    // ViewModels
    val authViewModel: AuthViewModel = viewModel()
    val petViewModel: PetViewModel = viewModel(factory = PetViewModelFactory(application.petRepository))
    val medicalViewModel: MedicalViewModel = viewModel(factory = MedicalViewModelFactory(application.medicalRepository))
    val weightViewModel: WeightViewModel = viewModel(factory = WeightViewModelFactory(application.weightRepository))
    val reminderViewModel: ReminderViewModel = viewModel(factory = ReminderViewModelFactory(application.reminderRepository))
    val adviceViewModel: AdviceViewModel = viewModel(factory = AdviceViewModelFactory(com.petcare.app.data.repository.AdviceRepository()))

    // Estados
    val authUiState by authViewModel.uiState.collectAsState()
    val petUiState by petViewModel.uiState.collectAsState()
    val medicalUiState by medicalViewModel.uiState.collectAsState()
    val weightUiState by weightViewModel.uiState.collectAsState()
    val reminderUiState by reminderViewModel.uiState.collectAsState()
    val adviceUiState by adviceViewModel.uiState.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onRegisterClick = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onLoginClick = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate(Screen.Login.route) }
            )
        }

        composable(Screen.Dashboard.route) {
            val userId = authViewModel.getCurrentUserId() ?: "demo_user"
            LaunchedEffect(Unit) {
                authViewModel.loadUserProfile()
                petViewModel.loadPets(userId)
                reminderViewModel.loadAllReminders()
                adviceViewModel.loadRandomAdvice()
            }

            val firstPetId = petUiState.pets.firstOrNull()?.id ?: 0

            DashboardScreen(
                userName = authUiState.userProfile?.name ?: "Usuario",
                pets = petUiState.pets,
                reminders = reminderUiState.reminders,
                dailyAdvice = adviceUiState.dailyAdvice,
                onPetListClick = { navController.navigate(Screen.PetList.route) },
                onRemindersClick = { navController.navigate(Screen.Reminders.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) },
                onAddPetClick = { navController.navigate(Screen.AddPet.createRoute()) },
                onAdviceClick = { navController.navigate(Screen.Advice.route) },
                onMedicalClick = {
                    if (firstPetId != 0) navController.navigate(Screen.MedicalHistory.createRoute(firstPetId))
                    else navController.navigate(Screen.PetList.route)
                },
                onWeightClick = {
                    if (firstPetId != 0) navController.navigate(Screen.WeightTracking.createRoute(firstPetId))
                    else navController.navigate(Screen.PetList.route)
                }
            )
        }

        composable(Screen.PetList.route) {
            val userId = authViewModel.getCurrentUserId() ?: "demo_user"
            LaunchedEffect(userId) { petViewModel.loadPets(userId) }
            PetListScreen(
                pets = petUiState.pets,
                onAddPetClick = { navController.navigate(Screen.AddPet.createRoute()) },
                onPetClick = { petId -> navController.navigate(Screen.PetDetail.createRoute(petId)) },
                onHomeClick = { navController.navigate(Screen.Dashboard.route) },
                onRemindersClick = { navController.navigate(Screen.Reminders.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }

        composable(
            route = Screen.AddPet.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: -1
            val existingPet = if (petId != -1) petUiState.pets.find { it.id == petId } else null

            AddPetScreen(
                userId = authViewModel.getCurrentUserId() ?: "demo_user",
                existingPet = existingPet,
                onBackClick = { navController.popBackStack() },
                onSavePet = { pet ->
                    if (petId != -1) petViewModel.updatePet(pet) else petViewModel.addPet(pet)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.PetDetail.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            LaunchedEffect(petId) { petViewModel.loadPetById(petId) }
            PetDetailScreen(
                pet = petUiState.selectedPet,
                onBackClick = { navController.popBackStack() },
                onEditClick = { navController.navigate(Screen.AddPet.createRoute(petId)) },
                onMedicalHistoryClick = { navController.navigate(Screen.MedicalHistory.createRoute(petId)) },
                onWeightTrackingClick = { navController.navigate(Screen.WeightTracking.createRoute(petId)) },
                onRemindersClick = { navController.navigate(Screen.Reminders.route) },
                onDeleteClick = { pet ->
                    petViewModel.deletePet(pet)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.MedicalHistory.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            LaunchedEffect(petId) { medicalViewModel.loadRecords(petId) }
            MedicalHistoryScreen(
                records = medicalUiState.records,
                onBackClick = { navController.popBackStack() },
                onAddClick = { type ->
                    when (type) {
                        "Vacunas" -> navController.navigate(Screen.VaccineForm.createRoute(petId))
                        "Desparasitaciones" -> navController.navigate(Screen.DewormingForm.createRoute(petId))
                        "Consultas" -> navController.navigate(Screen.ConsultationForm.createRoute(petId))
                    }
                },
                onRecordClick = { record ->
                    when (record.recordType) {
                        "Vacuna" -> navController.navigate(Screen.VaccineForm.createRoute(petId, record.id))
                        "Desparasitación" -> navController.navigate(Screen.DewormingForm.createRoute(petId, record.id))
                        "Consulta" -> navController.navigate(Screen.ConsultationForm.createRoute(petId, record.id))
                    }
                },
                onDeleteRecord = { medicalViewModel.deleteRecord(it) }
            )
        }

        composable(
            route = Screen.VaccineForm.route,
            arguments = listOf(
                navArgument("petId") { type = NavType.IntType },
                navArgument("recordId") { type = NavType.IntType; defaultValue = -1 }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            val recordId = backStackEntry.arguments?.getInt("recordId") ?: -1
            val existingRecord = if (recordId != -1) medicalUiState.records.find { it.id == recordId } else null

            VaccineFormScreen(
                petId = petId,
                existingRecord = existingRecord,
                onBack = { navController.popBackStack() },
                onSave = { record ->
                    if (recordId != -1) medicalViewModel.updateRecord(record) else medicalViewModel.addRecord(record)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.DewormingForm.route,
            arguments = listOf(
                navArgument("petId") { type = NavType.IntType },
                navArgument("recordId") { type = NavType.IntType; defaultValue = -1 }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            val recordId = backStackEntry.arguments?.getInt("recordId") ?: -1
            val existingRecord = if (recordId != -1) medicalUiState.records.find { it.id == recordId } else null

            DewormingFormScreen(
                petId = petId,
                existingRecord = existingRecord,
                onBack = { navController.popBackStack() },
                onSave = { record ->
                    if (recordId != -1) medicalViewModel.updateRecord(record) else medicalViewModel.addRecord(record)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.ConsultationForm.route,
            arguments = listOf(
                navArgument("petId") { type = NavType.IntType },
                navArgument("recordId") { type = NavType.IntType; defaultValue = -1 }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            val recordId = backStackEntry.arguments?.getInt("recordId") ?: -1
            val existingRecord = if (recordId != -1) medicalUiState.records.find { it.id == recordId } else null

            ConsultationFormScreen(
                petId = petId,
                existingRecord = existingRecord,
                onBack = { navController.popBackStack() },
                onSave = { record ->
                    if (recordId != -1) medicalViewModel.updateRecord(record) else medicalViewModel.addRecord(record)
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.WeightTracking.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            LaunchedEffect(petId) {
                weightViewModel.loadWeights(petId)
                petViewModel.loadPets(authViewModel.getCurrentUserId() ?: "demo_user")
            }
            WeightTrackingScreen(
                weights = weightUiState.weights,
                pets = petUiState.pets,
                currentPetId = petId,
                onBackClick = { navController.popBackStack() },
                onAddWeightClick = { navController.navigate(Screen.AddWeight.createRoute(petId)) },
                onPetSelected = { newPetId ->
                    navController.navigate(Screen.WeightTracking.createRoute(newPetId)) {
                        popUpTo(Screen.WeightTracking.route) { inclusive = true }
                    }
                },
                onDeleteWeight = { weightViewModel.deleteWeight(it) },
                onHomeClick = { navController.navigate(Screen.Dashboard.route) },
                onRemindersClick = { navController.navigate(Screen.Reminders.route) },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }

        composable(
            route = Screen.AddWeight.route,
            arguments = listOf(navArgument("petId") { type = NavType.IntType })
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getInt("petId") ?: 0
            AddWeightScreen(
                pets = petUiState.pets,
                initialPetId = petId,
                onBack = { navController.popBackStack() },
                onSave = { weightEntry ->
                    weightViewModel.addWeight(weightEntry)
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Reminders.route) {
            LaunchedEffect(Unit) { reminderViewModel.loadAllReminders() }
            RemindersScreen(
                reminders = reminderUiState.reminders,
                onBackClick = { navController.popBackStack() },
                onAddReminderClick = { navController.navigate(Screen.ReminderForm.createRoute()) },
                onReminderClick = { reminder ->
                    navController.navigate(Screen.ReminderForm.createRoute(reminder.id))
                },
                onDeleteReminder = { reminder ->
                    reminderViewModel.deleteReminder(reminder)
                    reminderScheduler.cancelReminder(reminder)
                },
                onMarkAsCompleted = { reminder ->
                    reminderViewModel.markAsCompleted(reminder)
                    reminderScheduler.cancelReminder(reminder)
                },
                onHomeClick = { navController.navigate(Screen.Dashboard.route) },
                onPetsClick = { navController.navigate(Screen.PetList.route) },
                onRemindersClick = { /* Actual */ },
                onProfileClick = { navController.navigate(Screen.Profile.route) }
            )
        }

        composable(
            route = Screen.ReminderForm.route,
            arguments = listOf(navArgument("reminderId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val reminderId = backStackEntry.arguments?.getInt("reminderId") ?: -1
            val existingReminder = if (reminderId != -1) reminderUiState.reminders.find { it.id == reminderId } else null

            ReminderFormScreen(
                pets = petUiState.pets,
                existingReminder = existingReminder,
                onBack = { navController.popBackStack() },
                onSave = { reminder ->
                    scope.launch {
                        if (reminderId != -1) {
                            reminderViewModel.updateReminder(reminder)
                            existingReminder?.let { reminderScheduler.cancelReminder(it) }
                            reminderScheduler.scheduleReminder(reminder)
                        } else {
                            val newId = reminderViewModel.addReminder(reminder)
                            reminderScheduler.scheduleReminder(reminder.copy(id = newId.toInt()))
                        }
                        navController.popBackStack()
                    }
                }
            )
        }

        composable(Screen.Advice.route) {
            LaunchedEffect(Unit) {
                adviceViewModel.loadAdvice()
                adviceViewModel.loadRandomAdvice()
            }
            AdviceScreen(
                adviceList = adviceUiState.adviceList,
                dailyAdvice = adviceUiState.dailyAdvice,
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            val authUiState by authViewModel.uiState.collectAsState()
            LaunchedEffect(Unit) {
                authViewModel.loadUserProfile()
            }
            ProfileScreen(
                userName = authUiState.userProfile?.name ?: "Usuario PetCare",
                userEmail = authUiState.userProfile?.email ?: "",
                petCount = petUiState.pets.size,
                reminderCount = reminderUiState.reminders.size,
                medicalCount = medicalUiState.records.size,
                onLogoutClick = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onHomeClick = { navController.navigate(Screen.Dashboard.route) },
                onPetsClick = { navController.navigate(Screen.PetList.route) },
                onRemindersClick = { navController.navigate(Screen.Reminders.route) }
            )
        }
    }
}
