package com.devmob.alaya.ui.screen.patient_profile

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.navigation.ProfessionalNavigation.NavUtilsProfessional
import com.devmob.alaya.ui.components.Button as ButtonAlaya
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.CardContainer
import com.devmob.alaya.ui.components.DashboardCard
import com.devmob.alaya.ui.components.NextAppointmentHeader
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactViewModel
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.NavUtils
import java.time.LocalDateTime

@Composable
fun PatientProfileScreen(navController: NavController, patientId: String, contactViewModel: ContactViewModel, patientProfileViewModel: PatientProfileViewModel) {
    val context = LocalContext.current
    val phoneNumber = "1166011371"
    val uiState = patientProfileViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(patientId){
        patientProfileViewModel.summarize(patientId)
    }

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
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())

    ) {
        val (image, header, summaryCard, treatmentButton, sessionButton, whatsappButton, iaSummaryCard) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.brenda_rodriguez),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .constrainAs(image) {
                    top.linkTo(header.top)
                    bottom.linkTo(header.bottom)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        )

        NextAppointmentHeader(
             "Brenda",
             "Rodriguez",
            date = LocalDateTime.now(),
            modifier = Modifier.constrainAs(header) {
                start.linkTo(image.end, margin = 8.dp)
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        )

        Button(
            onClick = {
                val formattedNumber = contactViewModel.formatPhoneNumberForWhatsApp(phoneNumber)
                contactViewModel.sendWhatsAppMessage(context, formattedNumber, "Hola, ¿cómo estás?")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF25D366)
            ),
            modifier = Modifier.constrainAs(whatsappButton) {
                top.linkTo(header.bottom, margin = 16.dp)
                start.linkTo(header.start)
                end.linkTo(header.end)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.whatsapp),
                contentDescription = "WhatsApp",
                modifier = Modifier.size(24.dp),
                tint = Color.White,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Enviar mensaje", color = Color.White)
        }

        DashboardCard(
            modifier = Modifier.constrainAs(summaryCard) {
                top.linkTo(whatsappButton.bottom, margin = 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            crisisAmount = 2,
            usedTools = listOf("Autoafirmaciones", "Respiracion Guiada"),
            dailyActivities = 4
        )

        when(uiState.value){
            SummaryUIState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(iaSummaryCard){
                        top.linkTo(summaryCard.bottom, margin = 8.dp)
                        bottom.linkTo(treatmentButton.top, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

            }
            SummaryUIState.Initial -> {}
            is SummaryUIState.Error -> {

                ElevatedCard(
                    modifier = Modifier
                        .padding(horizontal = 5.dp)
                        .height(200.dp)
                        .fillMaxWidth(0.8F)
                        .constrainAs(iaSummaryCard){
                            top.linkTo(summaryCard.bottom, margin = 8.dp)
                            start.linkTo(parent.start, margin = 10.dp)
                            end.linkTo(parent.end, margin = 10.dp)
                            bottom.linkTo(treatmentButton.top, margin = 8.dp)
                        }
                    ,
                    colors = CardDefaults.cardColors(containerColor = ColorWhite),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                ) {
                    Spacer(modifier = Modifier.fillMaxHeight(0.25F))
                    Text(modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.CenterHorizontally),
                        text = "No es posible ver el resumen en este momento",
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        color = ColorText,

                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    com.devmob.alaya.ui.components.Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        text = "Reintentar",
                        onClick = ({ patientProfileViewModel.onRetryClick() })
                    )
                }

            }
            is SummaryUIState.Success -> {

                ElevatedCard(
                    modifier = Modifier
                        .padding(horizontal = 5.dp, vertical = 5.dp)
                        .height(180.dp)
                        .fillMaxWidth(0.90F)
                        .clickable {

                            try {
                                navController.navigate(NavUtils.ProfessionalRoutes.PatientSummary.route)
                            } catch (e: Exception) {
                                Log.e("PatientProfileScreen",e.localizedMessage?: "")
                            }
                        }
                        .verticalScroll(
                        rememberScrollState()
                    )
                        .constrainAs(iaSummaryCard){
                            top.linkTo(summaryCard.bottom, margin = 8.dp)
                            start.linkTo(parent.start, margin = 10.dp)
                            end.linkTo(parent.end, margin = 10.dp)
                            bottom.linkTo(treatmentButton.top, margin = 8.dp)
                        }
                    ,
                    colors = CardDefaults.cardColors(containerColor = ColorWhite),
                    shape = CardDefaults.elevatedShape,
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 5.dp
                    ),
                ) {Text(
                            text = (uiState.value as SummaryUIState.Success).outputText,
                            textAlign = TextAlign.Center,
                            color = ColorText,
                        )
                }



            }
        }



        ButtonAlaya(
            text = stringResource(R.string.treatment_text_button_professional),
            modifier = Modifier.constrainAs(treatmentButton) {
                start.linkTo(parent.start)
                top.linkTo(iaSummaryCard.bottom, margin = 16.dp)
                end.linkTo(parent.end)
            },
            ButtonStyle.Outlined,
            {navController.navigate(NavUtils.ProfessionalRoutes.ConfigTreatment.route)},
            containerColor = ColorWhite
        )

        ButtonAlaya(
            text = stringResource(R.string.session_text_button_professional),
            modifier = Modifier.constrainAs(sessionButton) {
                start.linkTo(parent.start)
                top.linkTo(treatmentButton.bottom, margin = 16.dp)
                end.linkTo(parent.end)
            },
            style = ButtonStyle.Outlined,
            onClick = {},
            containerColor = ColorWhite
        )
    }
}

