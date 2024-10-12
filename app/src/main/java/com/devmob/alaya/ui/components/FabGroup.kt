package com.devmob.alaya.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.SentimentSatisfied
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.ui.theme.LightBlueColor


@Composable
fun FabGroup(modifier: Modifier = Modifier){

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            if (expanded) {
                FabOptionWithText(
                    onClick = { /* Acción para "¿Cómo te sentís?" */ },
                    icon = Icons.Outlined.SentimentSatisfied,
                    description = "¿Cómo te sentís?",
                    labelText = "¿Cómo te sentís?"
                )
                FabOptionWithText(
                    onClick = { /* Acción para "Registro de gratitud" */ },
                    icon = Icons.Default.FavoriteBorder,
                    description = "Registro de gratitud",
                    labelText = "Registro de gratitud"
                )
                FabOptionWithText(
                    onClick = { /* Acción para "Escribe algo" */ },
                    icon = Icons.Outlined.Edit,
                    description = "Escribe algo",
                    labelText = "Escribe algo"
                )
            }

            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = ColorPrimary,
                contentColor = ColorWhite,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(70.dp)
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.Close else Icons.Default.Add,
                    contentDescription = if (expanded) "Cerrar menú" else "Abrir menú",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun FabOptionWithText(
    onClick: () -> Unit,
    icon: ImageVector,
    description: String,
    labelText: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = labelText,
            modifier = Modifier.padding(end = 8.dp),
            color = ColorText,
            fontSize = 16.sp
        )

        FloatingActionButton(
            onClick = onClick,
            containerColor = LightBlueColor,
            contentColor = ColorPrimary,
            modifier = Modifier
                .size(60.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = description,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}