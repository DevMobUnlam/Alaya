package com.devmob.alaya.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class CrisisTool(
    val id: String, //cochinada temporal para que coincida con la tool de la db xd
    val name: String,
    val icon: ImageVector,
    val isActive: Boolean = true
)
