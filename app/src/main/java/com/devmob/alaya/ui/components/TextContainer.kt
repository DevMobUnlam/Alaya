package com.devmob.alaya.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun TextContainer(text: String, modifier: Modifier) {
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp)
        .background(ColorQuaternary.copy(alpha = 0.35f), RoundedCornerShape(8.dp)),
        content = {
            Text(
                text = text,
                color = ColorText,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TextContainerPreview() {
    TextContainer("Poner una mano en el pecho y la otra en el estómago para tomar aire y soltarlo lentamente.", Modifier)
}