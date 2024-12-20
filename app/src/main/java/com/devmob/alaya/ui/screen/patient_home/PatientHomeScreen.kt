package com.devmob.alaya.ui.screen.patient_home

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.outlined.Checklist
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Mood
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.ui.components.Card
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.utils.NavUtils

@Composable
fun PatientHomeScreen(viewmodel: PatientHomeScreenViewmodel, navController: NavController) {

    val shouldShowModal = viewmodel.shouldShowInvitation
    val nameProfessional = viewmodel.nameProfessional
    val namePatient = viewmodel.namePatient
    val greetingMessage = viewmodel.greetingMessage

    LaunchedEffect(Unit) {
        viewmodel.fetchPatient()
        viewmodel.updateGreetingMessage()
        viewmodel.checkProfessionalInvitation()
        viewmodel.updateCrisisSteps()
    }
    
    InvitationModal(nameProfessional, shouldShowModal, viewmodel)

    Image(
        painter = painterResource(id = R.drawable.fondo_home),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ConstraintLayout(
            modifier = Modifier
        ) {
            val (greetingText, cardColumn) = createRefs()
            Text(
                text = "Hola ${namePatient}\n¡${greetingMessage}!",
                fontSize = 31.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                color = ColorText,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier
                    .constrainAs(greetingText) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(cardColumn) {
                        top.linkTo(greetingText.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)
            ) {
                Card(
                    title = "¿Cómo me siento hoy?",
                    subtitle = "Evalúa tu estado emocional",
                    onClick = { },
                    leftIcon = Icons.Outlined.Mood,
                    rightIcon = Icons.Filled.ArrowForwardIos
                )
                Card(
                    title = "Mi red de contención",
                    subtitle = "Accede a tus contactos de confianza",
                    onClick = { navController.navigate(NavUtils.PatientRoutes.ContainmentNetwork.route) },
                    leftIcon = Icons.Outlined.Groups,
                    rightIcon = Icons.Filled.ArrowForwardIos
                )
                Card(
                    title = "Actividades diarias",
                    subtitle = "Prácticas diarias para mejorar el control emocional",
                    onClick = {navController.navigate(NavUtils.PatientRoutes.ActivityDay.route) },
                    leftIcon = Icons.Outlined.Checklist,
                    rightIcon = Icons.Filled.ArrowForwardIos
                )
                Card(
                    title = "Registro de crisis",
                    subtitle = "Registra detalles del episodio para entender y mejorar tu manejo en estos momentos",
                    onClick = { navController.navigate(NavUtils.PatientRoutes.CrisisRegistration.route) },
                    leftIcon = Icons.Outlined.Description,
                    rightIcon = Icons.Filled.ArrowForwardIos,
                )
            }
        }
    }
}

@Composable
private fun InvitationModal(
    nameProfessional: String,
    shouldShowModal: Boolean,
    viewmodel: PatientHomeScreenViewmodel
) {
    Modal(
        title = stringResource(R.string.title_modal_patient_invitation),
        description = stringResource(
            R.string.description_modal_patient_invitation,
            nameProfessional,
            nameProfessional
        ),
        show = shouldShowModal,
        primaryButtonText = stringResource(R.string.accept),
        secondaryButtonText = stringResource(R.string.decline),
        onConfirm = { viewmodel.acceptInvitation() },
        onDismiss = { viewmodel.rejectInvitation() },
    )
}
