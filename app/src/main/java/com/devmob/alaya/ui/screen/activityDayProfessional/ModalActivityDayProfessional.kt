package com.devmob.alaya.ui.screen.activityDayProfessional

import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalActivityDayProfessional(
    viewModel: ActivityDayProfessionalViewModel,
    navController: NavController,
    email: String = "",
){

    val context = LocalContext.current

    val titleState = remember { mutableStateOf(TextFieldValue()) }
    val descriptionState = remember { mutableStateOf(TextFieldValue()) }
    val countState = remember { mutableStateOf(TextFieldValue())}

    LaunchedEffect(Unit) {
        titleState.value = TextFieldValue(viewModel.focusedActivity.title)
        descriptionState.value = TextFieldValue(viewModel.focusedActivity.description)
        countState.value = TextFieldValue((viewModel.focusedActivity.maxProgress.toString()))
    }

            LaunchedEffect(viewModel.saveActivityResult.value){
                when(viewModel.saveActivityResult.value){
                null -> {}
                true -> {
                    navController.popBackStack()
                    Toast.makeText(
                        context,
                        "¡La actividad se ha guardado con exito!",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                false -> {
                    Toast.makeText(
                        context,
                        "Error al guardar la actividad",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }




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
                        value = titleState.value,
                        onValueChange = { titleState.value = it },
                        label = { Text("Título") },
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
                        value = descriptionState.value,
                        onValueChange = {descriptionState.value = it},
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
                        value = countState.value,
                        onValueChange = {countState.value = it},
                        label = { Text("Cantidad de veces por semana") },
                        isError = false,
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        onClick = {
                            if(titleState.value.text.isNotEmpty() && countState.value.text.isNotEmpty() && countState.value.text.toInt() !=0){
                                viewModel.onSaveActivity(email,titleState.value.text, descriptionState.value.text,countState.value.text.toInt()
                                )
                            }
                            },
                        style = ButtonStyle.Filled,
                        modifier = Modifier.width(300.dp).height(50.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        text = "Descartar",
                        onClick = {
                        viewModel.onDismiss()
                        navController.popBackStack()
                        },
                        style = ButtonStyle.Outlined,
                        modifier = Modifier.width(300.dp).height(50.dp),
                    )

                }
            }
        }
    }





