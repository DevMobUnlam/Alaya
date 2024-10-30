package com.devmob.alaya.ui.screen.patientSummary

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun PatientSummaryScreen(
    modifier: Modifier = Modifier,
    viewModel: PatientSummaryViewModel
){
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }

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
            is SummaryUIState.Initial -> {}
            is SummaryUIState.Error -> {

                Text(
                    modifier = Modifier.constrainAs(headerText){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start, margin = 14.dp)
                        end.linkTo(parent.end, margin = 14.dp)
                    },
                    text = "No es posible ver el resumen en este momento",
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    color = ColorText
                )

                Toast.makeText(
                    context,
                    stringResource(R.string.patient_summary_error),
                    Toast.LENGTH_SHORT).show()
            }
            is SummaryUIState.Success -> {

                Text(
                    modifier = Modifier.constrainAs(headerText){
                        top.linkTo(parent.top, margin = 30.dp)
                        start.linkTo(parent.start, margin = 14.dp)
                        end.linkTo(parent.end, margin = 14.dp)
                    },
                    text = "Resumen",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorText
                )

                ElevatedCard(
                    modifier = Modifier.padding(horizontal = 5.dp)
                        .constrainAs(summaryText){
                        top.linkTo(headerText.bottom, margin = 10.dp)
                        start.linkTo(parent.start, margin = 15.dp)
                        end.linkTo(parent.end, margin = 15.dp)
                        bottom.linkTo(parent.bottom, margin = 12.dp)
                    }
                        ,
                    colors = CardDefaults.cardColors(containerColor = ColorWhite),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                ) {
                    Text(modifier = Modifier
                        .padding(horizontal = 9.dp, vertical = 9.dp),
                        text = (uiState.value as SummaryUIState.Success).outputText,
                        textAlign = TextAlign.Center,
                        color = ColorText

                    )
                }


            }
        }
    }


}