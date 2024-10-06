package com.devmob.alaya.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.components.SegmentedProgressBar
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.CardFeedback
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.components.TextContainer
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.NavUtils

@Composable
fun FeedbackScreen(){

//    viewModel: FeedbackViewmodel, navController: NavController

    BackHandler {

    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(ColorWhite)
    ) {
        val (  nextButton, goodButton,imageCard) = createRefs()

        Text(
            text = "¡Felicidades!",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF000080),
            modifier = Modifier
                .padding(bottom = 25.dp)
        )

        Spacer(modifier = Modifier.height(70.dp))
        CardFeedback(textID = R.string.feedback_felicitaciones,
            imageID = R.drawable.feedback_felicitaciones)





        Button(
            stringResource(R.string.registrar_episodio_despues),
            Modifier.constrainAs(nextButton) {
                top.linkTo(imageCard.bottom, margin = 12.dp)
                start.linkTo(parent.start, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            ButtonStyle.Outlined,
            onClick = {})

        Button(
            stringResource(R.string.Registrar_episodio),
            Modifier.constrainAs(goodButton) {
                top.linkTo(imageCard.bottom, margin = 12.dp)
                start.linkTo(goodButton.end, margin = 8.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(parent.bottom, margin = 16.dp)
            },
            ButtonStyle.Filled,
            onClick = {  })

        
    }
}

// TODO // Eliminar esta función luego de implementar la descarga de imágenes
fun getImageCard(image: String): Int {
    return when (image) {
        "image_step_1" -> return R.drawable.crisis_step_1
        "image_step_2" -> return R.drawable.crisis_step_2
        "image_step_3" -> return R.drawable.crisis_step_3
        else -> 0
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardFeedbackTodoVaEstarBien() {
    FeedbackScreen()
}

