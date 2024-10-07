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
        data object RedDeContencion : Routes("red_de_contencion")
        data object AddContact : Routes ("add_contact")
    }

    val routeTitleAppBar = mapOf(
        Routes.RedDeContencion.route to "Red de Contención",
        Routes.AddContact.route to "Agregar Contacto",
        "contact_detail/{contactId}" to "Detalles del Contacto"
    )
}

