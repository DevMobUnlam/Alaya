package com.devmob.alaya.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.ui.theme.LightBlueColor


@Composable
fun Switch(modifier: Modifier, onChange: (Boolean) -> Unit) {
    var isProfessional by remember { mutableStateOf(false) }
    var size by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .width(200.dp)
            .height(50.dp)
            .background(
                color = LightBlueColor,
                shape = RoundedCornerShape(25.dp)
            )
            .padding(0.dp)
            .onSizeChanged { size = it },
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(size.width.div(5).dp) // TODO Fijarse que no se rompa
                .background(
                    color = ColorPrimary,
                    shape = CircleShape
                )
                .align(if (isProfessional) Alignment.CenterEnd else Alignment.CenterStart)
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.paciente),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isProfessional) ColorGray else ColorWhite,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.profesional),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isProfessional) ColorWhite else ColorGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable {
                    isProfessional = !isProfessional
                    onChange(isProfessional)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSwitch() {
    Switch(Modifier, {})
}