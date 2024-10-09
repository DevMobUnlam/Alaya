package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun CrisisRegistrationElementIconButton(
    symbol: ImageVector,
    text: String,
    size: Dp,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){

    Column(
        verticalArrangement = Arrangement.spacedBy((-3).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        FilledIconButton(
            onClick = onClick,
            enabled = true,
            shape = CircleShape,
            colors = IconButtonColors(
                containerColor = ColorText,
                contentColor = ColorWhite,
                disabledContentColor = ColorGray,
                disabledContainerColor = ColorWhite
            ),
            modifier = Modifier.size(size)
        ) {
            Icon(
                symbol,
                contentDescription = text,
                tint = ColorWhite,
                modifier = Modifier.fillMaxSize(0.80f)
            )
        }
        Text(
            text = text,
            color = ColorText,
            fontSize = 13.sp
        )

    }
}