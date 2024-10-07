package com.devmob.alaya

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devmob.alaya.domain.model.IconType
import com.devmob.alaya.domain.model.ItemMenu
import com.devmob.alaya.ui.screen.HomeScreen
import com.devmob.alaya.ui.components.BottomBarNavigation
import com.devmob.alaya.ui.screen.ContainmentNetwork.AddContactScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContactScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkViewModel
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingScreen
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingViewModel
import com.devmob.alaya.utils.NavUtils

@Composable
fun MainContent(navController: NavHostController) {
    val currentRoute = NavUtils.currentRoute(navController)
    val ContainmentViewModel: ContainmentNetworkViewModel = viewModel()
    Scaffold(
        bottomBar = {
            //condicion para mostrar o no el bottom
            //agregar a la lista las rutas que no deberian mostrarse!!
            if (currentRoute !in listOf("nobottom", "nobottom2")) {
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
            composable(NavUtils.Routes.RedDeContencion.route) {
                ContainmentNetworkScreen(
                    viewModel = ContainmentViewModel,
                    navController = navController
                )
            }
            composable("contact_detail/{contactId}") { backStackEntry ->
                val contactId = backStackEntry.arguments?.getString("contactId")
                if (contactId != null) {
                    ContactScreen(
                        contactId = contactId,
                        viewModel = ContainmentViewModel,
                        navController = navController
                    )
                }
            }
            composable("add_contact") {
                AddContactScreen(ContainmentViewModel, navController)
            }
        }
    }
}


