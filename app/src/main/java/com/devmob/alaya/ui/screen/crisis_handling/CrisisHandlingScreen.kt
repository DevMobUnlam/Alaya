package com.devmob.alaya.ui.screen.crisis_handling

import ExpandableButton
import android.speech.tts.TextToSpeech
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.devmob.alaya.R
import com.devmob.alaya.components.SegmentedProgressBar
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

    var shouldVoiceSpeak by remember{mutableStateOf(true)}
    var isVoiceOn by remember{mutableStateOf(false)}

    LaunchedEffect(currentStepIndex) {

        if (textToSpeech.isSpeaking) {
            textToSpeech.stop()
        }

        if (shouldVoiceSpeak && isVoiceOn) {
            if (isTtsInitialized) {
                if (currentStep != null) {
                    textToSpeech.speak(
                        currentStep.title,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }
                if (currentStep != null) {
                    textToSpeech.speak(
                        currentStep.description,
                        TextToSpeech.QUEUE_ADD,
                        null,
                        null
                    )
                }

            }
        }
    }



    BackHandler {}
    if (!viewModel.optionTreatmentsList.isNullOrEmpty()) {
        viewModel.loading.value = false
    }

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
                        if (textToSpeech.isSpeaking) {
                            textToSpeech.stop()
                        }
                        shouldVoiceSpeak = false
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
                        viewModel.playMusic(context)
                    }
                },
                onPauseMusic = { viewModel.pauseMusic() }, isVoiceOn = isVoiceOn,
                onMuteVoice = {
                    if (isVoiceOn) {
                        if (textToSpeech.isSpeaking) {
                            textToSpeech.stop()
                        }
                        isVoiceOn = false
                    } else {
                        isVoiceOn = true

                        if (isTtsInitialized) {

                            if (textToSpeech.isSpeaking) {
                                textToSpeech.stop()
                            }

                            if (currentStep != null) {
                                textToSpeech.speak(
                                    currentStep.title,
                                    TextToSpeech.QUEUE_ADD,
                                    null,
                                    null
                                )
                            }
                            if (currentStep != null) {
                                textToSpeech.speak(
                                    currentStep.description,
                                    TextToSpeech.QUEUE_ADD,
                                    null,
                                    null
                                )
                            }

                        }
                    }
                }
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

            SubcomposeAsyncImage(
                model = currentStep?.image,
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
                contentScale = ContentScale.Crop,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = ColorPrimary
                        )
                    }
                },
                error = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Error al cargar imagen", color = Color.Red)
                    }
                }
            )

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
                    if (textToSpeech.isSpeaking) {
                        textToSpeech.stop()
                    }

                    shouldVoiceSpeak = false
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

                    if (currentStepIndex == 2) {
                        if (textToSpeech.isSpeaking) {
                            textToSpeech.stop()
                        }
                        shouldVoiceSpeak = false
                    }

                    viewModel.nextStep()

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
                    shouldVoiceSpeak = true
                    if (isTtsInitialized && isVoiceOn) {
                        if (currentStep != null) {
                            textToSpeech.speak(
                                currentStep.title,
                                TextToSpeech.QUEUE_ADD,
                                null,
                                null
                            )
                        }
                        if (currentStep != null) {
                            textToSpeech.speak(
                                currentStep.description,
                                TextToSpeech.QUEUE_ADD,
                                null,
                                null
                            )
                        }

                    }
                })
        }
    }
}
