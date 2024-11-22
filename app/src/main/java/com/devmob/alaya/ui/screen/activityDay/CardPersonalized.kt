package com.devmob.alaya.ui.screen.activityDay

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor

@Composable
fun ProgressCard(
    modifier: Modifier = Modifier,
    title: String,
    progress: Int,
    maxProgress: Int,
    isChecked: Boolean,
    onCheckClick: () -> Unit = {},
    onHelpIconClick: () -> Unit = {},
) {

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(25.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF0FBFB)
            ),
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCheckClick,
                    modifier = Modifier.size(48.dp)                ) {
                    Icon(
                        imageVector = if (isChecked) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                        contentDescription = "Check icon",
                        modifier = Modifier.size(50.dp),
                        tint = if (isChecked) Color(0xFF4CAF50) else Color.Gray
                    )
                }

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
            imageVector = Icons.AutoMirrored.Outlined.HelpOutline,
            contentDescription = "Info icon",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .clickable(onClick = onHelpIconClick)
                .padding(8.dp),
            tint = ColorText,
        )
    }
}