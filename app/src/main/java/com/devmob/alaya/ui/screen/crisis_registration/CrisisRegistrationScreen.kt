package com.devmob.alaya.ui.screen.crisis_registration

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Preview
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.SentimentVeryDissatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.devmob.alaya.R
import com.devmob.alaya.components.SegmentedProgressBar
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.domain.model.Intensity
import com.devmob.alaya.ui.components.CrisisRegisterIconButton
import com.devmob.alaya.ui.components.DateTimePicker
import com.devmob.alaya.ui.components.IconButtonWithIntensity
import com.devmob.alaya.ui.components.IconButtonNoFill
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.components.NewCrisisElementCard
import com.devmob.alaya.ui.components.TextArea
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.NavUtils

@Composable
fun CrisisRegistrationScreen(
    viewModel: CrisisRegistrationViewModel,
    onClose: () -> Unit,
    onFinishedRegistration: () -> Unit,
    navController: NavHostController,
) {
    val screenState = viewModel.screenState.observeAsState()
    val shouldShowExitModal = viewModel.shouldShowExitModal
    val messageTextSize = 30.sp
    val horizontalMargin = 15.dp
    var shouldShowAddNewCard by remember { mutableStateOf(false) }
    val places by viewModel.places.observeAsState(emptyList())
    var selectedPlace by remember { mutableStateOf<String?>(null) }
    val tools by viewModel.tools.observeAsState(emptyList())
    var selectedTools by remember { mutableStateOf<Set<String>>(emptySet()) }
    val bodySensations by viewModel.bodySensations.observeAsState(emptyList())
    var selectedBodySensations by remember { mutableStateOf<Map<String, Intensity>>(emptyMap()) }
    val emotions by viewModel.emotions.observeAsState(emptyList())
    var selectedEmotions by remember { mutableStateOf<Set<String>>(emptySet()) }

    BackHandler {
        when(screenState.value?.currentStep){
            1 -> {
                viewModel.shouldShowExitModal = true
            }
            else -> if(viewModel.shouldGoToSummary && !viewModel.shouldGoToBack) navController.navigate(NavUtils.PatientRoutes.CrisisRegistrationSummary.route) else viewModel.goOneStepBack()
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorWhite)
    ) {
        val (progressBar, datePickerComponent, closeIcon, title, backArrow, forwardArrow) = createRefs()
        val (elementsGrid, addNewIcon, newElementsCard, addMoreStep) = createRefs()

        SegmentedProgressBar(
            totalSteps = screenState.value!!.totalSteps,
            currentStep = screenState.value!!.currentStep,
            modifier = Modifier.constrainAs(progressBar) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        when (screenState.value?.currentStep) {
            1 -> {
                Text(
                    text = "¿En qué momento comenzó?",
                    fontSize = messageTextSize,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    modifier = Modifier
                        .constrainAs(title) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(closeIcon.bottom, margin = 1.dp)
                        }
                )
                viewModel.screenState.value?.crisisDetails?.crisisTimeDetails?.let { time ->
                    DateTimePicker(
                        modifier = Modifier.constrainAs(datePickerComponent) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(title.bottom)
                        },
                        onStartDateChange = { newDate ->
                            viewModel.updateStartDate(newDate)
                        },
                        onStartTimeChange = { newTime ->
                            viewModel.updateStartDate(newTime)
                        },
                        onEndDateChange = { newDate ->
                            viewModel.updateEndDate(newDate)
                        },
                        onEndTimeChange = { newTime ->
                            viewModel.updateEndDate(newTime)
                        },
                        crisisTimeDetails = time
                    )
                }
            }

            2 -> {
                val icon = Icons.Filled.LocationOn
                Text(
                    text = "¿Donde estabas?",
                    fontSize = messageTextSize,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier
                        .constrainAs(title) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(progressBar.bottom, margin = 20.dp)
                        }
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.constrainAs(elementsGrid) {
                        top.linkTo(title.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                ) {
                    items(places) { place ->
                        val isSelected =
                            if (viewModel.screenState.value?.crisisDetails?.placeList?.isEmpty() == true) {
                                place.name == selectedPlace
                            } else {
                                viewModel.screenState.value?.crisisDetails?.placeList?.get(0)?.name == place.name
                            }
                        CrisisRegisterIconButton(
                            imageVector = place.icon,
                            size = 70.dp,
                            text = place.name,
                            isSelected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    selectedPlace = ""
                                    viewModel.clearPlaceSelection()
                                } else {
                                    selectedPlace = place.name
                                    viewModel.updatePlace(place, 0)
                                }
                            }
                        )
                    }
                }
                IconButtonNoFill(
                    text = "Agregar otra ubicacion",
                    modifier = Modifier.constrainAs(addNewIcon) {
                        top.linkTo(elementsGrid.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onClick = { shouldShowAddNewCard = !shouldShowAddNewCard }
                )
                AnimatedVisibility(
                    visible = shouldShowAddNewCard,
                    modifier = Modifier.constrainAs(newElementsCard) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }) {
                    NewCrisisElementCard(
                        placeholderText = "Agrega otro lugar...",
                        icon = icon,
                        onSave = {
                            viewModel.addCrisisPlace(
                                CrisisPlace(
                                    name = it,
                                    icon = icon
                                )
                            )
                            shouldShowAddNewCard = !shouldShowAddNewCard
                        },
                        onCancel = { shouldShowAddNewCard = false}
                    )
                }
            }

            3 -> {
                val icon = Icons.Filled.Accessibility
                Text(
                    text = "¿Que sensaciones\ncorporales sentiste?",
                    fontSize = messageTextSize,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier
                        .constrainAs(title) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(progressBar.bottom, margin = 20.dp)
                        }
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.constrainAs(elementsGrid) {
                        top.linkTo(title.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                ) {
                    items(bodySensations) { bodySensation ->
                        val isSelected: Boolean =
                            if (viewModel.screenState.value?.crisisDetails?.bodySensationList?.isEmpty() == true) {
                                selectedBodySensations.contains(bodySensation.name)
                            } else {
                                viewModel.screenState.value?.crisisDetails?.bodySensationList?.any { it.name == bodySensation.name }
                                    ?: false
                            }
                        val intensity =
                            viewModel.screenState.value?.crisisDetails?.bodySensationList?.find { it.name == bodySensation.name }?.intensity
                                ?: bodySensation.intensity
                        IconButtonWithIntensity(
                            symbol = bodySensation.icon,
                            text = bodySensation.name,
                            size = 70.dp,
                            isActive = isSelected,
                            intensity = intensity,
                            onClick = {
                                if (isSelected) {
                                    selectedBodySensations =
                                        selectedBodySensations - bodySensation.name
                                    viewModel.unselectCrisisBodySensation(bodySensation)
                                } else {
                                    selectedBodySensations.plus(bodySensation.name to intensity)
                                    viewModel.selectCrisisBodySensation(bodySensation)
                                }
                            },
                            onChangedIntensity = {
                                if (isSelected) {
                                    selectedBodySensations.plus(bodySensation.name to it)
                                    viewModel.updateIntensityBodySensation(bodySensation, it)
                                }
                            }
                        )
                    }
                }
                IconButtonNoFill(
                    text = "Añadir \n sensación \n corporal",
                    modifier = Modifier.constrainAs(addNewIcon) {
                        top.linkTo(elementsGrid.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onClick = { shouldShowAddNewCard = !shouldShowAddNewCard }
                )
                AnimatedVisibility(
                    visible = shouldShowAddNewCard,
                    modifier = Modifier.constrainAs(newElementsCard) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }) {
                    NewCrisisElementCard(
                        placeholderText = "Agrega otra sensacion...",
                        icon = icon,
                        onSave = {
                            viewModel.addCrisisBodySensation(
                                CrisisBodySensation(
                                    name = it,
                                    icon = icon
                                )
                            )
                            shouldShowAddNewCard = !shouldShowAddNewCard
                        },
                        onCancel = { shouldShowAddNewCard = false}
                    )
                }
            }

            4 -> {
                val icon = Icons.Filled.SentimentNeutral
                Text(
                    text = "¿Qué emociones sentiste?",
                    fontSize = messageTextSize,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier
                        .constrainAs(title) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(progressBar.bottom, margin = 30.dp)
                        }
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.constrainAs(elementsGrid) {
                        top.linkTo(title.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                ) {
                    items(emotions) { emotion ->
                        val isSelected =
                            if (viewModel.screenState.value?.crisisDetails?.emotionList?.isEmpty() == true) {
                                selectedEmotions.contains(emotion.name)
                            } else {
                                viewModel.screenState.value?.crisisDetails?.emotionList?.any { it.name == emotion.name }
                                    ?: false
                            }
                        val intensity =
                            viewModel.screenState.value?.crisisDetails?.emotionList?.find { it.name == emotion.name }?.intensity
                                ?: emotion.intensity
                        IconButtonWithIntensity(
                            symbol = emotion.icon,
                            text = emotion.name,
                            size = 70.dp,
                            isActive = isSelected,
                            intensity = intensity,
                            onClick = {
                                if (isSelected) {
                                    selectedEmotions =
                                        selectedEmotions - emotion.name
                                    viewModel.unselectCrisisEmotion(emotion)
                                } else {
                                    selectedEmotions.plus(emotion.name)
                                    viewModel.selectCrisisEmotion(emotion)
                                }
                            },
                            onChangedIntensity = {
                                if (isSelected) {
                                    selectedEmotions.plus(emotion.name to it)
                                    viewModel.updateIntensityEmotion(emotion, it)
                                }
                            }

                        )
                    }
                }
                IconButtonNoFill(
                    text = "Añadir \n emoción",
                    modifier = Modifier.constrainAs(addNewIcon) {
                        top.linkTo(elementsGrid.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onClick = { shouldShowAddNewCard = !shouldShowAddNewCard }
                )
                AnimatedVisibility(
                    visible = shouldShowAddNewCard,
                    modifier = Modifier.constrainAs(newElementsCard) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }) {
                    NewCrisisElementCard(
                        placeholderText = "Agrega otra emoción...",
                        icon = icon,
                        onSave = {
                            viewModel.addCrisisEmotion(
                                CrisisEmotion(
                                    name = it,
                                    icon = icon
                                )
                            )
                            shouldShowAddNewCard = !shouldShowAddNewCard
                        },
                        onCancel = { shouldShowAddNewCard = false }
                    )
                }
            }

            5 -> {
                val icon = Icons.Filled.ChangeHistory
                Text(
                    text = "¿Qué herramientas usaste para calmarte?",
                    fontSize = messageTextSize,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier
                        .constrainAs(title) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(progressBar.bottom, margin = 30.dp)
                        }
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.constrainAs(elementsGrid) {
                        top.linkTo(title.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                ) {
                    items(tools) { tool ->
                        val isSelected =
                            if (viewModel.screenState.value?.crisisDetails?.toolList?.isEmpty() == true) {
                                selectedTools.contains(tool.name)
                            } else {
                                viewModel.screenState.value?.crisisDetails?.toolList?.any { it.name == tool.name }
                                    ?: false
                            }
                        CrisisRegisterIconButton(
                            imageVector = tool.icon,
                            size = 70.dp,
                            text = tool.name,
                            isSelected = isSelected,
                            onClick = {
                                if (isSelected) {
                                    selectedTools = selectedTools - tool.name
                                    viewModel.unselectCrisisTool(tool)
                                } else {
                                    selectedTools.plus(tool.name)
                                    viewModel.selectCrisisTool(tool)
                                }
                            }
                        )
                    }
                }
                IconButtonNoFill(
                    text = "Agregar otra herramienta",
                    modifier = Modifier.constrainAs(addNewIcon) {
                        top.linkTo(elementsGrid.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onClick = { shouldShowAddNewCard = !shouldShowAddNewCard }
                )
                AnimatedVisibility(
                    visible = shouldShowAddNewCard,
                    modifier = Modifier.constrainAs(newElementsCard) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }) {
                    NewCrisisElementCard(
                        placeholderText = "Agrega otra herramienta...",
                        icon = icon,
                        onSave = {
                            viewModel.addCrisisTool(
                                CrisisTool(
                                    name = it,
                                    icon = icon,
                                    id = it
                                )
                            )
                            shouldShowAddNewCard = !shouldShowAddNewCard
                        },
                        onCancel = { shouldShowAddNewCard = false }
                    )
                }
            }

            6 -> {
                TextArea(
                    title = "¿Querés agregar algo más?",
                    textActual = screenState.value?.crisisDetails?.notes!!,
                    modifier = Modifier.constrainAs(addMoreStep) {
                        top.linkTo(title.bottom, margin = 3.dp)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onTextChange = { viewModel.updateNotes(it) }
                )
            }
        }
        Icon(
            Icons.Default.Close,
            contentDescription = "Close",
            tint = ColorGray,
            modifier = Modifier
                .size(32.dp)
                .constrainAs(closeIcon) {
                    top.linkTo(progressBar.bottom, margin = 10.dp)
                    end.linkTo(parent.end, margin = horizontalMargin)
                }
                .clickable {
                    viewModel.showExitModal()
                }
        )
        if (screenState.value?.currentStep != 1 && viewModel.shouldGoToBack) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = ColorPrimary,
                modifier = Modifier
                    .size(50.dp)
                    .constrainAs(backArrow) {
                        bottom.linkTo(parent.bottom, margin = 10.dp)
                        start.linkTo(parent.start, margin = 15.dp)
                    }
                    .clickable {
                        if (screenState.value?.currentStep != 1) {
                            viewModel.goOneStepBack()
                            shouldShowAddNewCard = false
                        }
                    }
            )
        }
        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Forward",
            tint = ColorPrimary,
            modifier = Modifier
                .size(50.dp)
                .constrainAs(forwardArrow) {
                    bottom.linkTo(parent.bottom, margin = 10.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                }
                .clickable {
                    when {
                        viewModel.shouldGoToSummary -> {
                            navController.popBackStack()
                        }

                        screenState.value?.currentStep!! < 6 -> {
                            viewModel.goOneStepForward()
                            shouldShowAddNewCard = false
                        }


                        screenState.value?.currentStep == 6 -> {
                            onFinishedRegistration()
                        }
                    }
                }
        )
        Modal(
            show = shouldShowExitModal,
            stringResource(R.string.title_exit_modal_crisis_handling),
            primaryButtonText = stringResource(R.string.confirm),
            secondaryButtonText = stringResource(R.string.dismiss),
            onConfirm = {
                onClose()
                viewModel.cleanState()
            },
            onDismiss = { viewModel.dismissExitModal() }
        )
    }
}


object GridElementsRepository {
    fun returnAvailablePlaces(): List<CrisisPlace> {
        return listOf(
            CrisisPlace(name = "Casa", icon = Icons.Filled.Home),
            CrisisPlace(name = "Trabajo", icon = Icons.Filled.Work),
            CrisisPlace(name = "Bar", icon = Icons.Filled.SportsBar),
            CrisisPlace(name = "Universidad", icon = Icons.Filled.School),
        )
    }

    fun returnAvailableEmotions(): List<CrisisEmotion> {
        return listOf(
            CrisisEmotion(
                name = "Miedo",
                icon = Icons.Outlined.SentimentVeryDissatisfied,
                intensity = Intensity.LOW
            ),
            CrisisEmotion(
                name = "Tristeza",
                icon = Icons.Outlined.SentimentDissatisfied,
                intensity = Intensity.LOW
            ),
            CrisisEmotion(
                name = "Enfado",
                icon = Icons.Outlined.SentimentVeryDissatisfied,
                intensity = Intensity.LOW
            )
        )
    }

    fun returnAvailableTools(): List<CrisisTool> {
        return listOf(
            CrisisTool(name = "Imaginacion guiada", icon = Icons.Outlined.Preview, id = "Imaginación guiada"),
            CrisisTool(name = "Respiracion", icon = Icons.Outlined.Air, id = "Controlar la respiración"),
            CrisisTool(name = "Autoafirmaciones", icon = Icons.Outlined.Psychology, id = "Autoafirmaciones"),
        )
    }

    fun returnAvailableBodySensations(): List<CrisisBodySensation> {
        return listOf(
            CrisisBodySensation(
                name = "Mareos",
                icon = Icons.Filled.Cached,
                intensity = Intensity.LOW
            ),
            CrisisBodySensation(
                name = "Temblores",
                icon = Icons.Filled.Sensors,
                intensity = Intensity.LOW
            ),
            CrisisBodySensation(
                name = "Palpitaciones",
                icon = Icons.Filled.MonitorHeart,
                intensity = Intensity.LOW
            )
        )
    }
}
