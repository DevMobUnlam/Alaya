package com.devmob.alaya.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class CrisisEmotion(
    val name: String,
    val icon: ImageVector,
    val intensity: Intensity = Intensity.LOW,
    val isActive: Boolean = false
)
