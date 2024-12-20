package com.devmob.alaya.ui.screen.patient_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.data.CrisisRepositoryImpl
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.domain.CrisisRepository
import com.devmob.alaya.domain.GetRegistersUseCase
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.HorizontalCardCarousel
import com.devmob.alaya.ui.components.NextAppointmentHeader
import com.devmob.alaya.ui.components.SingleLineChartWithGridLines
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactViewModel
import com.devmob.alaya.ui.screen.createSessions.SessionViewModel
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.NavUtils
import java.util.Date
import com.devmob.alaya.ui.components.Button as ButtonAlaya

@Composable
fun PatientProfileScreen(
    navController: NavController,
    viewModel: PatientProfileViewModel,
    email: String
) {
    val context = LocalContext.current
    val contactViewModel: ContactViewModel = viewModel()
    val phoneNumber = viewModel.patientData?.phone
    val namePatient = viewModel.patientData?.name
    val surnamePatient = viewModel.patientData?.surname
    val image = viewModel.patientData?.profileImage

    LaunchedEffect(Unit) {
        viewModel.cleanViewModel()
        viewModel.getNextSession(email)
        viewModel.getPatientData(email)
    }
    val nextSession by viewModel.nextSession.collectAsState()

    if (viewModel.isLoading) {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center),
            color = ColorPrimary
        )
    } else {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState()),
        ) {
            val (header, treatmentButton, sessionButton, titleCarousel, carousel, titleLineCharts, lineCharts) = createRefs()

            NextAppointmentHeader(
                namePatient ?: "",
                surnamePatient ?: "",
                date = nextSession?.date,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(header) {
                        start.linkTo(parent.start, margin = 16.dp)
                        top.linkTo(parent.top, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                contactViewModel, phoneNumber, context, image ?: ""
            )

            ButtonAlaya(
                text = stringResource(R.string.treatment_text_button_professional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .constrainAs(treatmentButton) {
                        start.linkTo(parent.start)
                        top.linkTo(header.bottom, margin = 4.dp)
                        end.linkTo(parent.end)
                    },
                ButtonStyle.Outlined,
                {
                    navController.navigate(
                        NavUtils.ProfessionalRoutes.ConfigTreatment.route.replace(
                            "{patientEmail}",
                            email
                        )
                    )
                },
                containerColor = ColorWhite
            )

            ButtonAlaya(
                text = stringResource(R.string.session_text_button_professional),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .constrainAs(sessionButton) {
                        start.linkTo(parent.start)
                        top.linkTo(treatmentButton.bottom, margin = 2.dp)
                        end.linkTo(parent.end)
                    },
                style = ButtonStyle.Outlined,
                onClick = {
                    navController.navigate(
                        NavUtils.ProfessionalRoutes.CreateSessions.route.replace(
                            "{patientEmail}",
                            email
                        )
                    )
                          },
                containerColor = ColorWhite
            )

            Text(
                text = "Esta semana",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = ColorText,
                modifier = Modifier.constrainAs(titleCarousel) {
                    top.linkTo(sessionButton.bottom, margin = 8.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
            )

            HorizontalCardCarousel(
                modifier = Modifier.constrainAs(carousel) {
                    top.linkTo(titleCarousel.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, viewModel.getCarouselItems(), onGenerateIASummary = {navController.navigate("patient_ia_summary/$email")},
                activityDayProfessional = {navController.navigate("${NavUtils.ProfessionalRoutes.ActivityDayProfessional.route}/$email") }
            )

            Text(
                text = "Eventos de crisis",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = ColorText,
                modifier = Modifier.constrainAs(titleLineCharts) {
                    top.linkTo(carousel.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
            )

            if(viewModel.getPointsData().isEmpty()) {
                Text(
                    text = "No hay eventos de crisis",
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = ColorText,
                    modifier = Modifier.constrainAs(lineCharts) {
                        top.linkTo(titleLineCharts.bottom, margin = 4.dp)
                        start.linkTo(parent.start, margin = 16.dp)
                    }
                )
            } else{
                SingleLineChartWithGridLines(
                    viewModel.getPointsData(),
                    modifier = Modifier.constrainAs(lineCharts) {
                        top.linkTo(titleLineCharts.bottom, margin = 4.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
            }
        }
    }
}

