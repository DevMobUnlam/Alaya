package com.devmob.alaya.ui.screen.crisis_registration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.Intensity
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorIntensityHigh
import com.devmob.alaya.ui.theme.ColorIntensityLow
import com.devmob.alaya.ui.theme.ColorIntensityMedium
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.NavUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * Pantalla de Resumen de Registro de Crisis
 *
 * Se muestran los datos del formulario de registro
 *
 * onConfirm: Accion a ejecutar una vez confirmado el registro de crisis
 *
 * onDelete: Accion a ejecutar una vez se aprete en eliminar datos del formulario
 *
 * onEditClick: Accion a ejecutar una vez se aprete en el icono para editar el respectivo paso del formulario. Recibe un Int que seria el paso a editar en cuestion
 *
 */

@Composable
fun CrisisRegistrationSummaryScreen(
    modifier: Modifier = Modifier,
    viewModel: CrisisRegistrationViewModel,
    navController: NavController
) {

    val screenState = viewModel.screenState.observeAsState()
    var showModalDelete by remember { mutableStateOf(false) }
    var showModalConfirm by remember { mutableStateOf(false) }


    BackHandler{
        navController.navigate(NavUtils.PatientRoutes.CrisisRegistration.route)
        viewModel.updateStep(6)
        viewModel.shouldGoToBack = true
        viewModel.shouldGoToSummary = false
    }

    ConstraintLayout(modifier = modifier.fillMaxSize()) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    screenState.value?.crisisDetails?.crisisTimeDetails?.let { date ->
                        SummaryItemCard(
                            title = "Inicio y Fin",
                            startContent = {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = dateFormatter(date.startTime),
                                        fontSize = 19.sp,
                                        color = ColorText
                                    )
                                    Text(
                                        text = dateFormatter(date.endTime),
                                        fontSize = 19.sp,
                                        color = ColorText
                                    )
                                }
                            },
                            endContent = {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = timeFormatter(date.startTime),
                                        fontSize = 19.sp,
                                        color = ColorText
                                    )
                                    Text(
                                        text = timeFormatter(date.endTime),
                                        fontSize = 19.sp,
                                        color = ColorText
                                    )
                                }
                            },
                            onEditClick = {
                                goToStepScreenToEdit(1, navController, viewModel)
                                viewModel.hideBackButton()
                            },
                            step = 1
                        )
                    }
                }

                item {
                    SummaryItemCard(
                        title = stringResource(R.string.place),
                        startContent = {
                            Column {
                                screenState.value?.crisisDetails?.placeList?.let { places ->
                                    if (places.isNotEmpty()) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                places.first().icon,
                                                contentDescription = stringResource(R.string.place),
                                                tint = ColorText,
                                                modifier = Modifier.size(35.dp)
                                            )
                                            Text(
                                                text = places.first().name,
                                                fontSize = 21.sp,
                                                color = ColorText,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        },
                        onEditClick = {
                            goToStepScreenToEdit(2, navController, viewModel)
                            viewModel.hideBackButton()
                        },
                        step = 2
                    )
                }

                item {
                    SummaryItemCard(
                        title = stringResource(R.string.body_sensations),
                        startContent = {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                screenState.value?.crisisDetails?.bodySensationList?.let { sensationsList ->
                                    if (sensationsList.isNotEmpty()) {
                                        sensationsList.forEach { sensation ->
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                            ) {
                                                Icon(
                                                    sensation.icon,
                                                    contentDescription = stringResource(R.string.place),
                                                    tint = ColorText,
                                                    modifier = Modifier.size(35.dp),
                                                )
                                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                                    Text(
                                                        text = sensation.name,
                                                        fontSize = 21.sp,
                                                        color = ColorText,
                                                        fontWeight = FontWeight.Bold,
                                                    )
                                                    Text(
                                                        text = intensityFormatter(sensation.intensity),
                                                        fontSize = 19.sp,
                                                        color = ColorText,
                                                        modifier = getIntensityModifier(sensation.intensity)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        onEditClick = {
                            goToStepScreenToEdit(3, navController, viewModel)
                            viewModel.hideBackButton()
                        },
                        step = 3
                    )
                }

                item {
                    SummaryItemCard(
                        title = stringResource(R.string.emotions),
                        startContent = {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                screenState.value?.crisisDetails?.emotionList?.let { emotionList ->
                                    if (emotionList.isNotEmpty()) {
                                        emotionList.forEach { emotion ->
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    emotion.icon,
                                                    contentDescription = stringResource(R.string.place),
                                                    tint = ColorText,
                                                    modifier = Modifier.size(35.dp)
                                                )
                                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                                    Text(
                                                        text = emotion.name,
                                                        fontSize = 21.sp,
                                                        color = ColorText,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = intensityFormatter(emotion.intensity),
                                                        fontSize = 19.sp,
                                                        color = ColorText,
                                                        modifier = getIntensityModifier(emotion.intensity)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        onEditClick = {
                            goToStepScreenToEdit(4, navController, viewModel)
                            viewModel.hideBackButton()
                        },
                        step = 4
                    )
                }

                item {
                    SummaryItemCard(
                        title = stringResource(R.string.tools),
                        startContent = {
                            Column {
                                screenState.value?.crisisDetails?.toolList?.let { toolList ->
                                    if (toolList.isNotEmpty()) {
                                        toolList.forEach { tool ->
                                            Row(
                                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    tool.icon,
                                                    contentDescription = stringResource(R.string.place),
                                                    tint = ColorText,
                                                    modifier = Modifier.size(35.dp)
                                                )
                                                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                                                    Text(
                                                        text = tool.name,
                                                        fontSize = 21.sp,
                                                        color = ColorText,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        onEditClick = {
                            goToStepScreenToEdit(5, navController, viewModel)
                            viewModel.hideBackButton()
                        },
                        step = 5
                    )
                }

                item {
                    screenState.value?.crisisDetails?.notes?.let { notes ->
                        SummaryItemCard(
                            title = stringResource(R.string.notes),
                            startContent = {
                                Text(
                                    notes,
                                    fontSize = 19.sp,
                                    color = ColorText,
                                    maxLines = 3
                                )
                            },
                            onEditClick = {
                                goToStepScreenToEdit(
                                    step = 6,
                                    navController = navController,
                                    viewModel = viewModel
                                )
                                viewModel.hideBackButton()
                            },
                            step = 6
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Button(
                    text = "Eliminar",
                    style = ButtonStyle.Outlined,
                    onClick = { showModalDelete = true }
                )
                Button(
                    text = "Confirmar",
                    style = ButtonStyle.Filled,
                    onClick = { showModalConfirm = true }
                )
            }
            Modal(
                show = showModalDelete,
                title = "¿Seguro que quiere eliminar el registro?",
                primaryButtonText = "Si",
                secondaryButtonText = "No",
                onDismiss = { showModalDelete = false },
                onConfirm = {
                    showModalDelete = false
                    viewModel.cleanState()
                    navController.navigate(NavUtils.PatientRoutes.Home.route){popUpTo(NavUtils.PatientRoutes.Home.route){inclusive = false} }
                }
            )

            Modal(
                show = showModalConfirm,
                title = "¿Quiere confirmar el registro?",
                primaryButtonText = "Si",
                secondaryButtonText = "No",
                onDismiss = { showModalConfirm = false },
                onConfirm = {
                    viewModel.saveRegister()
                    showModalConfirm = false
                    viewModel.cleanState()
                    navController.navigate(NavUtils.PatientRoutes.Home.route){popUpTo(NavUtils.PatientRoutes.Home.route){inclusive = false} }
                }
            )
        }
    }
}

@Composable
private fun getIntensityModifier(intensity: Intensity) = Modifier
    .drawBehind {
        drawRoundRect(
            solveColorIntensity(intensity),
            cornerRadius = CornerRadius(20.dp.toPx())
        )
    }
    .padding(4.dp)


@Composable
fun SummaryItemCard(
    modifier: Modifier = Modifier,
    title: String,
    step: Int = 1,
    onEditClick: (Int) -> Unit,
    startContent: @Composable () -> Unit,
    endContent: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = ColorWhite),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(9.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = ColorText
                )
                Icon(
                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            onEditClick(step)
                        },
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "Editar",
                    tint = ColorGray
                )

            }
            Spacer(modifier = Modifier.height(3.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                startContent()
                endContent?.invoke()
            }
        }
    }
}

/**
 * Formateador de Intensidad que devuelve como un String el tipo de intensidad que reciba por parametro
 */
@Composable
fun intensityFormatter(intensity: Intensity): String {
    return when (intensity) {
        Intensity.LOW -> "Intensidad: " + stringResource(R.string.intensity_low)
        Intensity.MEDIUM -> "Intensidad: " + stringResource(R.string.intensity_medium)
        Intensity.HIGH -> "Intensidad: " + stringResource(R.string.intensity_high)
    }
}

fun solveColorIntensity(intensity: Intensity): Color {
    return when (intensity) {
        Intensity.LOW -> ColorIntensityLow
        Intensity.MEDIUM -> ColorIntensityMedium
        Intensity.HIGH -> ColorIntensityHigh
    }
}

private fun dateFormatter(date: Date): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(date)
}

private fun timeFormatter(date: Date): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(date)
}

private fun goToStepScreenToEdit(
    step: Int,
    navController: NavController,
    viewModel: CrisisRegistrationViewModel
) {
    viewModel.updateStep(step)
    navController.navigate(NavUtils.PatientRoutes.CrisisRegistration.route)
}
