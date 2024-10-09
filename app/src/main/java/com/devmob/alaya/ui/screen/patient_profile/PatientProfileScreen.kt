package com.devmob.alaya.ui.screen.patient_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.navigation.ProfessionalNavigation.NavUtilsProfessional
import com.devmob.alaya.ui.components.Button as ButtonAlaya
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.DashboardCard
import com.devmob.alaya.ui.components.NextAppointmentHeader
import com.devmob.alaya.ui.screen.ContainmentNetwork.Contact.ContactViewModel
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.NavUtils
import java.time.LocalDateTime

@Composable
fun PatientProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val contactViewModel: ContactViewModel = viewModel()
    val phoneNumber = "1166011371"
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
    ) {
        val (image, header, summaryCard, treatmentButton, sessionButton, whatsappButton) = createRefs()

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
            }
        )

        ButtonAlaya(
            text = stringResource(R.string.treatment_text_button_professional),
            modifier = Modifier.constrainAs(treatmentButton) {
                start.linkTo(parent.start)
                top.linkTo(summaryCard.bottom, margin = 16.dp)
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

