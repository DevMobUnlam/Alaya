package com.devmob.alaya.ui.screen.activityDayProfessional

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor

@Composable
fun CardPersonalizedProfessional(
    title: String,
    descripcion: String,
    progress: Int,
    maxProgress: Int,
    modifier: Modifier = Modifier
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF0FBFB)
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorText
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = descripcion,
                        fontSize = 14.sp,
                        color = ColorText
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Progreso semanal: $progress de $maxProgress",
                        fontSize = 14.sp,
                        color = ColorText
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { progress / maxProgress.toFloat() },
                        modifier = Modifier.fillMaxWidth().height(8.dp),
                        color = ColorTertiary,
                        trackColor = LightBlueColor,
                        strokeCap = StrokeCap.Round,
                    )
                }
            }
        }
        Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Info icon",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp),
            tint = ColorText,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewActivityDay(){
    CardPersonalizedProfessional(title = "Meditacion guiada", descripcion = "Hola como te sentis hoy", progress = 3, maxProgress =7 )

}