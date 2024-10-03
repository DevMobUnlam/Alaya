package com.devmob.alaya.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateTimePicker() {
    val calendar = Calendar.getInstance()

    // Estados para fecha y hora seleccionada
    var selectedStartDate by remember { mutableStateOf(calendar.time) }
    var selectedStartTime by remember { mutableStateOf(calendar.time) }
    var selectedEndDate by remember { mutableStateOf(calendar.time) }
    var selectedEndTime by remember { mutableStateOf(calendar.time) }

    // Formatos para mostrar fecha y hora
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    // Variables para mostrar el diálogo de selección de fecha y hora
    val datePickerDialog = DatePickerDialog(
        LocalContext.current,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedStartDate = calendar.time
            selectedEndDate = calendar.time
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    )

    val timePickerDialog = TimePickerDialog(
        LocalContext.current,
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            selectedStartTime = calendar.time
            selectedEndTime = calendar.time
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Inicio: Selector de fecha y hora
        Column {
            OutlinedTextField(
                value = dateFormat.format(selectedStartDate),
                onValueChange = {},
                label = { Text(text = "Inicio") },
                trailingIcon = {
                    IconButton(onClick = {
                        datePickerDialog.show()
                    }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    }
                },
                readOnly = true,
                shape = RoundedCornerShape(50), // Forma ovalada
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Ajustar la altura para mantener la proporción ovalada
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = timeFormat.format(selectedStartTime),
                onValueChange = {},
                trailingIcon = {
                    IconButton(onClick = {
                        timePickerDialog.show()
                    }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Select Time")
                    }
                },
                readOnly = true,
                shape = RoundedCornerShape(50), // Forma ovalada
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Ajustar la altura
            )
        }

        // Fin: Selector de fecha y hora
        Column {
            OutlinedTextField(
                value = dateFormat.format(selectedEndDate),
                onValueChange = {},
                label = { Text(text = "Fin") },
                trailingIcon = {
                    IconButton(onClick = {
                        datePickerDialog.show()
                    }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    }
                },
                readOnly = true,
                shape = RoundedCornerShape(50), // Forma ovalada
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Ajustar la altura
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = timeFormat.format(selectedEndTime),
                onValueChange = {},
                trailingIcon = {
                    IconButton(onClick = {
                        timePickerDialog.show()
                    }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Select Time")
                    }
                },
                readOnly = true,
                shape = RoundedCornerShape(50), // Forma ovalada
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp) // Ajustar la altura
            )
        }
    }
}