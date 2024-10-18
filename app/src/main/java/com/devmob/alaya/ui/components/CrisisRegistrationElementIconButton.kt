package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.ui.theme.LightBlueColor


/**
 * IconButton para representar un elemento de crisis, sin intensitySelector
 *
 * symbol -> Icono
 * text -> Nombre del elemento
 * size -> Tamaño del Icono
 * isActive -> Si el icono esta activo o no
 * onClick -> Accion a ejecutar una vez se presiona el boton
 */
@Composable
fun CrisisRegistrationElementIconButton(
    modifier: Modifier = Modifier,
    symbol: ImageVector,
    text: String,
    size: Dp,
    isActive: Boolean = false,
    onClick: () -> Unit = {}
){

    // TODO() Este atributo parece no funcionar, ya que no genera la recomposicion, y no cambia el color del boton
    val buttonColor = if(isActive) ColorText else ColorPrimary

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
                containerColor = buttonColor,
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
                modifier = Modifier.fillMaxSize(0.75f)
            )
        }
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = text,
            color = ColorText,
            fontSize = 16.sp
        )

    }
}