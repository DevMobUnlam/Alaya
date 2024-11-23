package com.devmob.alaya.ui.screen.patient_profile

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.contentReceiver
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.IASummaryText
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.CardContainer
import com.devmob.alaya.ui.components.ExpandableCard
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorText
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive

@Composable
fun PatientIASummaryScreen(
    modifier: Modifier = Modifier,
    viewModel: PatientIASummaryViewModel = hiltViewModel(),
) {

    val texto = IASummaryText(
        "Se registraron 3 eventos. Dos ocurrieron en la universidad, uno por la tarde y otro por la noche. El otro evento ocurrió en transporte público al mediodía.",
        "Brenda experimentado vergüenza (media), angustia (baja), miedo (alto y medio), mareos (medios y altos) y sudoración (media y alta). Para sobrellevar estas emociones y sensaciones, utilizó la imaginación guiada afirmaciones positivas y ejercicios de respiración.",
        "Los eventos están relacionados con situaciones académicas y de transporte. La imaginación guiada y la respiración fueron las herramientas más utilizadas para la regulación emocional."
    )
    val uiState = IASummaryUIState.Success(texto)//viewModel.uiState.collectAsStateWithLifecycle()
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
    ) {
        val (progressIndicator, headerText, summaryCard, retryButton) = createRefs()
        CardContainer(
            modifier = Modifier
                .padding(5.dp)
                .constrainAs(summaryCard) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, margin = 15.dp)
                    end.linkTo(parent.end, margin = 15.dp)
                    bottom.linkTo(parent.bottom)
                }
                .verticalScroll(rememberScrollState()),
            enabled = false,
            content = {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp),
                    text = "Detalles",
                    fontSize = 27.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    color = ColorText

                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp),
                    text = "Se registraron 3 eventos. Dos ocurrieron en la universidad, uno por la tarde y otro por la noche. El otro evento ocurrió en transporte público al mediodía.",//(uiState.value as IASummaryUIState.Success).outputText.timeAndPlace,
                    fontSize = 23.sp,
                    textAlign = TextAlign.Start,
                    color = ColorText

                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp),
                    text = "¿Cómo sucedió?",
                    fontSize = 27.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    color = ColorText
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp),
                    text = "Brenda experimentado vergüenza (media), angustia (baja), miedo (alto y medio), mareos (medios y altos) y sudoración (media y alta). Para sobrellevar estas emociones y sensaciones, utilizó la imaginación guiada afirmaciones positivas y ejercicios de respiración.",//(uiState.value as IASummaryUIState.Success).outputText.details,
                    fontSize = 23.sp,
                    textAlign = TextAlign.Start,
                    color = ColorText

                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp),
                    text = "Comentarios",
                    fontSize = 27.sp,
                    textAlign = TextAlign.Start,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    color = ColorText
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp),
                    text = "Los eventos están relacionados con situaciones académicas y de transporte. La imaginación guiada y la respiración fueron las herramientas más utilizadas para la regulación emocional.", //(uiState.value as IASummaryUIState.Success).outputText.extra,
                    fontSize = 23.sp,
                    textAlign = TextAlign.Start,
                    color = ColorText

                )
            }
        )
    }
}


