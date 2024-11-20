package com.devmob.alaya

import android.speech.tts.TextToSpeech
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.devmob.alaya.data.ContactRepositoryImpl
import com.devmob.alaya.data.CrisisRepositoryImpl
import com.devmob.alaya.data.CrisisTreatmentRepositoryImpl
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.data.LoginRepositoryImpl
import com.devmob.alaya.data.NotificationRepositoryImpl
import com.devmob.alaya.data.NotificationService
import com.devmob.alaya.data.RegisterNewUserRepositoryImpl
import com.devmob.alaya.data.UploadImageToFirestoreRepositoryImpl
import com.devmob.alaya.data.UserFirestoreRepositoryImpl
import com.devmob.alaya.data.local_storage.CrisisStepsDatabase
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.AddUserToFirestoreUseCase
import com.devmob.alaya.domain.ContactUseCase
import com.devmob.alaya.domain.GetCrisisTreatmentUseCase
import com.devmob.alaya.domain.GetInvitationUseCase
import com.devmob.alaya.domain.GetRoleUseCase
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.LoginUseCase
import com.devmob.alaya.domain.RegisterNewUserUseCase
import com.devmob.alaya.domain.SaveCrisisRegistrationUseCase
import com.devmob.alaya.domain.SaveCrisisTreatmentUseCase
import com.devmob.alaya.domain.UploadImageToFirestoreUseCase
import com.devmob.alaya.domain.model.FeedbackType
import com.devmob.alaya.domain.model.IconType
import com.devmob.alaya.domain.model.ItemMenu
import com.devmob.alaya.ui.ViewModelFactory
import com.devmob.alaya.ui.components.AppBar
import com.devmob.alaya.ui.components.BottomBarNavigation
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkScreen
import com.devmob.alaya.ui.screen.ContainmentNetwork.ContainmentNetworkViewModel
import com.devmob.alaya.ui.screen.CustomActivity.CustomActivityScreen
import com.devmob.alaya.ui.screen.MenuPatientScreen
import com.devmob.alaya.ui.screen.MenuProfessionalScreen
import com.devmob.alaya.ui.screen.TreatmentSummaryScreen.TreatmentSummaryScreen
import com.devmob.alaya.ui.screen.activityDay.ActivityDayScreen
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingScreen
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingViewModel
import com.devmob.alaya.ui.screen.crisis_registration.CrisisRegistrationScreen
import com.devmob.alaya.ui.screen.crisis_registration.CrisisRegistrationSummaryScreen
import com.devmob.alaya.ui.screen.crisis_registration.CrisisRegistrationViewModel
import com.devmob.alaya.ui.screen.feedback.FeedbackScreen
import com.devmob.alaya.ui.screen.login.LoginScreen
import com.devmob.alaya.ui.screen.login.LoginViewModel
import com.devmob.alaya.ui.screen.patient_home.PatientHomeScreen
import com.devmob.alaya.ui.screen.patient_home.PatientHomeScreenViewmodel
import com.devmob.alaya.ui.screen.patient_profile.PatientIASummaryScreen
import com.devmob.alaya.ui.screen.patient_profile.PatientProfileScreen
import com.devmob.alaya.ui.screen.patient_profile.PatientProfileViewModel
import com.devmob.alaya.ui.screen.professionalCrisisTreatment.ConfigTreatmentScreen
import com.devmob.alaya.ui.screen.professionalCrisisTreatment.ConfigTreatmentViewModel
import com.devmob.alaya.ui.screen.professionalHome.ProfessionalHomeScreen
import com.devmob.alaya.ui.screen.professionalHome.ProfessionalHomeViewModel
import com.devmob.alaya.ui.screen.register.RegisterScreen
import com.devmob.alaya.ui.screen.register.RegisterViewmodel
import com.devmob.alaya.ui.screen.searchUser.SearchUserScreen
import com.devmob.alaya.ui.screen.searchUser.SearchUserViewModel
import com.devmob.alaya.ui.screen.send_invitation_screen.SendInvitationScreen
import com.devmob.alaya.ui.screen.send_invitation_screen.SendInvitationViewModel
import com.devmob.alaya.ui.screen.patient_profile.PatientIASummaryScreen
import com.devmob.alaya.utils.CrisisStepsManager
import com.devmob.alaya.utils.NavUtils
import com.devmob.alaya.utils.NavUtils.ProfessionalRoutes
import com.devmob.alaya.utils.NavUtils.currentRoute
import com.devmob.alaya.utils.NavUtils.routeTitleAppBar

@Composable
fun MainContent(
    navController: NavHostController,
    textToSpeech: TextToSpeech,
    isTtsInitialized: Boolean
) {
    val context = LocalContext.current
    val prefs = SharedPreferences(context)
    val currentRoute = currentRoute(navController)

    val firebaseClient = FirebaseClient()
    val getUserRepository = GetUserRepositoryImpl(firebaseClient)
    val notificationRepository = NotificationRepositoryImpl(NotificationService())
    val contactUseCase = ContactUseCase(
        prefs,
        ContactRepositoryImpl(firebaseClient),
        getUserRepository
    )
    val getInvitationUseCase = GetInvitationUseCase(
        getUserRepository,
        notificationRepository
    )
    val containmentViewModel = ContainmentNetworkViewModel(contactUseCase)
    val sendInvitationViewModel = SendInvitationViewModel(getInvitationUseCase)
    val getUserDataUseCase = GetUserDataUseCase(getUserRepository)
    val searchUserViewModel = SearchUserViewModel(getUserDataUseCase)
    val crisisStepsDao = CrisisStepsDatabase.getDataBase(context).crisisStepsDao()
    val crisisRepository = CrisisRepositoryImpl(firebaseClient)
    val saveCrisisRegistrationUseCase = SaveCrisisRegistrationUseCase(crisisRepository)
    val crisisTreatmentRepository = CrisisTreatmentRepositoryImpl(firebaseClient)
    val uploadImageToFirestoreRepository = UploadImageToFirestoreRepositoryImpl(firebaseClient)
    val uploadImageToFirestoreUseCase = UploadImageToFirestoreUseCase(
        uploadImageToFirestoreRepository,
        prefs
    )
    val saveCrisisTreatmentUseCase = SaveCrisisTreatmentUseCase(
        crisisTreatmentRepository,
        uploadImageToFirestoreUseCase
    )
    val userRepository = GetUserRepositoryImpl(firebaseClient)
    val loginRepository = LoginRepositoryImpl(firebaseClient)
    val loginUseCase = LoginUseCase(loginRepository)
    val getRoleUseCase = GetRoleUseCase(getUserRepository)
    val getCrisisTreatmentUseCase = GetCrisisTreatmentUseCase(
        prefs,
        userRepository,
        crisisStepsDao
    )
    val registerNewUserRepository = RegisterNewUserRepositoryImpl(firebaseClient)
    val registerNewUserUseCase = RegisterNewUserUseCase(registerNewUserRepository)
    val userFirestoreRepository = UserFirestoreRepositoryImpl(firebaseClient)
    val addUserToFirestoreUseCase = AddUserToFirestoreUseCase(userFirestoreRepository)
    val crisisStepsManager = CrisisStepsManager(crisisStepsDao, getCrisisTreatmentUseCase, context)

    val routesWithAppBar = listOf(
        NavUtils.PatientRoutes.ContainmentNetwork.route,
        NavUtils.PatientRoutes.AddContact.route,
        "contact_detail/{contactId}",
        NavUtils.PatientRoutes.CrisisRegistrationSummary.route,
        ProfessionalRoutes.PatientProfile.route,
        "patient_profile/{email}",
        NavUtils.PatientRoutes.ActivityDay.route,
        ProfessionalRoutes.ConfigTreatment.route,
        ProfessionalRoutes.TreatmentSummary.route,
        ProfessionalRoutes.AddCustomActivity.route,
        ProfessionalRoutes.TreatmentSummary.route,
        ProfessionalRoutes.AddCustomActivity.route,
        ProfessionalRoutes.SendInvitation.route
    )
    val factoryCrisisRegistrationVM = ViewModelFactory {
        CrisisRegistrationViewModel(saveCrisisRegistrationUseCase)
    }
    val crisisRegistrationViewModel: CrisisRegistrationViewModel =
        viewModel(factory = factoryCrisisRegistrationVM)
    val patientHomeScreenViewmodel: PatientHomeScreenViewmodel = viewModel(
        factory = ViewModelFactory {
            PatientHomeScreenViewmodel(
                getUserDataUseCase,
                getInvitationUseCase,
                firebaseClient,
                crisisStepsManager
            )
        }
    )
    val configTreatmentViewModel: ConfigTreatmentViewModel = viewModel(
        factory = ViewModelFactory {
            ConfigTreatmentViewModel(saveCrisisTreatmentUseCase)
        }
    )

    val patientProfileViewModel: PatientProfileViewModel =
        viewModel(factory = ViewModelFactory { PatientProfileViewModel(getUserDataUseCase) })
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
                PatientHomeScreen(patientHomeScreenViewmodel, navController)
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
                ProfessionalHomeScreen(ProfessionalHomeViewModel(getUserDataUseCase, firebaseClient), navController)
            }
            composable(
                route = "${ProfessionalRoutes.PatientProfile.route}/{email}",
                arguments = listOf(navArgument("email") { type = NavType.StringType }),
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
                val email = backStackEntry.arguments?.getString("email")
                PatientProfileScreen(navController, patientProfileViewModel, email = email ?: "")
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
                SearchUserScreen(searchUserViewModel, navController)
            }
            composable(ProfessionalRoutes.PatientIASummary.route,
                enterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
                exitTransition = { return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
                popEnterTransition = { return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) }
            ) {
                PatientIASummaryScreen()
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
                LoginScreen(navController, LoginViewModel(loginUseCase, getRoleUseCase, prefs))
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
                CrisisHandlingScreen(
                    CrisisHandlingViewModel(
                        saveCrisisRegistrationUseCase,
                        getCrisisTreatmentUseCase
                    ),
                    navController,
                    textToSpeech,
                    isTtsInitialized
                )

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
            //Pantalla actividades diarias
            composable(NavUtils.PatientRoutes.ActivityDay.route) {
                ActivityDayScreen()
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
            ) { backStackEntry ->
                val patientEmail = backStackEntry.arguments?.getString("patientEmail") ?: ""
                ConfigTreatmentScreen(
                    patientEmail = patientEmail,
                    configTreatmentViewModel,
                    navController
                )
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
                val patientEmail = backStackEntry.arguments?.getString("patientEmail") ?: ""
                TreatmentSummaryScreen(
                    firstStep,
                    secondStep,
                    thirdStep,
                    patientEmail,
                    navController,
                    configTreatmentViewModel
                )
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
                        registerNewUserUseCase,
                        addUserToFirestoreUseCase
                    )
                )
            }
            composable(NavUtils.PatientRoutes.CrisisRegistration.route,
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
                CrisisRegistrationScreen(onClose = {
                    navController.navigate(NavUtils.PatientRoutes.Home.route) {
                        popUpTo(NavUtils.PatientRoutes.Home.route) {
                            inclusive = false
                        }
                    }
                },
                    onFinishedRegistration = { navController.navigate(NavUtils.PatientRoutes.CrisisRegistrationSummary.route) },
                    viewModel = crisisRegistrationViewModel,
                    navController = navController
                )
            }
            composable(NavUtils.PatientRoutes.CrisisRegistrationSummary.route,
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
                CrisisRegistrationSummaryScreen(
                    navController = navController,
                    viewModel = crisisRegistrationViewModel
                )
            }
            composable(ProfessionalRoutes.AddCustomActivity.route,
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
                val patientEmail = backStackEntry.arguments?.getString("patientEmail") ?: ""
                CustomActivityScreen(
                    patientEmail = patientEmail,
                    navController = navController,
                    viewModel = configTreatmentViewModel
                )
            }

            composable(ProfessionalRoutes.SendInvitation.route,
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
                SendInvitationScreen(sendInvitationViewModel)
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

