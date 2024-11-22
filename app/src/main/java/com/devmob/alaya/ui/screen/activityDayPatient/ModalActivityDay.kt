package com.devmob.alaya.ui.screen.activityDayPatient

import android.Manifest.permission.RECORD_AUDIO
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.ButtonStyle
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.LightBlueColor
import com.devmob.alaya.utils.VoiceToText
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ActivityDayCard(
    onSave: (String) -> Unit,
    onSkip: () -> Unit
) {
    val textState = remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current
    val voiceToText = remember{VoiceToText(context)}
    val state by voiceToText.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var isPressed by remember {mutableStateOf(false)}
    var canRecord by remember{mutableStateOf(false)}

    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {isGranted ->
            canRecord = isGranted
        }
    )

    LaunchedEffect(state.spokenText){
        if(state.spokenText.isNotEmpty()){
            textState.value = textState.value.copy(text = state.spokenText)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlueColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Contanos cómo te sentiste realizando esta actividad",
                    fontSize = 23.sp,
                    color = ColorText,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))

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

                val backgroundColor by animateColorAsState(if (isPressed) Color.Gray else ColorPrimary)

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = { },
                        containerColor = backgroundColor,
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(70.dp)
                            .pointerInteropFilter { event ->
                                recordAudioLauncher.launch(RECORD_AUDIO)
                                if(!canRecord) return@pointerInteropFilter false
                                when(event.action){
                                    ACTION_DOWN -> {
                                        isPressed = true
                                        coroutineScope.launch{
                                            voiceToText.startListening()
                                        }
                                        true
                                    }

                                    ACTION_UP -> {
                                        isPressed = false
                                        coroutineScope.launch{
                                            voiceToText.stopListening()
                                        }
                                        true
                                    }

                                    else -> false
                                }
                            },
                        shape = RoundedCornerShape(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Micrófono",
                            tint = Color.White,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    text = "Guardar",
                    onClick = {onSave(textState.value.text)},
                    style = ButtonStyle.Filled,
                    modifier = Modifier.width(300.dp).height(50.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    text = "Omitir",
                    onClick = onSkip,
                    style = ButtonStyle.Outlined,
                    modifier = Modifier.width(300.dp).height(50.dp),
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TestCard(){
    ActivityDayCard(onSave = { /* Acción al guardar */ }, onSkip = { /* Acción al omitir */ })
}