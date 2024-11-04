package com.devmob.alaya.ui.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun CrisisRegisterIconButton(
    size: Dp = 50.dp,
    imageVector: ImageVector,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) ColorText else ColorPrimary

    IconButton(
        symbol =imageVector,
        size = size,
        text =text ,
        onClick = { onClick() },
        backgroundColor = backgroundColor,
    )
}