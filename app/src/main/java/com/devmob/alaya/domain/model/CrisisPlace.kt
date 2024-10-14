package com.devmob.alaya.domain.model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

data class CrisisPlace(
    val name: String = "",
    val icon: ImageVector,
    val isActive: Boolean = false
)
