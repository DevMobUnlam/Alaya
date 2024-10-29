package com.devmob.alaya.ui.screen.patientSummary

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devmob.alaya.R

@Composable
fun PatientSummaryScreen(
    modifier: Modifier = Modifier,
    viewModel: PatientSummaryViewModel = viewModel()
){
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current

    ConstraintLayout(
        modifier = modifier.fillMaxSize()
    ){
        val (progressIndicator, headerText, summaryText) = createRefs()

        when(uiState.value){
            is SummaryUIState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(progressIndicator){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }
            is SummaryUIState.Error -> {

                Text(
                    modifier = Modifier.constrainAs(headerText){
                        top.linkTo(parent.top, margin = 12.dp)
                    },
                    text = "No es posible ver el resumen en este momento",
                    fontSize = 15.sp
                )

                Toast.makeText(
                    context,
                    stringResource(R.string.patient_summary_error),
                    Toast.LENGTH_SHORT).show()
            }
            is SummaryUIState.Initial -> {}
            is SummaryUIState.Success -> {
                Text(modifier = Modifier.constrainAs(summaryText){
                    top.linkTo(headerText.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 18.dp)
                    end.linkTo(parent.end, margin = 18.dp)
                    bottom.linkTo(parent.bottom, margin = 15.dp)

                },
                    text = (uiState.value as SummaryUIState.Success).outputText
                )
            }
        }
    }


}