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
import com.devmob.alaya.ui.components.BottomBarNavigation
import com.devmob.alaya.ui.screen.HomeScreen
import com.devmob.alaya.ui.screen.CrisisHandlingScreen
import com.devmob.alaya.ui.screen.CrisisHandlingViewModel
import com.devmob.alaya.utils.NavUtils
import com.tuapp.ui.components.FeedbackScreen

@Composable
fun MainContent(navController: NavHostController) {
    val currentRoute = NavUtils.currentRoute(navController)
    Scaffold(
        bottomBar = {
            if (currentRoute !in listOf("nobottom", "nobottom2", NavUtils.Routes.Crisis.route)) {
                BottomBarNavigation(
                    items = listOf(
                        ItemMenu(iconType = IconType.MENU, route = "menu", contentDescription = "menu", order = 3),
                        ItemMenu(iconType = IconType.PATIENT, route = NavUtils.Routes.Crisis.route, contentDescription = "boton para el manejo de crisis", order = 2),
                        ItemMenu(iconType = IconType.HOME, route = "inicio", contentDescription = "boton de inicio", order = 1),
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
                FeedbackScreen(feedbackType = feedbackType ?: FeedbackType.TodoVaAEstarBien)
            }
        }
    }
}
