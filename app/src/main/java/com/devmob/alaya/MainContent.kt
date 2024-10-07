package com.devmob.alaya

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devmob.alaya.domain.model.FeedbackType
import com.devmob.alaya.domain.model.IconType
import com.devmob.alaya.domain.model.ItemMenu
import com.devmob.alaya.ui.screen.HomeScreen
import com.devmob.alaya.ui.components.BottomBarNavigation
import com.devmob.alaya.ui.screen.feedback.FeedbackScreen
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingScreen
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingViewModel
import com.devmob.alaya.utils.NavUtils

@Composable
fun MainContent(navController: NavHostController) {
    val currentRoute = NavUtils.currentRoute(navController)
    Scaffold(
        bottomBar = {
            //condicion para mostrar o no el bottom
            //agregar a la lista las rutas que no deberian mostrarse!!
            if (currentRoute !in listOf("nobottom", "nobottom2", NavUtils.Routes.Crisis.route,NavUtils.Routes.Feedback.route)) {
                BottomBarNavigation(
                    items = listOf(
                        ItemMenu(iconType = IconType.MENU, route = "menu", contentDescription = "menu", order = 3),
                        ItemMenu(iconType = IconType.PATIENT, route = NavUtils.Routes.Crisis.route, contentDescription = "boton para el manejo de crisis", order = 2),
                        ItemMenu(iconType = IconType.HOME, route = NavUtils.Routes.Home.route, contentDescription = "boton de inicio", order = 1),
                    ),
                    navHostController = navController
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavUtils.Routes.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavUtils.Routes.Home.route) {
                HomeScreen(navController)
            }
            composable(NavUtils.Routes.Crisis.route) {
                CrisisHandlingScreen(CrisisHandlingViewModel(), navController)
            }

            composable("feedback_screen/{feedbackType}") { backStackEntry ->
                val feedbackType = backStackEntry.arguments?.getString("feedbackType")?.let {
                    FeedbackType.valueOf(it)
                }
                FeedbackScreen(feedbackType = feedbackType ?: FeedbackType.TodoVaAEstarBien,navController)
            }
        }
    }
}
