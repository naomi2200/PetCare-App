package com.petcare.app.navigation
sealed class Screen(val route: String) {

    object Login : Screen("login")

    object Register : Screen("register")

    object Dashboard : Screen("dashboard")

    object PetList : Screen("pet_list")

    object AddPet : Screen("add_pet")

    object PetDetail : Screen("pet_detail/{petId}") {
        fun createRoute(petId: Int) = "pet_detail/$petId"
    }

    object MedicalHistory : Screen("medical_history")

    object WeightTracking : Screen("weight_tracking")

    object Reminders : Screen("reminders")

    object Advice : Screen("advice")

    object Profile : Screen("profile")
}