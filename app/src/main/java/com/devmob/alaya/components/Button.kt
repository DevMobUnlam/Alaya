package com.devmob.alaya.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun Button (
    text: String,
    modifier: Modifier = Modifier,
    style: ButtonStyle = ButtonStyle.Filled,
    onClick: () -> Unit,
    icon: ImageVector? = null //ver bien como vamos a manejar los iconos
) {
    when (style) {
        ButtonStyle.Filled -> {
            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                modifier = modifier
            ) {
                Text(text = text, color = ColorWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        ButtonStyle.Outlined -> {
            OutlinedButton(
                onClick = onClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorPrimary),
                border = BorderStroke(1.dp, ColorPrimary),
                modifier = modifier
            ) {
                Text(text = text, color = ColorText, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
        ButtonStyle.OutlinedWithIcon -> {
            OutlinedButton(
                onClick = onClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ColorPrimary),
                border = BorderStroke(1.dp, ColorPrimary),
                modifier = modifier
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = null,
                            tint = ColorPrimary,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                    Text(text = text, color = ColorText, fontWeight = FontWeight.Bold, fontSize = 16.sp, textAlign = TextAlign.Center,)
                }
            }
        }
    }

}

enum class ButtonStyle {
    Filled,
    Outlined,
    OutlinedWithIcon
}

@Preview(showBackground = true)
@Composable
fun ButtonPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )  {
        Button(
            text = "Botón Lleno",
            onClick = {},
            style = ButtonStyle.Filled,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            text = "Botón Contorneado",
            onClick = {},
            style = ButtonStyle.Outlined,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            text = "Guarda una nota de voz y termina en otro momento",
            onClick = {},
            style = ButtonStyle.OutlinedWithIcon,
            icon = Icons.Filled.FavoriteBorder,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

