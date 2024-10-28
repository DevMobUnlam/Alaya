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
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.Input
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.ui.theme.LightBlueColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendInvitationScreen(
) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.send_invitation))

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
        } else {

            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                composition = composition,
                progress = {
                    progress
                },
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
                style = MaterialTheme.typography.titleMedium,
                color = ColorText
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = ColorPrimary,
                    unfocusedIndicatorColor = Color(0xFFC0C0C0),
                    focusedLabelColor = ColorPrimary,
                    containerColor = ColorWhite
                ),
                placeholder = { Text("ejemplo@gmail.com", color = Color.LightGray) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {

                },
                text = "Enviar invitación"
            )
        }
    }
}