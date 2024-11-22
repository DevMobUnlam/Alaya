package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor

@Composable
fun Modal(
    show: Boolean,
    title: String,
    description: String? = null,
    primaryButtonText: String,
    secondaryButtonText: String? = null,
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    if (!show) return
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                primaryButtonText,
                Modifier.fillMaxWidth(),
                ButtonStyle.Filled,
                onConfirm
            )
        },
        dismissButton = {
            secondaryButtonText?.let {
                Button(
                    secondaryButtonText,
                    Modifier.fillMaxWidth(),
                    ButtonStyle.Outlined,
                    onDismiss
                )
            }
        },
        containerColor = LightBlueColor,
        title = {
            Text(
                text = title,
                color = ColorText,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        },
        text = {
            if (description != null) {
                Text(
                    text = description,
                    color = ColorText,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )
            }
            }
    )
}

@Preview
@Composable
fun ModalPreview() {
    Modal(
        true,
        "¿Te sentís más calmado ahora?",
        primaryButtonText = "Sí, me siento mejor",
        secondaryButtonText = "No, aún necesito apoyo"
    )
}
