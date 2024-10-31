package com.devmob.alaya

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.AddUserToFirestoreUseCase
import com.devmob.alaya.domain.ContactUseCase
import com.devmob.alaya.domain.GetInvitationUseCase
import com.devmob.alaya.domain.GetRoleUseCase
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.LoginUseCase
import com.devmob.alaya.domain.RegisterNewUserUseCase
import com.devmob.alaya.domain.SaveCrisisRegistrationUseCase
import com.devmob.alaya.domain.model.FeedbackType
import com.devmob.alaya.domain.model.IconType
import com.devmob.alaya.domain.model.ItemMenu
import com.devmob.alaya.ui.ViewModelFactory
import com.devmob.alaya.ui.components.AppBar
import com.devmob.alaya.ui.screen.patient_home.PatientHomeScreen
import com.devmob.alaya.ui.components.BottomBarNavigation
import com.devmob.alaya.ui.screen.feedback.FeedbackScreen
import com.devmob.alaya.ui.screen.login.LoginScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkViewModel
import com.devmob.alaya.ui.screen.CustomActivity.CustomActivityScreen
import com.devmob.alaya.ui.screen.MenuPatientScreen
import com.devmob.alaya.ui.screen.MenuProfessionalScreen
import com.devmob.alaya.ui.screen.patient_home.PatientHomeScreenViewmodel
import com.devmob.alaya.ui.screen.ProfessionalTreatment.ConfigTreatmentScreen
import com.devmob.alaya.ui.screen.ProfessionalTreatment.ConfigTreatmentViewModel
import com.devmob.alaya.ui.screen.TreatmentSummaryScreen.TreatmentSummaryScreen
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingScreen
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingViewModel
import com.devmob.alaya.ui.screen.login.LoginViewModel
import com.devmob.alaya.ui.screen.patient_profile.PatientProfileScreen
import com.devmob.alaya.ui.screen.professionalHome.ProfessionalHomeScreen
import com.devmob.alaya.ui.screen.professionalHome.ProfessionalHomeViewModel
import com.devmob.alaya.ui.screen.register.RegisterScreen
import com.devmob.alaya.ui.screen.register.RegisterViewmodel
import com.devmob.alaya.ui.screen.searchUser.SearchUserScreen
import com.devmob.alaya.ui.screen.searchUser.SearchUserViewModel
import com.devmob.alaya.ui.screen.crisis_registration.CrisisRegistrationScreen
import com.devmob.alaya.ui.screen.crisis_registration.CrisisRegistrationSummaryScreen
import com.devmob.alaya.ui.screen.crisis_registration.CrisisRegistrationViewModel
import com.devmob.alaya.utils.NavUtils
import com.devmob.alaya.utils.NavUtils.ProfessionalRoutes
import com.devmob.alaya.utils.NavUtils.currentRoute
import com.devmob.alaya.utils.NavUtils.routeTitleAppBar

@Composable
fun MainContent(navController: NavHostController) {
    val context = LocalContext.current
    val currentRoute = currentRoute(navController)
    val contactUseCase = ContactUseCase()
    val containmentViewModel = ContainmentNetworkViewModel(contactUseCase)
    val configTreatmentViewModel: ConfigTreatmentViewModel = viewModel()
    val prefs: SharedPreferences = SharedPreferences(context)
    val routesWithAppBar = listOf(
        NavUtils.PatientRoutes.ContainmentNetwork.route,
        NavUtils.PatientRoutes.AddContact.route,
        "contact_detail/{contactId}",
        NavUtils.PatientRoutes.CrisisRegistrationSummary.route,
        ProfessionalRoutes.PatientProfile.route,
        ProfessionalRoutes.ConfigTreatment.route,
        ProfessionalRoutes.TreatmentSummary.route,
        ProfessionalRoutes.AddCustomActivity.route
    )
    val factoryCrisisRegistrationVM = ViewModelFactory { CrisisRegistrationViewModel(SaveCrisisRegistrationUseCase()) }
    val crisisRegistrationViewModel: CrisisRegistrationViewModel = viewModel(factory = factoryCrisisRegistrationVM)
    val patientHomeScreenViewmodel: PatientHomeScreenViewmodel = viewModel(
        factory = ViewModelFactory {
            PatientHomeScreenViewmodel(
                GetUserDataUseCase(),
                GetInvitationUseCase(),
                FirebaseClient()
            )
        }
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
            startDestination = NavUtils.LoginRoutes.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavUtils.PatientRoutes.Home.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) {
                PatientHomeScreen(patientHomeScreenViewmodel,navController)
            }
            composable(ProfessionalRoutes.Home.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) {
                ProfessionalHomeScreen(ProfessionalHomeViewModel(), navController)
            }
            composable(ProfessionalRoutes.PatientProfile.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }

            ) {
                PatientProfileScreen(navController)
            }
            composable(ProfessionalRoutes.SearchPatient.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) {
                SearchUserScreen(SearchUserViewModel(), navController)
            }
            composable(NavUtils.LoginRoutes.Login.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) {
                LoginScreen(navController, LoginViewModel(LoginUseCase(), GetRoleUseCase(), prefs))
            }
            composable(NavUtils.PatientRoutes.Crisis.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) {
                CrisisHandlingScreen(CrisisHandlingViewModel(), navController)
            }
            composable(NavUtils.PatientRoutes.ContainmentNetwork.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) {
                ContainmentNetworkScreen(
                    viewModel = containmentViewModel,
                    navController = navController
                )
            }
            composable("contact_detail/{contactId}",
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
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

            composable("feedback_screen/{feedbackType}",
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) { backStackEntry ->
                val feedbackType = backStackEntry.arguments?.getString("feedbackType")?.let {
                    FeedbackType.valueOf(it)
                }
                FeedbackScreen(
                    feedbackType = feedbackType ?: FeedbackType.TodoVaAEstarBien,
                    navController
                )
            }
            composable(ProfessionalRoutes.ConfigTreatment.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) {
                ConfigTreatmentScreen(configTreatmentViewModel, navController)
            }
            composable(ProfessionalRoutes.TreatmentSummary.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) { backStackEntry ->
                val firstStep = backStackEntry.arguments?.getString("firstStep") ?: ""
                val secondStep = backStackEntry.arguments?.getString("secondStep") ?: ""
                val thirdStep = backStackEntry.arguments?.getString("thirdStep") ?: ""
                TreatmentSummaryScreen(firstStep, secondStep, thirdStep, navController, configTreatmentViewModel)
            }
            composable(NavUtils.PatientRoutes.MenuPatient.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) {
                MenuPatientScreen(navController, prefs)
            }
            composable(ProfessionalRoutes.MenuProfessional.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) {
                MenuProfessionalScreen(navController, prefs)
            }
            composable(NavUtils.LoginRoutes.Register.route,
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                },
                exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End, tween(500)
                    )
                },
                popEnterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start, tween(500)
                    )
                }
            ) {
                RegisterScreen(
                    navController,
                    RegisterViewmodel(
                        RegisterNewUserUseCase(),
                        AddUserToFirestoreUseCase()
                    )
                )
            }
            composable(NavUtils.PatientRoutes.CrisisRegistration.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                CrisisRegistrationScreen(onClose = {navController.navigate(NavUtils.PatientRoutes.Home.route) {
                    popUpTo(NavUtils.PatientRoutes.Home.route) {
                        inclusive = true
                    }
                }},
                    onFinishedRegistration = {navController.navigate(NavUtils.PatientRoutes.CrisisRegistrationSummary.route) {
                        popUpTo(NavUtils.PatientRoutes.CrisisRegistrationSummary.route) {
                            inclusive = true
                        }
                    }}, viewModel = crisisRegistrationViewModel, navController = navController)
            }
            composable(NavUtils.PatientRoutes.CrisisRegistrationSummary.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                CrisisRegistrationSummaryScreen(navController = navController, viewModel = crisisRegistrationViewModel)
            }
            composable(NavUtils.ProfessionalRoutes.AddCustomActivity.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                CustomActivityScreen(navController = navController, viewModel = configTreatmentViewModel )
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
                    ProfessionalRoutes.MenuProfessional.route
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
                    ProfessionalRoutes.SearchPatient.route
                },
                contentDescription = "",
                order = 2
            ),
            ItemMenu(
                iconType = IconType.HOME,
                route = if (NavUtils.isPatientRoute(currentRoute(navController))) {
                    NavUtils.PatientRoutes.Home.route
                } else {
                    ProfessionalRoutes.Home.route
                },
                contentDescription = "",
                order = 1
            ),
        ),
        navHostController = navController
    )
}
            /*composable(NavUtils.Routes.CrisisRegistration.route) {
                CrisisRegistrationScreen(onClose = {navController.navigate(NavUtils.Routes.Home.route) {
                    popUpTo(NavUtils.Routes.Home.route) {
                        inclusive = true
                    }
                }},
                    onFinishedRegistration = {navController.navigate(NavUtils.Routes.CrisisRegistrationSummary.route) {
                        popUpTo(NavUtils.Routes.CrisisRegistrationSummary.route) {
                            inclusive = true
                        }
                    }})
            }

            composable(NavUtils.Routes.CrisisRegistrationSummary.route){
                CrisisRegistrationSummaryScreen(navController = navController)
            }*/



