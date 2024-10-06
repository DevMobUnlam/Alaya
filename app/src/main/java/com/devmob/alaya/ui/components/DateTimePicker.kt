package com.devmob.alaya.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DateTimePicker() {
    val calendar = Calendar.getInstance()

    var selectedStartDate by remember { mutableStateOf(calendar.time) }
    var selectedStartTime by remember { mutableStateOf(calendar.time) }
    var selectedEndDate by remember { mutableStateOf(calendar.time) }
    var selectedEndTime by remember { mutableStateOf(calendar.time) }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val context = LocalContext.current

    val datePickerDialogStart = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedStartDate = calendar.time
        },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.maxDate = Calendar.getInstance().timeInMillis
    }

    val datePickerDialogEnd = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedEndDate = calendar.time
        },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.maxDate = Calendar.getInstance().timeInMillis
    }

    val timePickerDialogStart = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            selectedStartTime = calendar.time
        },
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    )

    val timePickerDialogEnd = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            selectedEndTime = calendar.time
        },
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            OutlinedTextField(
                value = dateFormat.format(selectedStartDate),
                onValueChange = {},
                label = { Text(text = "Inicio (Fecha)") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialogStart.show() }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialogStart.show() },
                shape = RoundedCornerShape(50),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = timeFormat.format(selectedStartTime),
                onValueChange = {},
                label = { Text(text = "Inicio (Hora)") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { timePickerDialogStart.show() }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Select Time")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { timePickerDialogStart.show() },
                shape = RoundedCornerShape(50),
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            OutlinedTextField(
                value = dateFormat.format(selectedEndDate),
                onValueChange = {},
                label = { Text(text = "Fin (Fecha)") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { datePickerDialogEnd.show() }) {
                        Icon(Icons.Default.CalendarToday, contentDescription = "Select Date")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialogEnd.show() },
                shape = RoundedCornerShape(50),
                textStyle = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = timeFormat.format(selectedEndTime),
                onValueChange = {},
                label = { Text(text = "Fin (Hora)") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { timePickerDialogEnd.show() }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Select Time")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { timePickerDialogEnd.show() },
                shape = RoundedCornerShape(50),
                textStyle = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDateTimePicker()
{
    Surface {
        DateTimePicker()
    }
}