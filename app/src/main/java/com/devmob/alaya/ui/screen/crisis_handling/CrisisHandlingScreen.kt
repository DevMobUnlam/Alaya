package com.devmob.alaya.ui.screen.crisis_handling

import android.speech.tts.TextToSpeech
import ExpandableButton
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devmob.alaya.R
import com.devmob.alaya.components.SegmentedProgressBar
import com.devmob.alaya.domain.model.StepCrisis
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.components.TextContainer
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.NavUtils

@Composable
fun CrisisHandlingScreen(
    viewModel: CrisisHandlingViewModel,
    navController: NavController,
    textToSpeech: TextToSpeech,
    isTtsInitialized: Boolean
) {

    val shouldShowModal = viewModel.shouldShowModal
    val shouldShowExitModal = viewModel.shouldShowExitModal
    val currentStepIndex = viewModel.currentStepIndex
    val currentStep = viewModel.currentStep
    val totalSteps = viewModel.steps.size

    val isPlaying = viewModel.isPlaying
    val isVoiceOn = viewModel.isVoiceOn


    DisposableEffect(isPlaying) {
        if (isPlaying) {
            viewModel.playMusic()
        } else {
            viewModel.stopMusic()
        }
        onDispose {
            viewModel.setShouldSpeakVoice(false)
            viewModel.stopMusic()
        }
    }



    LaunchedEffect(true) {
        viewModel.setShouldSpeakVoice(true)
    }



    BackHandler {
        // Comportamiento del botón "Atrás"
    }
    if (!viewModel.optionTreatmentsList.isNullOrEmpty()) {
        viewModel.loading.value = false
    }
    viewModel.fetchCrisisSteps()

    if (viewModel.currentStep == null) {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = ColorWhite
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = ColorPrimary
                )
            }
        }
    } else {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(ColorWhite)
        ) {
            val (progressBar, audioIcon, closeIcon, lottieAnimation, title, description, nextButton, goodButton) = createRefs()

            SegmentedProgressBar(
                totalSteps = totalSteps,
                currentStep = currentStepIndex + 1,
                modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(parent.top, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )

            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = ColorText,
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        viewModel.stopTextToSpeech(textToSpeech)
                        viewModel.setShouldSpeakVoice(false)
                        viewModel.showExitModal()
                    }
                    .constrainAs(closeIcon) {
                        top.linkTo(audioIcon.top)
                        bottom.linkTo(audioIcon.bottom)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
            )

            ExpandableButton(
                modifier = Modifier.constrainAs(audioIcon) {
                    top.linkTo(progressBar.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                },
                onPlayMusic = {
                    if (isPlaying) {
                        viewModel.pauseMusic()
                    } else {
                        viewModel.playMusic()
                    }
                },
                onPauseMusic = { viewModel.pauseMusic() }, 
                isVoiceOn = isVoiceOn,
                onMuteVoice = { viewModel.onMuteVoice(textToSpeech,isTtsInitialized) },
            )

            if (currentStep != null) {
                Text(
                    text = currentStep.title,
                    color = ColorText,
                    fontSize = 24.sp,
                    fontWeight = Bold,
                    modifier = Modifier.constrainAs(title) {
                        top.linkTo(audioIcon.bottom, margin = 12.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    })
            }
            //TODO acá se reemplazó la animación de Lottie por la image de Firestore
            /*
                        val composition by rememberLottieComposition(
                            spec = LottieCompositionSpec.RawRes(
                                when (currentStep?.image) {
                                    "image_step_1" -> R.raw.crisis_step1_animation
                                    "image_step_2" -> R.raw.animation
                                    "image_step_3" -> R.raw.crisis_step3_animation
                                    else -> R.raw.feedback_congratulations_animation // Animación por defecto si no hay un paso válido
                                }
                            )
                        )*/
            /*
                        val composition by rememberLottieComposition(
                            spec = LottieCompositionSpec.Url(currentStep?.image)
                        )

                        val progress by animateLottieCompositionAsState(
                            composition = composition,
                            iterations = LottieConstants.IterateForever
                        )

                        LottieAnimation(
                            composition = composition,
                            progress = progress,
                            modifier = Modifier
                                .fillMaxWidth(0.8f) // Ajusta el ancho al 80% del tamaño de la pantalla
                                .aspectRatio(1f) // Mantén una relación de aspecto de 1:1 (cuadrada)
                                .constrainAs(lottieAnimation) {
                                    top.linkTo(title.bottom, margin = 24.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(description.top, margin = 16.dp)
                                }
                        )*/
if(!viewModel.optionTreatmentsList.isNullOrEmpty()) {
    Image(
        painter = rememberImagePainter(data = currentStep?.image),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(1f)
            .constrainAs(lottieAnimation) {
                top.linkTo(title.bottom, margin = 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(description.top, margin = 16.dp)
            },
        contentScale = ContentScale.Crop
    )
}  else {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(
            when (currentStep?.image) {
                "image_step_1" -> R.raw.crisis_step1_animation
                "image_step_2" -> R.raw.animation
                "image_step_3" -> R.raw.crisis_step3_animation
                else -> R.raw.feedback_congratulations_animation
            }
        )
    )

        val progress by animateLottieCompositionAsState(
            composition = composition,
    iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier
            .fillMaxWidth(0.8f) // Ajusta el ancho al 80% del tamaño de la pantalla
            .aspectRatio(1f) // Mantén una relación de aspecto de 1:1 (cuadrada)
            .constrainAs(lottieAnimation) {
                top.linkTo(title.bottom, margin = 24.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(description.top, margin = 16.dp)
            }
    )

}

            if (currentStep != null) {
                TextContainer(
                    currentStep.description,
                    modifier = Modifier.constrainAs(description) {
                        top.linkTo(lottieAnimation.bottom, margin = 24.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    }
                )
            }

            Button(
                stringResource(R.string.primary_button_crisis_handling),
                Modifier.constrainAs(goodButton) {
                    top.linkTo(description.bottom, margin = 12.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                },
                ButtonStyle.Outlined,
                {
                    viewModel.showModal()
                    viewModel.stopMusic()
                    viewModel.stopTextToSpeech(textToSpeech)
                    viewModel.setShouldSpeakVoice(false)
                    viewModel.showModal()
                })

            Button(
                stringResource(R.string.secondary_button_crisis_handling),
                Modifier.constrainAs(nextButton) {
                    top.linkTo(description.bottom, margin = 12.dp)
                    start.linkTo(goodButton.end, margin = 8.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                },
                ButtonStyle.Filled,
                onClick = {
                    viewModel.nextStep()
                    if (currentStepIndex == 2) {
                        viewModel.stopTextToSpeech(textToSpeech)
                        viewModel.setShouldSpeakVoice(false)
                    }else{
                        viewModel.startTextToSpeech(textToSpeech,isTtsInitialized)
                    }
                }
            )

        Modal(
            show = shouldShowModal,
            title = stringResource(R.string.title_modal_crisis_handling),
            primaryButtonText = stringResource(R.string.primary_button_modal_crisis_handling),
            secondaryButtonText = stringResource(R.string.secondary_button_modal_crisis_handling),
            onConfirm = {
                viewModel.endCrisisHandling()
                viewModel.dismissModal()
                navController.navigate(
                    NavUtils.PatientRoutes.Feedback.route.replace(
                        "{feedbackType}",
                        "Felicitaciones"
                    )
                )
            },
            onDismiss = {
                viewModel.endCrisisHandling()
                viewModel.dismissModal()
                navController.navigate(
                    NavUtils.PatientRoutes.Feedback.route.replace(
                        "{feedbackType}",
                        "TodoVaAEstarBien"
                    )
                )
            }
        )

            Modal(
                show = shouldShowExitModal,
                stringResource(R.string.title_exit_modal_crisis_handling),
                primaryButtonText = stringResource(R.string.confirm),
                secondaryButtonText = stringResource(R.string.dismiss),
                onConfirm = {
                    viewModel.stopMusic()
                    navController.navigate(NavUtils.PatientRoutes.Home.route) {
                        popUpTo(NavUtils.PatientRoutes.Home.route) {
                            inclusive = true
                        }
                    }
                },
                onDismiss = {
                    viewModel.dismissExitModal()
                    viewModel.setShouldSpeakVoice(true)
                    viewModel.startTextToSpeech(textToSpeech,isTtsInitialized)
                    
                })
        }
    }
}

@Composable
fun CrisisStepAnimationView(steps: List<StepCrisis>) {
    var currentStepIndex by remember { mutableStateOf(0) }
    val currentStep = steps.getOrNull(currentStepIndex)

    if (currentStep != null) {
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                when (currentStep.image) {
                    "image_step_1" -> R.raw.crisis_step1_animation
                    "image_step_2" -> R.raw.crisis_step2_animation
                    "image_step_3" -> R.raw.crisis_step3_animation
                    else -> R.raw.feedback_congratulations_animation
                }
            )
        )

        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )

        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f)
        )

    }
}
