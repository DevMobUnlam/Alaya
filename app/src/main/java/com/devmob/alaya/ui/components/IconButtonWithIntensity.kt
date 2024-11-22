package com.devmob.alaya.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedButton
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
import com.devmob.alaya.domain.model.Intensity
import com.devmob.alaya.ui.theme.ColorDarkOrange
import com.devmob.alaya.ui.theme.ColorEnfado
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorMiedo
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorTristeza
import com.devmob.alaya.ui.theme.ColorWhite

/**
 * IconButton que muestra un selector de intensidad cuando se selecciona. Utilizado en el registro de crisis
 * para seleccionar las emociones y sensaciones corporales.
 * @param isActive Atributo que indica si esta activo el boton o no, el cual se usa para cambiarlo de color.
 * @param onClick Accion a ejecutar una vez se presiona en el iconButton.
 * @param intensity La intensidad que va a mostrar los radioButton, la intensidad seleccionada va a ser la que va a estar pintada.
 * @param onChangedIntensity Accion a ejecutar una vez se cambia la intensidad seleccionada con el tooltip.
 */

@Composable
fun IconButtonWithIntensity(
    symbol: ImageVector,
    text: String,
    size: Dp,
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    onClick: () -> Unit = {},
    intensity: Intensity,
    onChangedIntensity: (Intensity) -> Unit,
) {
    /**
     * showIntensitySelector determina si esta abierto o no el IntensitySelector
     * */
    var showIntensitySelector by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy((-3).dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        AnimatedVisibility(visible = showIntensitySelector) {
            IntensitySelector(
                onIntensityChange = {
                    showIntensitySelector = !showIntensitySelector
                    onChangedIntensity(it)
                },
                selectedIntensity = intensity,
            )
        }
        AnimatedVisibility(visible = !showIntensitySelector) {
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    repeat(intensity.ordinal + 1) {
                        OutlinedButton(
                            modifier = Modifier.size(19.dp),
                            contentPadding = PaddingValues(0.dp),
                            enabled = true,
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isActive && !showIntensitySelector) ColorPrimary else ColorWhite
                            ),
                            border = BorderStroke(width = 1.dp, color = ColorWhite),
                            onClick = { showIntensitySelector = !showIntensitySelector }
                        ) {}
                    }
                }
            }
        }
        FilledIconButton(
            onClick = {
                showIntensitySelector = !isActive
                onClick()
            },
            shape = CircleShape,
            colors = IconButtonColors(
                containerColor = if (isActive) ColorText else ColorPrimary,
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

@Preview
@Composable
fun EmotionIconButtonPreview() {
    IconButtonWithIntensity(
        Icons.Outlined.Refresh,
        "Mareos",
        size = 70.dp,
        isActive = false,
        onChangedIntensity = {},
        intensity = Intensity.MEDIUM,
        onClick = {})
}