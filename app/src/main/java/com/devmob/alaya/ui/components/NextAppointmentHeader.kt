package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun NextAppointmentHeader(name: String,lastName: String, date: LocalDateTime, modifier: Modifier){
    Card(
        colors = CardDefaults.cardColors(containerColor = ColorWhite),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 9.dp
        ),
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, top = 16.dp, bottom = 16.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "$name $lastName",
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = ColorText,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth(),color = ColorQuaternary, thickness = 3.dp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Próxima Sesión:",
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = ColorText,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${formatDate(date)} - \n ${formatTime(date)}hs",
                fontSize = 21.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = ColorText,
            )
        }
    }
}

@Preview()
@Composable
fun NextAppointmentHeaderPreview(name: String = "Mauro", lastName:String = "Catrambone",date: LocalDateTime = LocalDateTime.now()){
    NextAppointmentHeader(name = name, lastName = lastName, date, Modifier)
}

fun formatDate(date: LocalDateTime, pattern: String = "dd/MM/yyyy"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return date.format(formatter)
}

fun formatTime(date: LocalDateTime, pattern: String = "HH:mm"): String {
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return date.format(formatter)
}

