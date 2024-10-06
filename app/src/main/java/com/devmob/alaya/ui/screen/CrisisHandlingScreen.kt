package com.devmob.alaya.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

    val currentStep = viewModel.currentStep

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
                    top.linkTo(parent.top, margin = 12.dp)
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
                    top.linkTo(parent.top, margin = 12.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        )

        Text(
            text = currentStep.title,
            color = ColorText,
            fontSize = 24.sp,
            fontWeight = Bold,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(audioIcon.bottom, margin = 12.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            })

        /* TODO // Descargar imagen desde la URL
        AsyncImage(
            model = currentStep.image,
            contentDescription = null,
        )*/


        Image(
            painter = painterResource(id = getImage(currentStep.image)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .constrainAs(image) {
                    top.linkTo(title.bottom, margin = 24.dp)
                }
        )

        TextContainer(
            currentStep.description,
            modifier = Modifier.constrainAs(description) {
                top.linkTo(image.bottom, margin = 24.dp)
                start.linkTo(parent.start, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
            }
        )

        Button("Ya me encuentro bien", Modifier.constrainAs(goodButton) {
            top.linkTo(description.bottom, margin = 12.dp)
            start.linkTo(parent.start, margin = 16.dp)
        }, ButtonStyle.Outlined, {})

        Button("Siguiente", Modifier.constrainAs(nextButton) {
            top.linkTo(description.bottom, margin = 12.dp)
            start.linkTo(goodButton.end, margin = 8.dp)
            end.linkTo(parent.end, margin = 16.dp)
        }, ButtonStyle.Filled, onClick = { viewModel.nextStep() })
    }
}

// TODO // Eliminar esta función luego de implementar la descarga de imágenes
fun getImage(image: String): Int {
    return when (image) {
        "image_step_1" -> return R.drawable.crisis_step_1
        "image_step_2" -> return R.drawable.crisis_step_2
        "image_step_3" -> return R.drawable.crisis_step_3
        else -> 0
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewCrisisScreen() {
    CrisisHandlingScreen(CrisisHandlingViewModel())
}