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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
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
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            when (feedbackType) {
                FeedbackType.Felicitaciones -> {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.felicidades),
                        color = ColorText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 36.sp,
                            fontWeight = Bold
                        ),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text(
                        text = stringResource(R.string.lograste_superar_este_momento_segu_as),
                        color = ColorText,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.clap
                        )
                    )
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
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Button(
                            text = stringResource(R.string.registrar_el_episodio),
                            onClick = {
                                navController.navigate(NavUtils.PatientRoutes.CrisisRegistration.route)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            style = ButtonStyle.Filled,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            text = stringResource(R.string.registrar_el_episodio_m_s_tarde),
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
                    Text(
                        text = stringResource(R.string.todo_va_a_estar_bien),
                        color = ColorText,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 36.sp,
                            fontWeight = Bold
                        ),
                        modifier = Modifier.padding(bottom = 17.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.est_bien_lo_importante_es_que_lo_intentaste),
                        color = ColorText,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    val composition by rememberLottieComposition(
                        spec = LottieCompositionSpec.RawRes(
                            R.raw.everything_will_be_fine_animation
                        )
                    )
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
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        Button(
                            text = stringResource(R.string.mi_red_de_contenci_n),
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
                            text = stringResource(R.string.ir_a_inicio),
                            style = ButtonStyle.Outlined
                        )
                    }
                }
            }
        }
    }
}