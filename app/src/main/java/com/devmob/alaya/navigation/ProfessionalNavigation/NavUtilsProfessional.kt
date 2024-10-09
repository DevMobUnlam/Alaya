package com.devmob.alaya.navigation.ProfessionalNavigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

object NavUtilsProfessional {
    @Composable
    fun currentRoute(navHostController: NavHostController): String? {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }

    sealed class Routes (val route: String) {
        data object Home : Routes("home")
        data object PatientProfile : Routes("patient_profile")
        data object ConfigTreatment : Routes("config_treatment")
        data object TreatmentSummary : Routes("treatment_summary/{firstStep}/{secondStep}/{thirdStep}") {
            fun createRoute(firstStep: String, secondStep: String, thirdStep: String) =
                "treatment_summary/$firstStep/$secondStep/$thirdStep"
        }
    }
}