package com.devmob.alaya.navigation.ProfessionalNavigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.model.IconType
import com.devmob.alaya.domain.model.ItemMenu
import com.devmob.alaya.ui.components.BottomBarNavigation
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactViewModel
import com.devmob.alaya.ui.screen.ProfessionalTreatment.ConfigTreatmentScreen
import com.devmob.alaya.ui.screen.ProfessionalTreatment.ConfigTreatmentViewModel
import com.devmob.alaya.ui.screen.TreatmentSummaryScreen.TreatmentSummaryScreen
import com.devmob.alaya.ui.screen.patient_profile.PatientProfileScreen
import com.devmob.alaya.ui.screen.patient_profile.PatientIASummaryViewModel
import com.devmob.alaya.ui.screen.professionalHome.ProfessionalHomeScreen
import com.devmob.alaya.ui.screen.professionalHome.ProfessionalHomeViewModel
import com.devmob.alaya.ui.screen.searchUser.SearchUserScreen
import com.devmob.alaya.ui.screen.searchUser.SearchUserViewModel
import com.devmob.alaya.utils.NavUtils

@Composable
fun ProfessionalNavigation(navController: NavHostController){
    val currentRoute = NavUtilsProfessional.currentRoute(navController)

    val contactViewModel: ContactViewModel = hiltViewModel()
    val patientIASummaryViewModel: PatientIASummaryViewModel = hiltViewModel()

    val getUserDataUseCase = GetUserDataUseCase();
    val searchUserViewModel = SearchUserViewModel(getUserDataUseCase)

    Scaffold(
        bottomBar = {
            //condicion para mostrar o no el bottom
            //agregar a la lista las rutas que no deberian mostrarse!!
            if (currentRoute !in listOf("nobottom", "nobottom2")) {
                BottomBarNavigation(
                    items = listOf(
                        ItemMenu(iconType = IconType.MENU, route = "menu", contentDescription = "menu", order = 3),
                        ItemMenu(iconType = IconType.PROFESSIONAL, route = NavUtils.ProfessionalRoutes.Home.route, contentDescription = "Home profesional", order = 2),
                        ItemMenu(iconType = IconType.HOME, route = NavUtils.PatientRoutes.Home.route, contentDescription = "boton de inicio", order = 1),
                    ),
                    navHostController = navController
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavUtils.ProfessionalRoutes.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavUtils.ProfessionalRoutes.Home.route) {
                ProfessionalHomeScreen(ProfessionalHomeViewModel(getUserDataUseCase), navController)
            }
            composable(NavUtils.ProfessionalRoutes.PatientProfile.route){
                PatientProfileScreen(navController)
            }
            composable(NavUtils.ProfessionalRoutes.PatientProfile.route){
                SearchUserScreen(searchUserViewModel, navController)
            }
            composable(NavUtils.ProfessionalRoutes.ConfigTreatment.route){
                ConfigTreatmentScreen(ConfigTreatmentViewModel(), navController)
            }
         /*   composable(NavUtils.ProfessionalRoutes.TreatmentSummary.route) { backStackEntry ->
                val firstStep = backStackEntry.arguments?.getString("firstStep") ?: ""
                val secondStep = backStackEntry.arguments?.getString("secondStep") ?: ""
                val thirdStep = backStackEntry.arguments?.getString("thirdStep") ?: ""

                TreatmentSummaryScreen(firstStep, secondStep, thirdStep, navController)
            }*/

        }
    }
}
