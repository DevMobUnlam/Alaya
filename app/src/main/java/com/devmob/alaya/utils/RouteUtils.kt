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

    sealed class Routes (val route: String) {
        data object Home : Routes("home")
        data object Crisis : Routes("crisis")
        data object Login : Routes ("login")
        data object ContainmentNetwork : Routes("containment_network")
        data object AddContact : Routes ("add_contact")
        data object Feedback : Routes("feedback_screen/{feedbackType}")

    }

    val routeTitleAppBar = mapOf(
        Routes.ContainmentNetwork.route to "Red de Contención",
        Routes.AddContact.route to "Agregar Contacto",
        "contact_detail/{contactId}" to "Detalles del Contacto"
    )
}

