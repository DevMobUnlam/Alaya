package com.devmob.alaya.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Canvas
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SegmentedProgressBar(
    totalSteps: Int,
    currentStep: Int,
    segmentWidth: Float = 200f, // Ajustado para hacer los indicadores más largos
    segmentSpacing: Float = 20f, // Ajustado para modificar el espacio entre segmentos
    filledColor: Color = Color(0xFF7D8DF1),
    unfilledColor: Color = Color(0xFFE0E5F1)
) {
    val totalBarWidth = totalSteps * (segmentWidth + segmentSpacing) - segmentSpacing

    Canvas(
        modifier = Modifier
            .width(totalBarWidth.dp)
            .height(20.dp)
    ) {
        for (i in 0 until totalSteps) {
            val startX = i * (segmentWidth + segmentSpacing)

            drawRect(
                color = if (i < currentStep) filledColor else unfilledColor,
                topLeft = Offset(startX, 0f),
                size = Size(segmentWidth, size.height)
            )
        }
    }
}

@Composable
fun ProgressBarScreen() {
    var currentStep by remember { mutableStateOf(0) }
    val totalSteps = 6 // Cambia esto según el número de pasos que tengas

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        SegmentedProgressBar(totalSteps = totalSteps, currentStep = currentStep)

        Spacer(modifier = Modifier.height(20.dp))

        // Botones para avanzar y retroceder entre los pasos
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(onClick = { if (currentStep > 0) currentStep-- }) {
                Text("Anterior")
            }
            Button(onClick = { if (currentStep < totalSteps) currentStep++ }) {
                Text("Siguiente")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProgressBar() {
    ProgressBarScreen()
}

