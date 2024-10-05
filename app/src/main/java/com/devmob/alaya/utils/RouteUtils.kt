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
}