package com.devmob.alaya.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.devmob.alaya.ui.theme.ColorText
import com.devmob.alaya.ui.theme.ColorWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title: String, onBackClick: () -> Unit) {
    Column{
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = ColorWhite,
                        titleContentColor = ColorText,
                    ),
                    title = {
                        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    },
                    modifier = Modifier.height(60.dp).windowInsetsPadding(WindowInsets.statusBars) ,
                    navigationIcon = {
                        IconButton(onClick = { onBackClick() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = ColorText
                            )
                        }
                    }
                )
                Divider(
                    color = Color.Gray,
                    thickness = 1.dp
                )
            }
        }
