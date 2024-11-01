package com.devmob.alaya.ui.screen.send_invitation_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.devmob.alaya.R
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.ui.theme.LightBlueColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendInvitationScreen(
    viewModel: SendInvitationViewModel
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isInvitationSent by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    var currentEmail = FirebaseClient().auth.currentUser?.email

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.send_invitation))
    val successComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.send))

    if (composition != null) {
        isLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBlueColor)
    ) {

        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = ColorPrimary
            )
        } else if (isInvitationSent) {
            val progress by animateLottieCompositionAsState(
                composition = successComposition,
                iterations = 1,
                isPlaying = true
            )
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Invitación enviada",
                    style = MaterialTheme.typography.titleMedium,
                    color = ColorText,
                    modifier = Modifier.padding(top = 16.dp)
                )
                LottieAnimation(
                    composition = successComposition,
                    progress = { progress },
                    modifier = Modifier.size(300.dp)
                )


            }
        } else {

            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ingrese el email del paciente:",
                style = MaterialTheme.typography.titleLarge,
                color = ColorText
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = false
                },
                isError = emailError,
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = ColorPrimary,
                    unfocusedIndicatorColor = Color(0xFFC0C0C0),
                    focusedLabelColor = ColorPrimary,
                    containerColor = ColorWhite
                ),
                placeholder = { Text("ejemplo@gmail.com", color = Color.LightGray) }
            )

            if (emailError) {
                Text(
                    text = "Por favor, ingrese un email válido.",
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (isValidEmail(email)) {
                        if (currentEmail != null) {
                            viewModel.sendInvitation(email, currentEmail)
                        }
                        isInvitationSent = true
                        email = ""
                    } else {
                        emailError = true
                    }
                },
                text = "Enviar invitación"
            )
        }
    }
}

private fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}