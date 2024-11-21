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

    sealed class LoginRoutes (val route: String){
        data object Login: LoginRoutes("login")
        data object Register: LoginRoutes("register")
    }

    sealed class PatientRoutes(val route: String) {
        data object Home : PatientRoutes("home")
        data object Crisis : PatientRoutes("crisis")
        //data object Login : PatientRoutes("login")
        data object ContainmentNetwork : PatientRoutes("red_de_contencion")
        data object AddContact : PatientRoutes("add_contact")
        data object Feedback : PatientRoutes("feedback_screen/{feedbackType}")
        data object MenuPatient : PatientRoutes ("menu_patient")
        data object CrisisRegistration : PatientRoutes ("crisis_registration")
        data object CrisisRegistrationSummary: PatientRoutes("crisis_registration_summary")
        data object ActivityDay : PatientRoutes("activity_day")
    }

    sealed class ProfessionalRoutes(val route: String) {
        data object Home : ProfessionalRoutes("home_profesional")
        data object PatientProfile : ProfessionalRoutes("patient_profile")
        data object SearchPatient : ProfessionalRoutes("search_patient")
        data object ConfigTreatment : ProfessionalRoutes("config_treatment/{patientEmail}")
        data object TreatmentSummary :
            ProfessionalRoutes("treatment_summary/{firstStep}/{secondStep}/{thirdStep}/{patientEmail}") {
            fun createRoute(firstStep: String, secondStep: String, thirdStep: String, patientEmail: String) =
                "treatment_summary/$firstStep/$secondStep/$thirdStep/$patientEmail"
        }
        data object MenuProfessional : ProfessionalRoutes("menu_professional")
        data object PatientIASummary: ProfessionalRoutes("patient_ia_summary/{patientID}")
        data object AddCustomActivity : ProfessionalRoutes("add_custom_activity/{patientEmail}"){
            fun createRoute(patientEmail: String) =
                "add_custom_activity/$patientEmail"
        }
        data object SendInvitation : ProfessionalRoutes("send_invitation")
        data object ActivityDayProfessional : ProfessionalRoutes("activity_day_professional")
        data object ModalActivityDayProfessional : ProfessionalRoutes("modal_activity_day_professional")
        data object ProfileUser : ProfessionalRoutes("profile_user")
    }

    val routeTitleAppBar = mapOf(
        PatientRoutes.ContainmentNetwork.route to "Red de Contención",
        PatientRoutes.AddContact.route to "Agregar Contacto",
        "contact_detail/{contactId}" to "Detalles del Contacto",
        PatientRoutes.CrisisRegistrationSummary.route to "Resumen",
        PatientRoutes.ActivityDay.route to "Actividades diarias",
        ProfessionalRoutes.PatientProfile.route to "Perfil del paciente",
        "patient_profile/{email}" to "Perfil del paciente",
        ProfessionalRoutes.ConfigTreatment.route to "Configurar tratamiento",
        ProfessionalRoutes.TreatmentSummary.route to "Resumen",
        ProfessionalRoutes.AddCustomActivity.route to "Actividad personalizada",
        ProfessionalRoutes.SendInvitation.route to "Enviar invitación",
        ProfessionalRoutes.ActivityDayProfessional.route to "Actividades diarias",
        ProfessionalRoutes.ModalActivityDayProfessional.route to "Actividades diarias"

        )
        ProfessionalRoutes.SendInvitation.route to "Enviar invitación",
        ProfessionalRoutes.ProfileUser.route to "Mi perfil"
    )

    val routesWithBottomBar = listOf(
        PatientRoutes.Home.route,
        PatientRoutes.ContainmentNetwork.route,
        ProfessionalRoutes.Home.route,
        ProfessionalRoutes.SearchPatient.route,
        PatientRoutes.MenuPatient.route,
        ProfessionalRoutes.MenuProfessional.route,
        ProfessionalRoutes.PatientProfile.route,
        ProfessionalRoutes.SendInvitation.route,
        PatientRoutes.ActivityDay.route,
        ProfessionalRoutes.ActivityDayProfessional.route,
        ProfessionalRoutes.ModalActivityDayProfessional.route,
        PatientRoutes.ActivityDay.route,
        ProfessionalRoutes.ProfileUser.route

    )

    fun isProfessionalRoute(route: String?): Boolean {
        return route in ProfessionalRoutes::class.sealedSubclasses.map { it.objectInstance?.route }
    }

    fun isPatientRoute(route: String?): Boolean {
        return route in PatientRoutes::class.sealedSubclasses.map { it.objectInstance?.route }
    }
}

