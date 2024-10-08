package com.devmob.alaya.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun EmotionIconButton(
    symbol: ImageVector,
    text: String,
    size: Dp,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    onClick: () -> Unit,
){

    var showIntensitySelector by remember{mutableStateOf(false)}
        Column(
            verticalArrangement = Arrangement.spacedBy((-3).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {

            AnimatedVisibility(visible = showIntensitySelector){
                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                }
            }

            FilledIconButton(
                onClick = {
                    showIntensitySelector = !showIntensitySelector
                    onClick()
                    },
                enabled = enabled,
                shape = CircleShape,
                colors = IconButtonColors(
                    containerColor = Color(0xFF2E4D83),
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

@Preview
@Composable

fun EmotionIconButtonPreview(){
    EmotionIconButton(Icons.Outlined.Refresh, "Mareos", size = 70.dp, enabled = true, onClick = {})
}