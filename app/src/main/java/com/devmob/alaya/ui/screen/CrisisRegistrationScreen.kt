package com.devmob.alaya.ui.screen

import android.graphics.fonts.FontStyle
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devmob.alaya.R
import com.devmob.alaya.components.SegmentedProgressBar
import com.devmob.alaya.ui.components.EmotionIconButton
import com.devmob.alaya.ui.components.IconButtonNoFill
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.SentimentVeryDissatisfied
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTool

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CrisisRegistrationScreen(
    viewModel: CrisisRegistrationViewModel = viewModel(),
    onCloseClick: () -> Unit)
{
    val screenState = viewModel.screenState.observeAsState()


    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(ColorWhite)) {
        val (progressBar,message,startingDate, closeIcon, startingTime,endDate, endTime, contentBox,addNewIcon, backArrow, forwardArrow) = createRefs()
        SegmentedProgressBar(
            totalSteps = screenState.value!!.totalSteps,
            currentStep = screenState.value!!.currentSteps,
            modifier = Modifier.constrainAs(progressBar){
                top.linkTo(parent.top, margin = 15.dp)
            }
        )

        Icon(
            Icons.Default.Close,
            contentDescription = "Close",
            tint = ColorGray,
            modifier = Modifier.size(25.dp).constrainAs(closeIcon){
                top.linkTo(progressBar.bottom,margin = 15.dp)
                end.linkTo(parent.end, margin = 15.dp)
            }.clickable {
                viewModel.cleanState()
                onCloseClick()
            }
        )

        when(screenState.value!!.currentSteps){
            1 -> {
                Text(stringResource(
                R.string.start_question),
                fontSize = 30.sp,
                color = ColorText,
                textAlign = TextAlign.Center,
                modifier = Modifier.constrainAs(message) {
                    top.linkTo(progressBar.bottom, margin = 18.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    }
                )


            }
            2 -> {

                var shouldShowGridList by remember{mutableStateOf(false)}

                Text(text = "¿Donde estabas?",
                    fontSize = 30.sp,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.constrainAs(message) {
                        top.linkTo(progressBar.bottom, margin = 18.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                LazyVerticalGrid(
                    contentPadding = PaddingValues(5.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    columns = GridCells.Fixed(3)
                ) {
                    viewModel.screenState.value?.placeList?.let {
                        items(it){place ->
                            EmotionIconButton(text = place.name, symbol = place.icon, onClick = {})
                        }

                    }

                }

                IconButtonNoFill(text = "Otra ubicación", onClick = {shouldShowGridList = !shouldShowGridList})
                AnimatedVisibility(visible = shouldShowGridList) {
                    LazyVerticalGrid(
                        contentPadding = PaddingValues(5.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        columns = GridCells.Fixed(3)
                    ) {
                        GridElementsRepository.returnAvailablePlaces().let{
                            items(it){ place ->
                                EmotionIconButton(text = place.name, symbol = place.icon, onClick = {})
                            }
                        }

                    }
                }
            }
            3 -> {}
            4 -> {}
            5 -> {}
            6 -> {}
        }

        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = ColorPrimary,
            modifier = Modifier.size(50.dp).constrainAs(backArrow){
                start.linkTo(parent.start, margin = 25.dp)
                bottom.linkTo(parent.bottom, margin = 25.dp)
            }
        )

        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = "Back",
            tint = ColorPrimary,
            modifier = Modifier.size(50.dp).constrainAs(forwardArrow){
                end.linkTo(parent.end, margin = 25.dp)
                bottom.linkTo(parent.bottom, margin = 25.dp)
            }
        )


    }



}

object GridElementsRepository{
    fun returnAvailablePlaces(): List<CrisisPlace>{
        return listOf(
            CrisisPlace(name = "Casa", icon = Icons.Filled.Home),
            CrisisPlace(name = "Trabajo", icon = Icons.Filled.Work),
            CrisisPlace(name = "Gymnasio", icon = Icons.Filled.SportsBar),
            CrisisPlace(name = "Universidad", icon = Icons.Filled.School),
        )
    }

    @Composable
    fun returnAvailableEmotions(): List<CrisisEmotion>{
        return listOf(
            CrisisEmotion(name = "Miedo",icon = Icons.Outlined.SentimentVeryDissatisfied, intensity = "Baja"),
            CrisisEmotion(name = "Tristeza",icon = ImageVector.vectorResource(R.drawable.sentiment_fear), intensity = "Baja"),
            CrisisEmotion(name = "Enfado",icon = ImageVector.vectorResource(R.drawable.sentiment_extremely_dissatisfied) , intensity = "Baja"),
        )
    }

    @Composable
    fun returnAvailableTools(): List<CrisisTool>{
        return listOf(
            CrisisTool(name = "Meditación",icon = Icons.Outlined.SelfImprovement),
            CrisisTool(name = "Respiracion",icon = Icons.Outlined.Air),
            CrisisTool(name = "Cerrar los ojos",icon = ImageVector.vectorResource(R.drawable.domino_mask)),
        )
    }

    @Composable
    fun returnAvailableBodySensations(): List<CrisisBodySensation>{
        return listOf(
            CrisisBodySensation(name = "Mareos",icon = Icons.Filled.Cached, intensity = "Baja"),
            CrisisBodySensation(name = "Temblores",icon = Icons.Filled.Sensors, intensity = "Baja"),
            CrisisBodySensation(name = "Palpitaciones",icon = ImageVector.vectorResource(R.drawable.vital_signs), intensity = "Baja")
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CrisisRegistrationScreenPreview(){
    CrisisRegistrationScreen(onCloseClick = {})
}