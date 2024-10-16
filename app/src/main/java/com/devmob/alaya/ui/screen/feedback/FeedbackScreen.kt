package com.devmob.alaya.ui.screen.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.devmob.alaya.ui.components.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.FeedbackType
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.utils.NavUtils


@Composable
fun FeedbackScreen(
    feedbackType: FeedbackType,
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        when (feedbackType) {
            FeedbackType.Felicitaciones -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¡Felicidades!",
                    color = ColorText,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 50.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
//
                val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.feedback_congratulations_animation))
                var isPlaying by remember {
                    mutableStateOf(true)
                }
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
                LottieAnimation(
                    composition = composition,
                    progress = {
                        progress
                    }
                )

                Column {
                    Button(
                        text = "Registrar el episodio",
                        onClick = {
                            navController.navigate(NavUtils.PatientRoutes.CrisisRegistration.route)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        style = ButtonStyle.Filled,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        text = "Registrar el episodio más tarde",
                        onClick = {
                            navController.navigate(NavUtils.PatientRoutes.Home.route) {
                                popUpTo(NavUtils.PatientRoutes.Home.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        style = ButtonStyle.Outlined
                    )
                }
            }

            FeedbackType.TodoVaAEstarBien -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¡Todo va a estar bien!",
                    color = ColorText,
                    fontSize = 35.sp,
                    fontWeight = Bold,
                    modifier = Modifier.padding(bottom = 17.dp)
                )
//
                val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.everything_will_be_fine_animation))
                var isPlaying by remember {
                    mutableStateOf(true)
                }
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )
                LottieAnimation(
                    composition = composition,
                    progress = {
                        progress
                    }
                )

                Column {
                    Button(
                        text = "Mi red de contención",
                        onClick = {
                            navController.navigate(NavUtils.PatientRoutes.ContainmentNetwork.route) {}
                        },
                        style = ButtonStyle.Filled,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            navController.navigate(NavUtils.PatientRoutes.Home.route) {
                                popUpTo(NavUtils.PatientRoutes.Home.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = "Ir a inicio",
                        style = ButtonStyle.Outlined
                    )
                }
            }
        }
    }
}

