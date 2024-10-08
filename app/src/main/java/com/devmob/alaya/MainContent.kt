package com.devmob.alaya

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import com.devmob.alaya.ui.screen.MenuPatientScreen
import com.devmob.alaya.ui.screen.MenuProfessionalScreen
import com.devmob.alaya.ui.screen.ProfessionalTreatment.ConfigTreatmentScreen
import com.devmob.alaya.ui.screen.ProfessionalTreatment.ConfigTreatmentViewModel
import com.devmob.alaya.ui.screen.TreatmentSummaryScreen.TreatmentSummaryScreen
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingScreen
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingViewModel
import com.devmob.alaya.ui.screen.login.LoginViewModel
import com.devmob.alaya.ui.screen.patient_profile.PatientProfileScreen
import com.devmob.alaya.ui.screen.professionalHome.ProfessionalHomeScreen
import com.devmob.alaya.ui.screen.professionalHome.ProfessionalHomeViewModel
import com.devmob.alaya.ui.screen.searchUser.SearchUserScreen
import com.devmob.alaya.ui.screen.searchUser.SearchUserViewModel
import com.devmob.alaya.utils.NavUtils
import com.devmob.alaya.utils.NavUtils.currentRoute
import com.devmob.alaya.utils.NavUtils.routeTitleAppBar

@Composable
fun MainContent(navController: NavHostController) {
    val currentRoute = currentRoute(navController)
    val containmentViewModel: ContainmentNetworkViewModel = viewModel()
    val routesWithAppBar = listOf(
        NavUtils.PatientRoutes.ContainmentNetwork.route,
        NavUtils.PatientRoutes.AddContact.route,
        "contact_detail/{contactId}"
    )

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


            if (currentRoute in NavUtils.routesWithBottomBar) {
                GetBottomBarNavigation(navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavUtils.PatientRoutes.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavUtils.PatientRoutes.Home.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                HomeScreen(navController)
            }
            composable(NavUtils.ProfessionalRoutes.Home.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
                ) {
                ProfessionalHomeScreen(ProfessionalHomeViewModel(), navController)
            }
            composable(NavUtils.ProfessionalRoutes.PatientProfile.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }

            ) {
                PatientProfileScreen(navController)
            }
            composable(NavUtils.ProfessionalRoutes.SearchPatient.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                SearchUserScreen(SearchUserViewModel(), navController)
            }
            composable(NavUtils.PatientRoutes.Login.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                SreenLogin(navController, LoginViewModel())
            }
            composable(NavUtils.PatientRoutes.Crisis.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                CrisisHandlingScreen(CrisisHandlingViewModel(), navController)
            }
            composable(NavUtils.PatientRoutes.ContainmentNetwork.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                ContainmentNetworkScreen(
                    viewModel = containmentViewModel,
                    navController = navController
                )
            }
            composable("contact_detail/{contactId}",
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) { backStackEntry ->
                val contactId = backStackEntry.arguments?.getString("contactId")
                if (contactId != null) {
                    ContactScreen(
                        contactId = contactId,
                        viewModel = containmentViewModel,
                        navController = navController
                    )
                }
            }
            composable("add_contact",
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                AddContactScreen(containmentViewModel, navController)
            }

            composable("feedback_screen/{feedbackType}",
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) { backStackEntry ->
                val feedbackType = backStackEntry.arguments?.getString("feedbackType")?.let {
                    FeedbackType.valueOf(it)
                }
                FeedbackScreen(
                    feedbackType = feedbackType ?: FeedbackType.TodoVaAEstarBien,
                    navController
                )
            }
            composable(NavUtils.ProfessionalRoutes.ConfigTreatment.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                ConfigTreatmentScreen(ConfigTreatmentViewModel(), navController)
            }
            composable(NavUtils.ProfessionalRoutes.TreatmentSummary.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) { backStackEntry ->
                val firstStep = backStackEntry.arguments?.getString("firstStep") ?: ""
                val secondStep = backStackEntry.arguments?.getString("secondStep") ?: ""
                val thirdStep = backStackEntry.arguments?.getString("thirdStep") ?: ""
                TreatmentSummaryScreen(firstStep, secondStep, thirdStep, navController)
            }
            composable(NavUtils.PatientRoutes.MenuPatient.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                MenuPatientScreen(navController)
            }
            composable(NavUtils.ProfessionalRoutes.MenuProfessional.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                MenuProfessionalScreen(navController)
            }
        }
    }
}

@Composable
fun GetBottomBarNavigation(navController: NavHostController) {
    BottomBarNavigation(
        items = listOf(
            ItemMenu(
                iconType = IconType.MENU,
                route = if (NavUtils.isPatientRoute(currentRoute(navController))) {
                    NavUtils.PatientRoutes.MenuPatient.route
                } else {
                    NavUtils.ProfessionalRoutes.MenuProfessional.route
                },
                contentDescription = "",
                order = 3
            ),
            ItemMenu(
                iconType = if (NavUtils.isPatientRoute(currentRoute(navController))) {
                    IconType.PATIENT
                } else {
                    IconType.PROFESSIONAL
                },
                route = if (NavUtils.isPatientRoute(currentRoute(navController))) {
                    NavUtils.PatientRoutes.Crisis.route
                } else {
                    NavUtils.ProfessionalRoutes.SearchPatient.route
                },
                contentDescription = "",
                order = 2
            ),
            ItemMenu(
                iconType = IconType.HOME,
                route = if (NavUtils.isPatientRoute(currentRoute(navController))) {
                    NavUtils.PatientRoutes.Home.route
                } else {
                    NavUtils.ProfessionalRoutes.Home.route
                },
                contentDescription = "",
                order = 1
            ),
        ),
        navHostController = navController
    )
}


