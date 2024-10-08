package com.devmob.alaya.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Preview
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.SentimentVeryDissatisfied
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import com.devmob.alaya.domain.model.CrisisBodySensation
import com.devmob.alaya.domain.model.CrisisEmotion
import com.devmob.alaya.domain.model.CrisisPlace
import com.devmob.alaya.domain.model.CrisisTool
import com.devmob.alaya.ui.components.Modal

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CrisisRegistrationScreen(
    viewModel: CrisisRegistrationViewModel = viewModel(),
    onClose: () -> Unit) {

    val screenState = viewModel.screenState.observeAsState()
    val shouldShowExitModal = viewModel.shouldShowExitModal
    var messageTextSize = 30.sp
    val horizontalPadding = 15.dp
    var shouldShowGridList by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = horizontalPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        SegmentedProgressBar(
            totalSteps = screenState.value!!.totalSteps,
            currentStep = screenState.value!!.currentStep,
        )

        Icon(
            Icons.Default.Close,
            contentDescription = "Close",
            tint = ColorGray,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.End)
                .clickable {
                    viewModel.showExitModal()
                }
        )

        AnimatedContent(targetState = screenState.value!!.currentStep) {
            var screenMessage = when (it) {
                1 -> stringResource(R.string.start_question)
                2 -> "¿Donde estabas?"
                3 -> "¿Qué sensaciones\n corporales sentiste?"
                4 -> "¿Que emociones sentiste?"
                5 -> "Qué herramientas usaste\npara calmarte?"
                else -> "¿Querés agregar algo más?"
            }.toString()

            Text(
                text = screenMessage,
                fontSize = messageTextSize,
                color = ColorText,
                textAlign = TextAlign.Center,
                lineHeight = 30.sp,
                modifier = Modifier
                    .padding(15.dp)
                )

    }

            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = ColorPrimary,
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        if (screenState.value?.currentStep != 1) {
                            viewModel.goOneStepBack()
                        }

                    }
            )

            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Forward",
                tint = ColorPrimary,
                modifier = Modifier
                    .size(50.dp)
                    .clickable {
                        viewModel.goOneStepForward()
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
            onDismiss = { viewModel.dismissExitModal() })


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
            CrisisEmotion(name = "Miedo",icon = Icons.Outlined.SentimentVeryDissatisfied, intensity = "Baja"),
            CrisisEmotion(name = "Tristeza",icon = ImageVector.vectorResource(R.drawable.sentiment_fear), intensity = "Baja"),
            CrisisEmotion(name = "Enfado",icon = ImageVector.vectorResource(R.drawable.sentiment_extremely_dissatisfied) , intensity = "Baja"),
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
            CrisisBodySensation(name = "Mareos",icon = Icons.Filled.Cached, intensity = "Baja"),
            CrisisBodySensation(name = "Temblores",icon = Icons.Filled.Sensors, intensity = "Baja"),
            CrisisBodySensation(name = "Palpitaciones",icon = Icons.Filled.MonitorHeart, intensity = "Baja")
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CrisisRegistrationScreenPreview(){
    CrisisRegistrationScreen(onClose = {})
}