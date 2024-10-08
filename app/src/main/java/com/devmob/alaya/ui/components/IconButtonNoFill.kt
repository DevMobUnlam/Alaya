package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import dashedBorder

/**
 * Boton de Icono sin relleno
 *
 * text - El texto va a ir debajo del boton
 */
@Composable
fun IconButtonNoFill(
    text : String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(IntrinsicSize.Min)
    )
    {
        FilledIconButton(
            onClick = onClick,
            enabled = true,
            shape = CircleShape,
            colors = IconButtonColors(
                containerColor = Color.White,
                contentColor = ColorWhite,
                disabledContentColor = ColorGray,
                disabledContainerColor = ColorWhite
            ),
            modifier = modifier.dashedBorder(
                color = ColorPrimary,
                shape = CircleShape,
                strokeWidth = 1.dp,
                dashLength = 2.dp,
                cap = StrokeCap.Square
            )
        ) {
            Icon(Icons.Filled.Add ,contentDescription = text, tint= ColorPrimary, modifier = Modifier.fillMaxSize(0.70f))
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            color = ColorText,
            fontSize = 13.sp,
            lineHeight = 13.sp,
            textAlign = TextAlign.Center,
            maxLines = 3
        )
    }
}

@Preview(showBackground = true)
@Composable
fun IconButtonNoFillPreview(){
    IconButtonNoFill(text = "Añadir emocion", onClick = {})
}
