package com.devmob.alaya.navigation.professionalNavigation

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
}