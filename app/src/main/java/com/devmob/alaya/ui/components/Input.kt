package com.devmob.alaya.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Gray) },
        placeholder = { Text(placeholder, color = Color.LightGray) },
        singleLine = true,
        textStyle = TextStyle(fontSize = 16.sp),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType),
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = ColorPrimary,
            unfocusedIndicatorColor = Color(0xFFC0C0C0),
            focusedLabelColor = ColorPrimary,
            containerColor = ColorWhite
        ),
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(56.dp)
            .background(ColorWhite)
    )
}