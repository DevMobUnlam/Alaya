package com.tuapp.ui.components

import androidx.compose.foundation.layout.*
import com.devmob.alaya.ui.components.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.FeedbackType
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.components.CardFeedback


@Composable
fun FeedbackScreen(
    feedbackType: FeedbackType,
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
                // Pantalla de Felicitaciones
                CardFeedback(
                    textID = R.string.feedback_felicitaciones,
                    imageID = R.drawable.feedback_felicitaciones
                )
                Column {
                    Button(
                        text = "Registrar el episodio",
                        onClick = {
                        },
                        modifier = Modifier.fillMaxWidth(),
                        style = ButtonStyle.Filled,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        text = "Registrar el episodio más tarde",
                        onClick = {
                        },
                        modifier = Modifier.fillMaxWidth(),
                        style = ButtonStyle.Outlined
                    )
                }
            }
            FeedbackType.TodoVaAEstarBien -> {
                // Pantalla de "Todo va a estar bien"
                CardFeedback(
                    textID = R.string.feedback_va_aestarbien,
                    imageID = R.drawable.feedback_todovaaestarbien
                )
                Column {
                    Button(
                        text = "Mi red de contención",
                        onClick = {
                        },
                        style = ButtonStyle.Filled,
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
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

