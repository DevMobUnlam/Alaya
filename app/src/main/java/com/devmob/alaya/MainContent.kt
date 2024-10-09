package com.devmob.alaya

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devmob.alaya.domain.model.FeedbackType
import com.devmob.alaya.domain.model.IconType
import com.devmob.alaya.domain.model.ItemMenu
import com.devmob.alaya.ui.components.AppBar
import com.devmob.alaya.ui.screen.HomeScreen
import com.devmob.alaya.ui.components.BottomBarNavigation
import com.devmob.alaya.ui.screen.feedback.FeedbackScreen
import com.devmob.alaya.ui.screen.login.SreenLogin
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.AddContactScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkViewModel
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingScreen
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingViewModel
import com.devmob.alaya.ui.screen.login.LoginViewModel
import com.devmob.alaya.ui.screen.crisis_registration.CrisisRegistrationScreen
import com.devmob.alaya.utils.NavUtils
import com.devmob.alaya.utils.NavUtils.routeTitleAppBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainContent(navController: NavHostController) {
    val currentRoute = NavUtils.currentRoute(navController)
    val containmentViewModel: ContainmentNetworkViewModel = viewModel()
    val routesWithAppBar = listOf(NavUtils.Routes.RedDeContencion.route, NavUtils.Routes.AddContact.route, "contact_detail/{contactId}",NavUtils.Routes.CrisisRegistrationSummary.route)

    Scaffold(
        topBar = {
            if (currentRoute in routesWithAppBar) {
                val appBarTitle = routeTitleAppBar[currentRoute] ?: " "
                AppBar(title = appBarTitle) {
                    navController.popBackStack()
                }
            }
        },
        bottomBar = {
            //condicion para mostrar o no el bottom
            //agregar a la lista las rutas que no deberian mostrarse!!

            if (currentRoute !in listOf(NavUtils.Routes.Login.route,"nobottom", "nobottom2", NavUtils.Routes.Crisis.route,NavUtils.Routes.Feedback.route,NavUtils.Routes.CrisisRegistration.route)) {
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
            //startDestination = "login",
            startDestination = NavUtils.Routes.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavUtils.Routes.Home.route) {
                HomeScreen(navController)
            }
            composable(NavUtils.Routes.Login.route){
                SreenLogin(navController, LoginViewModel())
            }

            composable(NavUtils.Routes.Crisis.route) {
                CrisisHandlingScreen(CrisisHandlingViewModel(), navController)
            }
            composable(NavUtils.Routes.RedDeContencion.route) {
                ContainmentNetworkScreen(
                    viewModel = containmentViewModel,
                    navController = navController
                )
            }
            composable("contact_detail/{contactId}") { backStackEntry ->
                val contactId = backStackEntry.arguments?.getString("contactId")
                if (contactId != null) {
                    ContactScreen(
                        contactId = contactId,
                        viewModel = containmentViewModel,
                        navController = navController
                    )
                }
            }
            composable("add_contact") {
                AddContactScreen(containmentViewModel, navController)
            }

            composable("feedback_screen/{feedbackType}") { backStackEntry ->
                val feedbackType = backStackEntry.arguments?.getString("feedbackType")?.let {
                    FeedbackType.valueOf(it)
                }
                FeedbackScreen(feedbackType = feedbackType ?: FeedbackType.TodoVaAEstarBien,navController)
            }
            composable(NavUtils.Routes.CrisisRegistration.route) {
                CrisisRegistrationScreen(onClose = {navController.navigate(NavUtils.Routes.Home.route) {
                    popUpTo(NavUtils.Routes.Home.route) {
                        inclusive = true
                    }
                }}, onFinishedRegistration = {/*navController.navigate(NavUtils.Routes.CrisisRegistrationSummary.route)*/})
            }

            /*composable(NavUtils.Routes.CrisisRegistrationSummary.route){
                popUpTo(NavUtils.Routes.Home.route) {
                    inclusive = true
                }
            }
            */

        }
    }
}


