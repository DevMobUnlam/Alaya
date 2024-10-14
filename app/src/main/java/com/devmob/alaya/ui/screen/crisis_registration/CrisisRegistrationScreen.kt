package com.devmob.alaya.ui.screen.crisis_registration

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Accessibility
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devmob.alaya.R
import com.devmob.alaya.components.SegmentedProgressBar
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.ChangeHistory
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Preview
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.SentimentVeryDissatisfied
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.constraintlayout.compose.ConstraintLayout
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.domain.model.Intensity
import com.devmob.alaya.ui.components.CrisisRegistrationElementIconButton
import com.devmob.alaya.ui.components.DateTimePicker
import com.devmob.alaya.ui.components.EmotionIconButton
import com.devmob.alaya.ui.components.IconButtonNoFill
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.components.NewCrisisElementCard
import com.devmob.alaya.ui.components.TextArea
import com.devmob.alaya.ui.theme.ColorWhite

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CrisisRegistrationScreen(
    viewModel: CrisisRegistrationViewModel = viewModel(),
    onClose: () -> Unit,
    onFinishedRegistration: (CrisisRegistrationScreenState) -> Unit,
    ) {

    val screenState = viewModel.screenState.observeAsState()
    val shouldShowExitModal = viewModel.shouldShowExitModal
    var messageTextSize = 30.sp
    val horizontalMargin = 15.dp
    var shouldShowAddNewCard by remember { mutableStateOf(false) }

    GridElementsRepository.returnAvailableTools().let { list ->
        for(tool in list){
            viewModel.addCrisisTool(tool)
        }
    }

    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(ColorWhite)) {
        val (progressBar, datePickerComponent, closeIcon,title, backArrow, forwardArrow, saveEditingButton) = createRefs()
        val (elementsGrid,addNewIcon, newElementsCard, addMoreStep) = createRefs()

        SegmentedProgressBar(
            totalSteps = screenState.value!!.totalSteps,
            currentStep = screenState.value!!.currentStep,
            modifier = Modifier.constrainAs(progressBar){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )


        when(screenState.value?.currentStep){
            1 ->{
                Text(
                    text = "¿En qué momento\ncomenzó?",
                    fontSize = messageTextSize,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier
                        .constrainAs(title){
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(progressBar.bottom, margin = 20.dp)
                        }

                )

                viewModel.screenState.value?.crisisDetails?.crisisTimeDetails?.let {
                    DateTimePicker(modifier = Modifier.constrainAs(datePickerComponent){
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(title.bottom)
                    },
                        onStartDateChange = {viewModel.updateStartDate(it)} ,
                        onStartTimeChange = {viewModel.updateStartTime(it)} ,
                        onEndDateChange = {viewModel.updateEndDate(it)},
                        onEndTimeChange = {viewModel.updateEndTime(it)},
                        crisisTimeDetails = it
                    )
                }
            }
            2 ->{

                val icon = Icons.Filled.LocationOn

                Text(
                    text = "¿Donde estabas?",
                    fontSize = messageTextSize,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier
                        .constrainAs(title){
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(progressBar.bottom, margin = 20.dp)
                        }
                )



                    viewModel.screenState.value?.crisisDetails?.placeList?.let { places ->
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.constrainAs(elementsGrid){
                                top.linkTo(title.bottom,margin = 20.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                        ) {
                            itemsIndexed(places) {index, place ->
                                CrisisRegistrationElementIconButton(
                                    symbol = place.icon,
                                    text = place.name,
                                    size = 60.dp,
                                    isActive = place.isActive,
                                    onClick = {viewModel.updatePlaceStatus(place, index, !place.isActive)}
                                )
                            }
                        }
                    }
                IconButtonNoFill(
                    text = "Agregar otra ubicacion",
                    modifier = Modifier.constrainAs(addNewIcon){
                        top.linkTo(elementsGrid.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onClick = {shouldShowAddNewCard = !shouldShowAddNewCard}
                )

                AnimatedVisibility(visible = shouldShowAddNewCard, modifier = Modifier.constrainAs(newElementsCard){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                    NewCrisisElementCard(
                        placeholderText = "Agrega otro lugar...",
                        icon = icon,
                        onSave = {viewModel.addCrisisPlace(
                            CrisisPlace(
                                name = it,
                                icon = icon
                            )
                        )
                            shouldShowAddNewCard = !shouldShowAddNewCard
                        }
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
                        .constrainAs(title){
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(progressBar.bottom, margin = 20.dp)
                        }
                )


                viewModel.screenState.value?.crisisDetails?.bodySensationList?.let { bodySensations ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.constrainAs(elementsGrid){
                            top.linkTo(title.bottom,margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    ) {
                        itemsIndexed(bodySensations) {index, sensation ->
                            var isActive = false
                            EmotionIconButton(
                                symbol = sensation.icon,
                                text = sensation.name,
                                size = 60.dp,
                                intensity = sensation.intensity,
                                onChangedIntensity = {
                                    viewModel.onBodySensationIntensityChange(
                                        intensity = it,
                                        index = index,
                                        bodySensation = sensation
                                    )
                                }
                            )
                        }
                    }
                }
                IconButtonNoFill(
                    text = "Añadir \n sensación \n corporal",
                    modifier = Modifier.constrainAs(addNewIcon){
                        top.linkTo(elementsGrid.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onClick = {shouldShowAddNewCard = !shouldShowAddNewCard}
                )

                AnimatedVisibility(visible = shouldShowAddNewCard, modifier = Modifier.constrainAs(newElementsCard){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                    NewCrisisElementCard(
                        placeholderText = "Agrega otra sensacion...",
                        icon = icon,
                        onSave = {viewModel.addCrisisBodySensation(
                            CrisisBodySensation(
                                name = it,
                                icon = icon
                            )
                        )
                            shouldShowAddNewCard = !shouldShowAddNewCard
                        }
                    )
                }

            }
            4 ->{
                Text(
                    text = "¿Qué emociones sentiste?",
                    fontSize = messageTextSize,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier
                        .constrainAs(title){
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(progressBar.bottom, margin = 30.dp)
                        }
                )
            }
            5 ->{
                val icon = Icons.Filled.ChangeHistory

                Text(
                    text = "¿Qué herramientas usaste para calmarte?",
                    fontSize = messageTextSize,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    lineHeight = 30.sp,
                    modifier = Modifier
                        .constrainAs(title){
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            top.linkTo(progressBar.bottom, margin = 30.dp)
                        }
                )

                viewModel.screenState.value?.crisisDetails?.toolList?.let { tools ->
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.constrainAs(elementsGrid){
                            top.linkTo(title.bottom,margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    ) {
                        itemsIndexed(tools) {index, tool ->
                            CrisisRegistrationElementIconButton(
                                symbol = tool.icon,
                                text = tool.name,
                                size = 60.dp,
                                isActive = tool.isActive,
                                // TODO() AGREGAR ACCION CUANDO SE PRESIONA EL BOTON
                                onClick = {/*viewModel.updateToolStatus(tool, index, !place.isActive)*/}
                            )
                        }
                    }
                }
                IconButtonNoFill(
                    text = "Agregar otra herramienta",
                    modifier = Modifier.constrainAs(addNewIcon){
                        top.linkTo(elementsGrid.bottom, margin = 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    onClick = {shouldShowAddNewCard = !shouldShowAddNewCard}
                )

                AnimatedVisibility(visible = shouldShowAddNewCard, modifier = Modifier.constrainAs(newElementsCard){
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }) {
                    NewCrisisElementCard(
                        placeholderText = "Agrega otra herramienta...",
                        icon = icon,
                        onSave = {viewModel.addCrisisTool(
                            CrisisTool(
                                name = it,
                                icon = icon
                            )
                        )
                            shouldShowAddNewCard = !shouldShowAddNewCard
                        }
                    )
                }


            }
            6 ->{
                    TextArea(
                        title = "¿Querés agregar algo más?",
                        text = screenState.value?.crisisDetails?.notes!!,
                        modifier = Modifier.constrainAs(addMoreStep){
                            top.linkTo(title.bottom)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        onTextChange = {viewModel.updateNotes(it)}
                        // TODO() AGREGAR ACCION A EJECUTAR UNA VEZ SE APRIETA EL BOTON DEL MICROFONO
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


            if(screenState.value?.currentStep != 1){
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
                        if (screenState.value?.currentStep!! < 6) {
                            viewModel.goOneStepForward()
                            shouldShowAddNewCard = false
                    } else{
                            onFinishedRegistration(screenState.value!!)
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

object GridElementsRepository{
    fun returnAvailablePlaces(): List<CrisisPlace>{
        return listOf(
            CrisisPlace(name = "Casa", icon = Icons.Filled.Home),
            CrisisPlace(name = "Trabajo", icon = Icons.Filled.Work),
            CrisisPlace(name = "Bar", icon = Icons.Filled.SportsBar),
            CrisisPlace(name = "Universidad", icon = Icons.Filled.School),
        )
    }

    @Composable
    fun returnAvailableEmotions(): List<CrisisEmotion>{
        return listOf(
            CrisisEmotion(name = "Miedo",icon = Icons.Outlined.SentimentVeryDissatisfied, intensity = Intensity.LOW),
            CrisisEmotion(name = "Tristeza",icon = ImageVector.vectorResource(R.drawable.sentiment_fear), intensity = Intensity.LOW),
            CrisisEmotion(name = "Enfado",icon = ImageVector.vectorResource(R.drawable.sentiment_extremely_dissatisfied) ,  intensity = Intensity.LOW),
        )
    }

    @Composable
    fun returnAvailableTools(): List<CrisisTool>{
        return listOf(
            CrisisTool(name = "Imaginacion guiada",icon = Icons.Outlined.Preview),
            CrisisTool(name = "Respiracion",icon = Icons.Outlined.Air),
            CrisisTool(name = "Autoafirmaciones",icon = Icons.Outlined.Psychology),
        )
    }

    fun returnAvailableBodySensations(): List<CrisisBodySensation>{
        return listOf(
            CrisisBodySensation(name = "Mareos",icon = Icons.Filled.Cached, intensity = Intensity.LOW),
            CrisisBodySensation(name = "Temblores",icon = Icons.Filled.Sensors, intensity = Intensity.LOW),
            CrisisBodySensation(name = "Palpitaciones",icon = Icons.Filled.MonitorHeart, intensity = Intensity.LOW)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CrisisRegistrationScreenPreview(){
    CrisisRegistrationScreen(onClose = {}, onFinishedRegistration = {})
}