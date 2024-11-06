package com.devmob.alaya.ui.screen.patient_profile

import androidx.compose.ui.graphics.Color

sealed class CarouselItem(val title: String, val colorBackground: Color) {
    class GenerateSummary(colorBackground: Color) :
        CarouselItem("Generar\nResumen", colorBackground)

    class Crisis(title: String, val count: String, colorBackground: Color) :
        CarouselItem(title, colorBackground)

    class Activities(title: String, val progress: Float, colorBackground: Color) :
        CarouselItem(title, colorBackground)

    class Tools(
        title: String,
        colorBackground: Color,
        val tools: List<ToolProgress>
    ) : CarouselItem(title, colorBackground)
}

data class ToolProgress(val name: String, val progress: Float)
