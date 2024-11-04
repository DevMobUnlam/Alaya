package com.devmob.alaya.ui.screen.crisis_handling

import ExpandableButton
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devmob.alaya.R
import com.devmob.alaya.components.SegmentedProgressBar
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.components.TextContainer
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.NavUtils

@Composable
fun CrisisHandlingScreen(viewModel: CrisisHandlingViewModel, navController: NavController) {
    val currentStep = viewModel.currentStep
    val shouldShowModal = viewModel.shouldShowModal
    val shouldShowExitModal = viewModel.shouldShowExitModal
    val totalSteps = viewModel.steps.size
    val currentStepIndex = viewModel.currentStepIndex

    val context = LocalContext.current
    val isPlaying = viewModel.isPlaying


    DisposableEffect(isPlaying) {
        if (isPlaying) {
            viewModel.playMusic(context)
        } else {
            viewModel.stopMusic()
        }
        onDispose {
            viewModel.stopMusic()
        }
    }
    BackHandler {
        // Comportamiento del botón "Atrás"
    }

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
                .clickable { viewModel.showExitModal() }
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
            onPlayMusic = {   if (isPlaying) {
                viewModel.pauseMusic()
            } else {
                viewModel.playMusic(context)
            }
            },
            onPauseMusic = { viewModel.pauseMusic()}
        )

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

        // Reemplazo de la imagen con la animación de Lottie
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(
                when (currentStep.image) {
                    "image_step_1" -> R.raw.crisis_step1_animation
                    "image_step_2" -> R.raw.animation
                    "image_step_3" -> R.raw.crisis_step3_animation
                    else -> R.raw.feedback_congratulations_animation // Animación por defecto si no hay un paso válido
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

        TextContainer(
            currentStep.description,
            modifier = Modifier.constrainAs(description) {
                top.linkTo(lottieAnimation.bottom, margin = 24.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        )

        Button(
            stringResource(R.string.primary_button_crisis_handling),
            Modifier.constrainAs(goodButton) {
                top.linkTo(description.bottom, margin = 12.dp)
                start.linkTo(parent.start, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            ButtonStyle.Outlined,
            { viewModel.showModal()
                viewModel.stopMusic()})

        Button(
            stringResource(R.string.secondary_button_crisis_handling),
            Modifier.constrainAs(nextButton) {
                top.linkTo(description.bottom, margin = 12.dp)
                start.linkTo(goodButton.end, margin = 8.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            ButtonStyle.Filled,
            onClick = { viewModel.nextStep() })

        Modal(
            show = shouldShowModal,
            title = stringResource(R.string.title_modal_crisis_handling),
            primaryButtonText = stringResource(R.string.primary_button_modal_crisis_handling),
            secondaryButtonText = stringResource(R.string.secondary_button_modal_crisis_handling),
            onConfirm = {
                viewModel.dismissModal()
                navController.navigate(
                    NavUtils.PatientRoutes.Feedback.route.replace(
                        "{feedbackType}",
                        "Felicitaciones"
                    ))
            },
            onDismiss = {
                viewModel.dismissModal()
                navController.navigate(
                    NavUtils.PatientRoutes.Feedback.route.replace(
                        "{feedbackType}",
                        "TodoVaAEstarBien"
                    )
                )
                viewModel.stopMusic()
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
                viewModel.dismissExitModal() })
    }
}

@Preview
@Composable
fun CrisisHandlingScreenPreview() {
    CrisisHandlingScreen(CrisisHandlingViewModel(), rememberNavController())
}