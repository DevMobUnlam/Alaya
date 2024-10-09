package com.devmob.alaya.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

object NavUtils {
    @Composable
    fun currentRoute(navHostController: NavHostController): String? {
        val navBackStackEntry by navHostController.currentBackStackEntryAsState()
        return navBackStackEntry?.destination?.route
    }

    sealed class PatientRoutes(val route: String) {
        data object Home : PatientRoutes("home")
        data object Crisis : PatientRoutes("crisis")
        data object Login : PatientRoutes("login")
        data object RedDeContencion : PatientRoutes("red_de_contencion")
        data object AddContact : PatientRoutes("add_contact")
        data object Feedback : PatientRoutes("feedback_screen/{feedbackType}")
    }

    sealed class ProfessionalRoutes(val route: String) {
        data object Home : ProfessionalRoutes("home_profesional")
        data object PatientProfile : ProfessionalRoutes("patient_profile")
        data object SearchPatient : ProfessionalRoutes("search_patient")
        data object ConfigTreatment : ProfessionalRoutes("config_treatment")
        data object TreatmentSummary :
            ProfessionalRoutes("treatment_summary/{firstStep}/{secondStep}/{thirdStep}") {
            fun createRoute(firstStep: String, secondStep: String, thirdStep: String) =
                "treatment_summary/$firstStep/$secondStep/$thirdStep"
        }
    }

    val routeTitleAppBar = mapOf(
        PatientRoutes.RedDeContencion.route to "Red de Contención",
        PatientRoutes.AddContact.route to "Agregar Contacto",
        "contact_detail/{contactId}" to "Detalles del Contacto"
    )

    val routesWithBottomBar = listOf(
        PatientRoutes.Home.route,
        PatientRoutes.RedDeContencion.route,
        ProfessionalRoutes.Home.route,
        ProfessionalRoutes.SearchPatient.route
    )

    fun isProfessionalRoute(route: String?): Boolean {
        return route in ProfessionalRoutes::class.sealedSubclasses.map { it.objectInstance?.route }
    }

    fun isPatientRoute(route: String?): Boolean {
        return route in PatientRoutes::class.sealedSubclasses.map { it.objectInstance?.route }
    }
}

