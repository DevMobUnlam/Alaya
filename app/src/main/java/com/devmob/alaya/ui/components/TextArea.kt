package com.devmob.alaya.ui.components

import android.Manifest.permission.RECORD_AUDIO
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorPrimary
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite
import com.devmob.alaya.utils.VoiceToText
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun TextArea(
    modifier: Modifier = Modifier,
    title: String
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf(TextFieldValue("")) }
    val voiceToText = remember { VoiceToText(context) }
    val state by voiceToText.state.collectAsState()
    var isPressed by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var canRecord by remember { mutableStateOf(false) }

    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            canRecord = isGranted
        }
    )

    LaunchedEffect(Unit) {
        recordAudioLauncher.launch(RECORD_AUDIO)
    }

    LaunchedEffect(state.spokenText) {
        if (state.spokenText.isNotEmpty()) {
            text = text.copy(text = state.spokenText)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            fontSize = 30.sp,
            color = ColorText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(30.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                decorationBox = { innerTextField ->
                    if (text.text.isEmpty()) {
                        Text(
                            text = state.spokenText.ifEmpty {
                                "¿Con quién estabas?\n" +
                                        "¿Qué estabas haciendo?\n" +
                                        "¿Qué hiciste después?\n" +
                                        "¿Qué herramientas te ayudaron?\n" +
                                        "a superar la crisis?\n" +
                                        "¿Qué pensamientos tuviste?"
                            },
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                    }
                    innerTextField()
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (canRecord) {
            FloatingActionButton(
                onClick = {},
                shape = CircleShape,
                containerColor = ColorPrimary,
                modifier = Modifier
                    .size(80.dp)
                    .pointerInteropFilter { event ->
                        when (event.action) {
                            ACTION_DOWN -> {
                                isPressed = true
                                coroutineScope.launch {
                                    voiceToText.startListening()
                                }
                                true
                            }

                            ACTION_UP -> {
                                isPressed = false
                                coroutineScope.launch {
                                    voiceToText.stopListening()
                                }
                                true
                            }

                            else -> false
                        }
                    }
            ) {
                Icon(
                    imageVector = if (isPressed) Icons.Rounded.Stop else Icons.Rounded.Mic,
                    contentDescription = "Agregar",
                    tint = ColorWhite,
                    modifier = Modifier.size(35.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}