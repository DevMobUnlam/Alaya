package com.devmob.alaya.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun Header(name: String){
    Card(
        colors = CardDefaults.cardColors(containerColor = ColorWhite),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 9.dp
        ),
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .padding(20.dp)
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(13.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
                Text(
                    text = "Hola $name,",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "¡Buen día!",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                    )
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = ColorQuaternary, thickness = 3.dp)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(R.string.patients_today),
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    color = ColorText,
                    maxLines = 3
                )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun HeaderPreview(name: String = "Mauro"){
    Header(name)
}