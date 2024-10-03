package com.devmob.alaya.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorText


@Composable
fun IntensitySelector(onClick: (Intensity) -> Unit) {
    var selectedIntensity by remember { mutableStateOf(0) }

    var context = LocalContext

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        Text(text = context.current.resources.getString(R.string.intensity_selector_text), color = ColorText)

        Spacer(modifier = Modifier.height(1.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (i in Intensity.entries) {

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .border(1.dp, color = Color(0xFF2E4D83), CircleShape)
                        .size(30.dp)
                        .background(color = if (i.ordinal == selectedIntensity) Color(0xFF2E4D83) else Color(0xFFeef3ff))
                        .clickable {onClick}

                )

            }
        }
    }
}

enum class Intensity{
    BAJA,
    MEDIA,
    ALTA
}


@Preview(showBackground = true)
@Composable
fun PreviewIntensitySelector() {
    //IntensitySelector(onClick = {})
}