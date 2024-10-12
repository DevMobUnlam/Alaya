package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun CardContainer(
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    ElevatedCard(
        onClick = { onClick },
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorWhite,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 9.dp),
        content = content,
        enabled = enabled
    )
}