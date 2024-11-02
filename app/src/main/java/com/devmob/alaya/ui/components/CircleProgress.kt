package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.ColorText

@Composable
fun CircleProgress(progress: Float) {
    Box(
        modifier = Modifier.size(40.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            progress = progress,
            color = ColorTertiary,
            trackColor = ColorQuaternary,
            strokeWidth = 4.dp,
            modifier = Modifier.size(80.dp)
        )
        Text(
            text = "${(progress * 100).toInt()}%",
            fontSize = 12.sp,
            color = ColorText
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProgressPreview() {
    CircleProgress(progress = 0.7f)
}