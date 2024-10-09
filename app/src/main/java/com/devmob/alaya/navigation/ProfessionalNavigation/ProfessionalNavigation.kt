package com.devmob.alaya.navigation.ProfessionalNavigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devmob.alaya.domain.model.IconType
import com.devmob.alaya.domain.model.ItemMenu
import com.devmob.alaya.ui.components.BottomBarNavigation
import com.devmob.alaya.ui.screen.ProfessionalTreatment.ConfigTreatmentScreen
import com.devmob.alaya.ui.screen.ProfessionalTreatment.ConfigTreatmentViewModel
import com.devmob.alaya.ui.screen.TreatmentSummaryScreen.TreatmentSummaryScreen
import com.devmob.alaya.ui.screen.patient_profile.PatientProfileScreen
import com.devmob.alaya.ui.screen.professionalHome.ProfessionalHomeScreen
import com.devmob.alaya.ui.screen.professionalHome.ProfessionalHomeViewModel
import com.devmob.alaya.utils.NavUtils

@Composable
fun ProfessionalNavigation(navController: NavHostController){
    val currentRoute = NavUtilsProfessional.currentRoute(navController)
    Scaffold(
        bottomBar = {
            //condicion para mostrar o no el bottom
            //agregar a la lista las rutas que no deberian mostrarse!!
            if (currentRoute !in listOf("nobottom", "nobottom2")) {
                BottomBarNavigation(
                    items = listOf(
                        ItemMenu(iconType = IconType.MENU, route = "menu", contentDescription = "menu", order = 3),
                        ItemMenu(iconType = IconType.PROFESSIONAL, route = NavUtilsProfessional.Routes.Home.route, contentDescription = "Home profesional", order = 2),
                        ItemMenu(iconType = IconType.HOME, route = NavUtils.Routes.Home.route, contentDescription = "boton de inicio", order = 1),
                    ),
                    navHostController = navController
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavUtilsProfessional.Routes.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavUtilsProfessional.Routes.Home.route) {
                ProfessionalHomeScreen(ProfessionalHomeViewModel(), navController)
            }
            composable(NavUtilsProfessional.Routes.PatientProfile.route){
                PatientProfileScreen(navController)
            }
            composable(NavUtilsProfessional.Routes.ConfigTreatment.route){
                ConfigTreatmentScreen(ConfigTreatmentViewModel(), navController)
            }
            composable(NavUtilsProfessional.Routes.TreatmentSummary.route) { backStackEntry ->
                val firstStep = backStackEntry.arguments?.getString("firstStep") ?: ""
                val secondStep = backStackEntry.arguments?.getString("secondStep") ?: ""
                val thirdStep = backStackEntry.arguments?.getString("thirdStep") ?: ""

                TreatmentSummaryScreen(firstStep, secondStep, thirdStep, navController)
            }

        }
    }
}
