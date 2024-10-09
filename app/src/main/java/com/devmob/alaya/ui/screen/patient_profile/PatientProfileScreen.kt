package com.devmob.alaya.ui.screen.patient_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.devmob.alaya.R
import com.devmob.alaya.navigation.ProfessionalNavigation.NavUtilsProfessional
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.NextAppointmentHeader
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.NavUtils
import java.time.LocalDateTime

@Composable
fun PatientProfileScreen(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {

        val (image, header, contactButton, summaryCard, treatmentButton, sessionButton) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
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
            Modifier.constrainAs(header) {
                start.linkTo(image.end, margin = 8.dp)
                top.linkTo(parent.top, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            })

        Button(
            stringResource(R.string.send_message),
            Modifier.constrainAs(contactButton) {
                top.linkTo(header.bottom, margin = 4.dp)
                start.linkTo(header.start)
                end.linkTo(header.end)
            },
            ButtonStyle.OutlinedWithIcon,
            {},
            icon = Icons.Filled.ChatBubbleOutline,
            containerColor = ColorWhite
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = ColorWhite),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 9.dp
            ),
            modifier = Modifier
                .constrainAs(summaryCard) {
                    top.linkTo(contactButton.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(16.dp)
                .background(color = ColorWhite)) {

            Row(
                modifier = Modifier
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.summary_text_button_professional),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = ColorText,
                )
            }
        }

        Button(
            stringResource(R.string.treatment_text_button_professional),
            Modifier.constrainAs(treatmentButton) {
                start.linkTo(parent.start)
                top.linkTo(summaryCard.bottom, margin = 16.dp)
                end.linkTo(parent.end)
            },
            ButtonStyle.Outlined,
            {navController.navigate(NavUtils.ProfessionalRoutes.ConfigTreatment.route)},
            containerColor = ColorWhite
        )

        Button(
            stringResource(R.string.session_text_button_professional),
            Modifier.constrainAs(sessionButton) {
                start.linkTo(parent.start)
                top.linkTo(treatmentButton.bottom, margin = 16.dp)
                end.linkTo(parent.end)
            },
            ButtonStyle.Outlined,
            {},
            containerColor = ColorWhite
        )
    }}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPatientProfileScreen() {
    val navController = rememberNavController()
    PatientProfileScreen(navController)
}