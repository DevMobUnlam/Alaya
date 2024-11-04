package com.devmob.alaya.ui.screen.professionalCrisisTreatment

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.SelectMenu
import com.devmob.alaya.ui.theme.LightBlueColor
import com.devmob.alaya.utils.NavUtils

@Composable
fun ConfigTreatmentScreen(
    patientEmail: String,
    viewModel: ConfigTreatmentViewModel,
    navController: NavController
) {
    val treatmentOptions = remember { viewModel.treatmentOptions }
    Log.d("leandro", "configtreatment screen emailPatient es $patientEmail")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlueColor)

    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 100.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SelectMenu(
                    title = "Paso 1",
                    options = treatmentOptions,
                    onOptionSelected = { viewModel.firstSelectOption.value = it }
                )
            }
            item {
                SelectMenu(
                    title = "Paso 2",
                    options = treatmentOptions,
                    onOptionSelected = { viewModel.secondSelectOption.value = it }
                )
            }
            item {
                SelectMenu(
                    title = "Paso 3",
                    options = treatmentOptions,
                    onOptionSelected = { viewModel.thirdSelectOption.value = it }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Button(
                text = "Confirmar tratamiento",
                onClick = {
                    navController.navigate(
                        NavUtils.ProfessionalRoutes.TreatmentSummary.createRoute(
                            firstStep = viewModel.firstSelectOption.value?.title ?: "",
                            secondStep = viewModel.secondSelectOption.value?.title ?: "",
                            thirdStep = viewModel.thirdSelectOption.value?.title ?: "",
                            patientEmail = patientEmail
                        )
                    )
                },
                style = ButtonStyle.Filled,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                text = "Actividad personalizada",
                onClick = {
                    navController.navigate(
                        NavUtils.ProfessionalRoutes.AddCustomActivity.createRoute(
                            patientEmail = patientEmail
                        )
                    )
                },
                style = ButtonStyle.Filled,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

