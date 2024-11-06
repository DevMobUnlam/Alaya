package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.R
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun Header(name: String, greeting:String){
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
            if (name.isEmpty()) {
                // MUESTRO UN LOADING SI EL NOMBRE NO ESTÁ DISPONIBLE PARA EVITAR MOSTRAR PANTALLA CON DATOS VACÍOS
                CircularProgressIndicator(
                    modifier = Modifier.size(40.dp),
                    color = ColorPrimary
                )
            } else {
                Text(
                    text = "Hola $name,",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorText,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
                Text(
                    text = "$greeting!",
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
fun HeaderPreview(name: String = "Mauro",greeting:String = "Buen Dia!"){
    Header(name,greeting)
}