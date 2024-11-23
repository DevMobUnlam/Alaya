package com.devmob.alaya.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    header: String,
    content: @Composable () -> Unit = {},
){

    var isExpanded by remember { mutableStateOf(false) }
    var onExpandClick = remember { { isExpanded = !isExpanded } }


    Card(
        modifier = modifier
            .padding(8.dp)
        ,shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = ColorWhite,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp),
        ){
        Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(vertical = 6.dp)){


                Text(
                    modifier = Modifier.weight(3.5F),
                    text = header,
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = ColorText,
                )
                Icon(
                    modifier = Modifier.clickable{onExpandClick()}.weight(0.5F),
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Contraer" else "Expandir",
                    tint = ColorTertiary
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                content()
            }
        }
    }
}
