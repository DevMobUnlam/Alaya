package com.devmob.alaya.ui.screen.activityDayProfessional

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalActivityDayProfessional(){

        val textState = remember { mutableStateOf(TextFieldValue()) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LightBlueColor),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = textState.value,
                        onValueChange = { textState.value = it },
                        label = { Text("Titulo") },
                        isError = false,
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = ColorPrimary,
                            unfocusedBorderColor = ColorPrimary,
                            focusedLabelColor = ColorText,
                            unfocusedLabelColor = ColorText,
                            focusedTextColor = ColorText
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = textState.value,
                        onValueChange = { textState.value = it },
                        label = { Text("Descripción") },
                        isError = false,
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = ColorPrimary,
                            unfocusedBorderColor = ColorPrimary,
                            focusedLabelColor = ColorText,
                            unfocusedLabelColor = ColorText,
                            focusedTextColor = ColorText
                        )
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = textState.value,
                        onValueChange = { textState.value = it },
                        label = { Text("Cantidad de veces por semnana*") },
                        isError = false,
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = ColorPrimary,
                            unfocusedBorderColor = ColorPrimary,
                            focusedLabelColor = ColorText,
                            unfocusedLabelColor = ColorText,
                            focusedTextColor = ColorText
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        text = "Guardar",
                        onClick = {},
                        style = ButtonStyle.Filled,
                        modifier = Modifier.width(300.dp).height(50.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        text = "Descartar",
                        onClick = {},
                        style = ButtonStyle.Outlined,
                        modifier = Modifier.width(300.dp).height(50.dp),
                    )

                }
            }
        }
    }




@Preview(showBackground = true)
@Composable
fun PreviewModalAactivityModal(){

    ModalActivityDayProfessional()
}


