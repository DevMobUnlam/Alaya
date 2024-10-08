package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun IconButton(
    symbol: ImageVector,
    text: String,
    size: Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(verticalArrangement = Arrangement.spacedBy((-3).dp),horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier){
        FilledIconButton(
            onClick = onClick,
            enabled = true,
            colors = IconButtonColors(
                containerColor = ColorPrimary,
                contentColor = ColorWhite,
                disabledContentColor = ColorGray,
                disabledContainerColor = ColorWhite),
            modifier = Modifier.size(size)
        ) {
            Icon(symbol ,contentDescription = text, tint= ColorWhite, modifier = Modifier.fillMaxSize(0.75f))
        }
        Text(
            text = text,
            color = ColorText,
            fontSize = 13.sp
        )
    }

}

@Preview(showBackground = true)
@Composable
fun IconButtonPreview(){
    IconButton(Icons.Outlined.Home, "Home", onClick = {}, size = 70.dp)
}
