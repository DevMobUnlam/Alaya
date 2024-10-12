package com.devmob.alaya.ui.components

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorBlack
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorSecondary
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun NewCrisisElementCard(
    modifier: Modifier = Modifier,
    placeholderText: String,
    icon: ImageVector,
    onSave: (String) -> Unit = {},
    ){
    
    var text by remember{mutableStateOf("")}
    val context = LocalContext.current
    
    Card(
        colors = CardDefaults.elevatedCardColors(
            containerColor = ColorTertiary
        ),
        shape = RoundedCornerShape(10.dp),
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.padding(10.dp)) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = ColorWhite,
                )
                Spacer(modifier = Modifier.width(12.dp))
                TextField(
                    value = text,
                    onValueChange = {text = it},
                    placeholder = {
                        Text(
                            text = placeholderText,
                            color = ColorGray,
                            fontSize = 14.sp
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedPlaceholderColor = ColorGray,
                        focusedTextColor = ColorBlack,
                        focusedContainerColor = ColorWhite,
                        unfocusedContainerColor = ColorWhite
                    ),
                    shape = RoundedCornerShape(7.dp)
                )

            }

            Spacer(modifier = Modifier.height(2.dp))
            Button(
                modifier = Modifier.shadow(elevation = 2.dp, shape = CircleShape),
                text = "Guardar",
                style = ButtonStyle.Filled,
                onClick = { 
                    if(text.isNotBlank() && text.length > 2) {
                        onSave(text)
                    }else{
                        Toast.makeText(context,"Introduce información válida", Toast.LENGTH_SHORT).show()
                    }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview
@Composable
fun NewCrisisElementCardPreview(){
    NewCrisisElementCard(
        placeholderText = "Lugar...",
        icon = Icons.Filled.LocationOn
    )
}