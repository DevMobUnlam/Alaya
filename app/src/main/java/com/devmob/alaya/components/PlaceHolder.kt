package com.devmob.alaya.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorGray
import com.devmob.alaya.ui.theme.ColorWhite

@Composable
fun PlaceHolder(
    initialTitle: String,
    initialDescription: String
) {
    var titleState by remember { mutableStateOf(initialTitle) }
    var descriptionState by remember { mutableStateOf(initialDescription) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(50.dp))

        CustomCard(
            value = titleState,
            onValueChange = { newValue -> titleState = newValue },
            placeholder = "Título",
            singleLine = true,
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomCard(
            value = descriptionState,
            onValueChange = { newValue -> descriptionState = newValue },
            placeholder = "Descripción",
            singleLine = false,
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.height(200.dp)
        )
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCard(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    singleLine: Boolean,
    fontSize: TextUnit,
    textAlign: TextAlign,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White)

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            TextField(
                value = if (isFocused || value.isNotEmpty()) value else placeholder,
                onValueChange = onValueChange,
                placeholder = {
                    if (!isFocused && value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = ColorWhite,
                            fontSize = fontSize,
                            textAlign = textAlign
                        )
                    }
                },
                textStyle = LocalTextStyle.current.copy(

                    color = ColorGray,
                    fontSize = fontSize,
                    fontWeight = FontWeight.Bold,
                    textAlign = textAlign
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            isFocused = true
                            onValueChange("")
                        }
                    },
                singleLine = singleLine,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
    }
}

