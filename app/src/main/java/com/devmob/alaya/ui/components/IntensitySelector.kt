package com.devmob.alaya.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.devmob.alaya.domain.model.Intensity

/**
 * Radiobutton para seleccionar intensidad
 *
 * onIntensityChange - Accion que recibe tipo de Intensidad
 */
@Composable
fun IntensitySelector(
    onIntensityChange: (Intensity) -> Unit,
    selectedIntensity: Intensity,
    modifier: Modifier = Modifier) {



    var context = LocalContext

    ConstraintLayout(modifier = modifier){
        Card(
            colors = CardDefaults.cardColors(containerColor = LightBlueColor),
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .shadow(elevation = 2.dp, shape = SpeechBubbleShape())

        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .height(IntrinsicSize.Max)
                    .padding(top = 6.dp, bottom = 8.dp, start = 12.dp, end = 12.dp)
            ) {
                Text(text = context.current.resources.getString(R.string.intensity_selector_text), color = ColorText,fontSize = 16.sp)
                Spacer(modifier = Modifier.height(1.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ){
                    for (i in Intensity.entries) {
                        OutlinedButton(
                            modifier= Modifier.size(15.dp),
                            contentPadding = PaddingValues(0.dp),
                            onClick = {
                                onIntensityChange(i)
                            },
                            shape = CircleShape,
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (i == selectedIntensity) ColorText else LightBlueColor
                            ),
                            border = BorderStroke(width = 1.dp, color = ColorText)
                        ){

                        }

                    }
                }
            }
        }
    }



}



class SpeechBubbleShape() : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val rect = Rect(0f, 0f, size.width*0.95f, size.height * 0.95f)
            val cornerRadius = CornerRadius(12f, 12f)
            addRoundRect(RoundRect(rect,cornerRadius))
            moveTo(size.width * 0.47f, size.height * 0.95f)
            lineTo(size.width * 0.53f, size.height *0.95f)
            lineTo(size.width * 0.5f, size.height*1.05f)
            close()
        }
        return Outline.Generic(path)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewIntensitySelector() {
    IntensitySelector(onIntensityChange = {}, selectedIntensity = Intensity.LOW)
}


