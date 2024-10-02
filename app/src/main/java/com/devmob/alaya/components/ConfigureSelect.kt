package com.devmob.alaya.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun ConfigureSelect(stepTitle: String,
                    stepItems: List<Pair<String, String>> ){

    var expanded by remember { mutableStateOf(false) }

       Card(
           shape = RoundedCornerShape(40.dp),
           elevation = CardDefaults.cardElevation(8.dp),
           modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp)
               .clickable { expanded = !expanded }
       ) {
           Column(
               modifier = Modifier
                   .background(ColorWhite)
                   .padding(16.dp)
           ) {
               Row(
                   verticalAlignment = Alignment.CenterVertically,
                   modifier = Modifier.fillMaxWidth()
               ) {
                   Text(
                       text = stepTitle,
                       fontSize = 20.sp,
                       fontWeight = FontWeight.Bold,
                       color = ColorPrimary,
                       modifier = Modifier.weight(1f)
                   )
                   IconButton(onClick = { expanded = !expanded }) {
                       Icon(tint = ColorPrimary,
                           imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                           contentDescription = if (expanded) "Collapse" else "Expand"
                       )
                   }
               }
               // Animación para mostrar/ocultar el contenido
               AnimatedVisibility(
                   visible = expanded,
                   enter = androidx.compose.animation.expandVertically(animationSpec = tween(100)),
                   exit = androidx.compose.animation.shrinkVertically(animationSpec = tween(100))
               ) {
                   Column {
                       Spacer(modifier = Modifier.height(8.dp))

                       // Generar los ítems de la lista de pasos
                       stepItems.forEach { (title, description) ->
                           StepItem(title = title, description = description)
                           Spacer(modifier = Modifier.height(8.dp))
                       }
                   }
               }
           }
       }
}
@Composable
fun StepItem(title: String, description: String) {
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = ColorPrimary
        )
        Text(
            text = description,
            fontSize = 14.sp,
            color = ColorGray
        )
    }
}
