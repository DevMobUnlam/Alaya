package com.devmob.alaya.ui.screen.TreatmentSummaryScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.Card
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.screen.professionalCrisisTreatment.ConfigTreatmentViewModel
import com.devmob.alaya.utils.NavUtils

@Composable
fun TreatmentSummaryScreen(
    firstStep: String,
    secondStep: String,
    thirdStep: String,
    patientEmail: String,
    navController: NavController,
    viewModel: ConfigTreatmentViewModel
) {
    val selectedOptions = listOfNotNull(
        getTreatmentOption(firstStep, viewModel.treatmentOptions),
        getTreatmentOption(secondStep, viewModel.treatmentOptions),
        getTreatmentOption(thirdStep, viewModel.treatmentOptions)
    )
    var showModal by remember { mutableStateOf(false) }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlueColor)
    ) {
        val (title, content, buttonColumn) = createRefs()

        Text(
            text = "Resumen del Tratamiento",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            ),
            color = ColorText,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 50.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(content) {
                    top.linkTo(title.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(buttonColumn.top, margin = 16.dp)
                    height = Dimension.fillToConstraints
                }
        ) {
            selectedOptions.forEach { option ->
                val drawableResId = getDrawableResId(option.title)
                val urivacio = ""
                when {
                    option.imageUri != urivacio ->
                        Card(
                            title = option.title,
                            subtitle = option.description,
                            imageUrl = option.imageUri,
                            onClick = { }
                        )
                    drawableResId != 0 ->
                        Card(
                            title = option.title,
                            subtitle = option.description,
                            imageResId = getDrawableResId(option.title),
                            onClick = { }
                        )

          else -> {
                    Card(
                        title = option.title,
                        subtitle = option.description,
                        onClick = { }
                    )
                }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .constrainAs(buttonColumn) {
                    bottom.linkTo(parent.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    showModal = true
                    viewModel.saveCrisisTreatment(patientEmail, selectedOptions)
                    viewModel.sendNotification(patientEmail)

                },
                modifier = Modifier.fillMaxWidth(),
                text = "Confirmar"
            )

            Button(
                onClick = {navController.popBackStack()},
                modifier = Modifier.fillMaxWidth(),
                style = ButtonStyle.Outlined,
                text = "Cancelar"
            )
        }

        Modal(
            show = showModal,
            title = "Tratamiento Confirmado",
            primaryButtonText = "Aceptar",
            onDismiss = { showModal = false },
            onConfirm = {
                showModal = false
                navController.navigate(NavUtils.ProfessionalRoutes.Home.route)
            }
        )
    }
}

fun getDrawableResId(title: String): Int {
    return when (title) {
        "Controlar la respiración" -> R.drawable.crisis_step_1
        "Imaginación guiada" -> R.drawable.crisis_step_2
        "Autoafirmaciones" -> R.drawable.crisis_step_3
        else -> 0
    }
}

fun getTreatmentOption(title: String, options: List<OptionTreatment>): OptionTreatment? {
    return options.find { it.title == title }
}
