package com.devmob.alaya.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun CrisisRegisterIconButton(
    imageVector: ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) ColorText else ColorPrimary

    IconButton(
        symbol =imageVector,
        text =text ,
        onClick = { onClick() },
        backgroundColor = backgroundColor,
    )
}