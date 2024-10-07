package com.devmob.alaya

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devmob.alaya.domain.model.IconType
import com.devmob.alaya.domain.model.ItemMenu
import com.devmob.alaya.ui.screen.HomeScreen
import com.devmob.alaya.ui.components.BottomBarNavigation
import com.devmob.alaya.ui.screen.login.SreenLogin
import com.devmob.alaya.utils.NavUtils

@Composable
fun MainContent(navController: NavHostController) {
    val currentRoute = NavUtils.currentRoute(navController)
    Scaffold(
        bottomBar = {
            //condicion para mostrar o no el bottom
            //agregar a la lista las rutas que no deberian mostrarse!!
            if (currentRoute !in listOf("login","nobottom", "nobottom2")) {
                BottomBarNavigation(
                    items = listOf(
                        ItemMenu(iconType = IconType.MENU, route = "menu", contentDescription = "menu", order = 3),
                        ItemMenu(iconType = IconType.PATIENT, route = "nobottom", contentDescription = "boton para el manejo de crisis", order = 2),
                        ItemMenu(iconType = IconType.HOME, route = "inicio", contentDescription = "boton de inicio", order = 1),
                    ),
                    navHostController = navController
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home") {
                HomeScreen(navController)
            }
            composable("login"){
                SreenLogin(navController)
            }

        }
    }
}
