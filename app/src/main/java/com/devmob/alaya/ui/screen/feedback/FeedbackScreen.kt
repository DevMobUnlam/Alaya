package com.devmob.alaya.ui.screen.feedback

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import com.devmob.alaya.ui.components.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.FeedbackType
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.CardFeedback
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.utils.NavUtils


@Composable
fun FeedbackScreen(
    feedbackType: FeedbackType,
    navController: NavHostController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        when (feedbackType) {
            FeedbackType.Felicitaciones -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¡Felicidades!",
                    color = ColorText,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 50.sp),
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                CardFeedback(
                    textID = R.string.feedback_felicitaciones,
                    imageID = R.drawable.feedback_felicitaciones
                )
                Column {
                    Button(
                        text = "Registrar el episodio",
                        onClick = {
                            //TODO Navegar a la pantalla Registrar Crisis
                        },
                        modifier = Modifier.fillMaxWidth(),
                        style = ButtonStyle.Filled,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        text = "Registrar el episodio más tarde",
                        onClick = {navController.navigate( "home") {
                            NavUtils.Routes.Home
                        }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        style = ButtonStyle.Outlined
                    )
                }
            }
            FeedbackType.TodoVaAEstarBien -> {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "¡Todo va a estar bien!",
                    color = ColorText,
                    fontSize = 35.sp,
                    fontWeight = Bold,
                    modifier = Modifier.padding(bottom = 17.dp)
                )
                CardFeedback(
                    textID = R.string.feedback_va_aestarbien,
                    imageID = R.drawable.feedback_todovaaestarbien
                )
                Column {
                    Button(
                        text = "Mi red de contención",
                        onClick = {
                            //TODO Navegar a la pantalla de Mi red de Contencion
                        },
                        style = ButtonStyle.Filled,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {navController.navigate( "home") {
                            NavUtils.Routes.Home
                        }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        text = "Ir a inicio",
                        style = ButtonStyle.Outlined
                    )
                }
            }
        }
    }
}

