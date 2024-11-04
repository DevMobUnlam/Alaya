package com.devmob.alaya.ui.screen.patient_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import co.yml.charts.common.model.Point
import com.devmob.alaya.R
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.HorizontalCardCarousel
import com.devmob.alaya.ui.components.NextAppointmentHeader
import com.devmob.alaya.ui.components.SingleLineChartWithGridLines
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactViewModel
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.NavUtils
import java.time.LocalDateTime
import com.devmob.alaya.ui.components.Button as ButtonAlaya

@Composable
fun PatientProfileScreen(
    navController: NavController,
    viewModel: PatientProfileViewModel,
    email: String
) {
    val context = LocalContext.current
    val contactViewModel: ContactViewModel = viewModel()
    val phoneNumber = "1166011371"
    val namePatient = viewModel.patientData?.name
    val surnamePatient = viewModel.patientData?.surname

    LaunchedEffect(Unit) {
        viewModel.getPatientData(email)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
    ) {
        val (header, treatmentButton, sessionButton, whatsappButton, titleCarousel, carousel, titleLineCharts, lineCharts) = createRefs()

        NextAppointmentHeader(
            namePatient ?: "",
            surnamePatient ?: "",
            date = LocalDateTime.now(),
            modifier = Modifier.fillMaxWidth().constrainAs(header) {
                start.linkTo(parent.start, margin = 16.dp)
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

        Text(
            text = "Esta semana",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = ColorText,
            modifier = Modifier.constrainAs(titleCarousel) {
                top.linkTo(whatsappButton.bottom, margin = 8.dp)
                start.linkTo(parent.start, margin = 16.dp)
            }
        )

        HorizontalCardCarousel(
            modifier = Modifier.constrainAs(carousel) {
                top.linkTo(titleCarousel.bottom, margin = 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )

        //TODO llevar al viewModel
        val pointsData = listOf(
            Point(0f, 8f, "Dic"),
            Point(1f, 6f, "Ene"),
            Point(2f, 0f, "Feb"),
            Point(3f, 2f, "Mar"),
            Point(4f, 6f, "Abr"),
            Point(5f, 0f, "May"),
            Point(6f, 4f, "Jun"),
            Point(7f, 5f, "Jul"),
            Point(8f, 0f, "Ago"),
            Point(9f, 2f, "Sep"),
            Point(10f, 1f, "Oct"),
            Point(11f, 5f, "Nov"),
            Point(12f, 0f, "Dic"),
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
        SingleLineChartWithGridLines(pointsData, modifier = Modifier.constrainAs(lineCharts) {
            top.linkTo(titleLineCharts.bottom, margin = 4.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })

        ButtonAlaya(
            text = stringResource(R.string.treatment_text_button_professional),
            modifier = Modifier.constrainAs(treatmentButton) {
                start.linkTo(parent.start)
                top.linkTo(lineCharts.bottom, margin = 16.dp)
                end.linkTo(parent.end)
            },
            ButtonStyle.Outlined,
            { navController.navigate(NavUtils.ProfessionalRoutes.ConfigTreatment.route) },
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

@Preview
@Composable
fun PatientProfileScreenPreview() {
    PatientProfileScreen(
        navController = NavController(LocalContext.current),
        PatientProfileViewModel(GetUserDataUseCase()),
        ""
    )
}
