package com.devmob.alaya.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.devmob.alaya.R
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.TextContainer
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun CrisisHandlingScreen(viewModel: CrisisHandlingViewModel) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorWhite)
    ) {
        val (progressBar, audioIcon, closeIcon, image, title, description, nextButton, goodButton) = createRefs()

        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            tint = ColorText,
            modifier = Modifier
                .size(32.dp)
                .constrainAs(closeIcon) {
                    top.linkTo(parent.top, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        )

        var isVolumeUp by remember { mutableStateOf(true) }
        Image(
            painter = painterResource(id = if (isVolumeUp) R.drawable.volume_up else R.drawable.volume_off),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(32.dp)
                .clickable { isVolumeUp = !isVolumeUp }
                .constrainAs(audioIcon) {
                    top.linkTo(parent.top, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        )

        Text(
            text = "Controlar la respiración",
            color = ColorText,
            fontSize = 24.sp,
            fontWeight = Bold,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(parent.top, margin = 100.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            })

        Image(
            painter = painterResource(id = R.drawable.feedback_felicitaciones),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.constrainAs(image) {
                top.linkTo(title.bottom, margin = 24.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        )

        TextContainer(
            "Poner una mano en el pecho y la otra en el estómago para tomar aire y soltarlo lentamente",
            modifier = Modifier.constrainAs(description) {
                top.linkTo(image.bottom, margin = 80.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        )

        Button("Ya me encuentro bien", Modifier.constrainAs(goodButton) {
            top.linkTo(description.bottom, margin = 48.dp)
            start.linkTo(parent.start, margin = 16.dp)
        }, ButtonStyle.Outlined, {})

        Button("Siguiente", Modifier.constrainAs(nextButton) {
            top.linkTo(description.bottom, margin = 48.dp)
            start.linkTo(goodButton.end, margin = 8.dp)
            end.linkTo(parent.end, margin = 16.dp)
        }, ButtonStyle.Filled, {})
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCrisisScreen() {
    CrisisHandlingScreen(CrisisHandlingViewModel())
}