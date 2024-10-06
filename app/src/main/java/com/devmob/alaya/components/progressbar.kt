package com.devmob.alaya.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Canvas
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devmob.alaya.ui.theme.ColorTertiary

@Composable
fun SegmentedProgressBar(
    totalSteps: Int,
    currentStep: Int,
    segmentWidth: Float = 600f,
    segmentSpacing: Float = 20f,
    filledColor: Color = ColorTertiary,
    unfilledColor: Color = Color(0xFFE0E5F1)
) {
    val totalBarWidth = (segmentWidth - (segmentSpacing * (totalSteps - 1))) / totalSteps

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
    Canvas(
        modifier = Modifier
            .width(segmentWidth.dp)
            .height(8.dp)

    ) {
        for (i in 0 until totalSteps) {
            val startX = i * (totalBarWidth + segmentSpacing)

            drawRoundRect(
                color = if (i < currentStep) filledColor else unfilledColor,
                topLeft = Offset(startX, 0f),
                size = Size(totalBarWidth, size.height),
                cornerRadius = CornerRadius(50f, 50f)
            )
        }}
    }
}

@Composable
fun ProgressBarScreen(modifier: Modifier = Modifier) {
    var currentStep by remember { mutableStateOf(0) }
    val totalSteps = 3

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        SegmentedProgressBar(totalSteps = totalSteps, currentStep = currentStep)

        Spacer(modifier = Modifier.height(19.dp))

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

