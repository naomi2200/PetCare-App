package com.petcare.app.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object PetList : Screen("pet_list")
    object AddPet : Screen("add_pet?petId={petId}") {
        fun createRoute(petId: Int? = null) = if (petId != null) "add_pet?petId=$petId" else "add_pet"
    }
    
    object PetDetail : Screen("pet_detail/{petId}") {
        fun createRoute(petId: Int) = "pet_detail/$petId"
    }

    object MedicalHistory : Screen("medical_history/{petId}") {
        fun createRoute(petId: Int) = "medical_history/$petId"
    }

    object WeightTracking : Screen("weight_tracking/{petId}") {
        fun createRoute(petId: Int) = "weight_tracking/$petId"
    }

    object AddWeight : Screen("add_weight/{petId}") {
        fun createRoute(petId: Int) = "add_weight/$petId"
    }

    object ConsultationForm : Screen("consultation_form/{petId}?recordId={recordId}") {
        fun createRoute(petId: Int, recordId: Int? = null) = 
            if (recordId != null) "consultation_form/$petId?recordId=$recordId" else "consultation_form/$petId"
    }

    object VaccineForm : Screen("vaccine_form/{petId}?recordId={recordId}") {
        fun createRoute(petId: Int, recordId: Int? = null) = 
            if (recordId != null) "vaccine_form/$petId?recordId=$recordId" else "vaccine_form/$petId"
    }

    object DewormingForm : Screen("deworming_form/{petId}?recordId={recordId}") {
        fun createRoute(petId: Int, recordId: Int? = null) = 
            if (recordId != null) "deworming_form/$petId?recordId=$recordId" else "deworming_form/$petId"
    }

    object Reminders : Screen("reminders")
    object ReminderForm : Screen("reminder_form?reminderId={reminderId}") {
        fun createRoute(reminderId: Int? = null) = if (reminderId != null) "reminder_form?reminderId=$reminderId" else "reminder_form"
    }
    object Advice : Screen("advice")
    object Profile : Screen("profile")
}