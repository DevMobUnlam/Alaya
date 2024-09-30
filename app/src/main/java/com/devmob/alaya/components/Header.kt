package com.devmob.alaya.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor

@Composable
fun Header(name: String){
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(),
        colors = CardColors(containerColor = LightBlueColor, contentColor = ColorText, disabledContentColor = ColorPrimary, disabledContainerColor = ColorGray),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier =
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
                Text(
                    text = "Hola $name,\r\n¡Buen día!",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.patients_today),
                    fontSize = 21.sp,
                    textAlign = TextAlign.Center,
                    color = ColorText,
                )
        }
    }
}

@Preview
@Composable
fun HeaderPreview(name: String = "Mauro"){
    Header(name)
}