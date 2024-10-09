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
import com.devmob.alaya.components.Card
import com.devmob.alaya.navigation.ProfessionalNavigation.NavUtilsProfessional
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.Modal

@Composable
fun TreatmentSummaryScreen(
    firstStep: String,
    secondStep: String,
    thirdStep: String,
    navController: NavController
) {

    val selectedOptions = listOfNotNull(
        getTreatmentOption(firstStep),
        getTreatmentOption(secondStep),
        getTreatmentOption(thirdStep)
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
                Card(
                    title = option.title,
                    subtitle = option.description,
                    imageResId = getDrawableResId(option.title),
                    onClick = { }
                )
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
                navController.navigate(NavUtilsProfessional.Routes.Home.route)
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

fun getTreatmentOption(title: String): OptionTreatment? {
    return when (title) {
        "Controlar la respiración" -> OptionTreatment(
            "Controlar la respiración",
            "Poner una mano en el pecho y otra en el estómago para tomar aire y soltarlo lentamente"
        )

        "Imaginación guiada" -> OptionTreatment(
            "Imaginación guiada",
            "Cerrar los ojos y pensar en un lugar tranquilo, prestando atención a todos los sentidos del ambiente que te rodea"
        )

        "Autoafirmaciones" -> OptionTreatment(
            "Autoafirmaciones",
            """
                Repetir frases:
                “Soy fuerte y esto pasará”
                “Tengo el control de mi mente y mi cuerpo”
                “Me merezco tener alegría y plenitud”
            """.trimIndent()
        )

        else -> null
    }
}
