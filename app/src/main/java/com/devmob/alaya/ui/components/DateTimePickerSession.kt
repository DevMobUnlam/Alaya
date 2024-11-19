package com.devmob.alaya.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.ContextThemeWrapper
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.devmob.alaya.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DateTimePickerSession(
    modifier: Modifier = Modifier,
    onStartDateChange: (Date) -> Unit = {},
    onStartTimeChange: (Date) -> Unit = {},
) {
    val calendar = Calendar.getInstance()

    val selectedStartTime = remember { mutableStateOf(Date()) }
    val selectedEndTime = remember { mutableStateOf(Date()) }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    val context = LocalContext.current

    val datePickerDialogStart = DatePickerDialog(
        ContextThemeWrapper(context, R.style.CustomPickerTheme),
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            selectedStartTime.value = calendar.time
            onStartDateChange(calendar.time)
        },
        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.minDate = Calendar.getInstance().timeInMillis // Solo fechas futuras
    }


    val timePickerDialogStart = TimePickerDialog(
        ContextThemeWrapper(context, R.style.CustomPickerTheme),
        { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            selectedStartTime.value = calendar.time
            onStartTimeChange(calendar.time)
        },
        calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Spacer(modifier = Modifier.height(50.dp))

            OutlinedTextField(
                value = if (selectedStartTime.value != null) dateFormat.format(selectedStartTime.value) else "",
                onValueChange = {},
                label = { Text(text = "Inicio (Fecha)", color = Color(0xFF2E4D83)) },
                readOnly = false,
                trailingIcon = {
                    androidx.compose.material3.IconButton(onClick = { datePickerDialogStart.show() }) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Select Date",
                            tint = Color(0xFF2E4D83)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialogStart.show() },
                shape = RoundedCornerShape(50),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF2E4D83))
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = if (selectedStartTime.value != null) timeFormat.format(selectedStartTime.value) else "",
                onValueChange = {},
                label = { Text(text = "Inicio (Hora)", color = Color(0xFF2E4D83)) },
                readOnly = false,
                trailingIcon = {
                    androidx.compose.material3.IconButton(onClick = { timePickerDialogStart.show() }) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Select Time",
                            tint = Color(0xFF2E4D83)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { timePickerDialogStart.show() },
                shape = RoundedCornerShape(50),
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF2E4D83))
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

    }
}