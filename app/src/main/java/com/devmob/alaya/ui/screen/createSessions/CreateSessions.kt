package com.devmob.alaya.ui.screen.createSessions

import android.app.TimePickerDialog
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.DayOfWeek
import com.devmob.alaya.domain.model.Recurrence
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.DateTimePickerSession
import com.devmob.alaya.ui.theme.ColorPrimary
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun ScheduleSessionScreen(
    viewModel: SessionViewModel,
    navController: NavController,
    patientEmail: String
) {
    val session = viewModel.session.value
    val selectedDays = viewModel.selectedDays.value
    val selectedDate = viewModel.selectedDate.value
    val selectedTime = viewModel.selectedTime.value
    val sessionDuration = viewModel.sessionDuration.value
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            DropdownMenu(
                selectedOption = session.recurrence ?: Recurrence.NONE,
                onOptionSelected = { recurrence ->
                    viewModel.updateRecurrence(recurrence)
                }
            )
            when (session.recurrence) {
                Recurrence.WEEKLY, Recurrence.DAILY -> {
                    WeekdaySelector(selectedDays = selectedDays) { days ->
                        viewModel.selectedDays.value = days
                    }
                    TimePickers(
                        onStartTimeChange = { time ->
                            viewModel.selectedTime.value = time.toString()
                        }
                    )
                }

                Recurrence.FORTNIGHTLY, Recurrence.MONTHLY, Recurrence.ONCE -> {
                    DateTimePickerSession(
                        onStartDateChange = { date ->
                            viewModel.selectedDate.value = date
                        },

                        )
                }

                Recurrence.NONE -> Log.d("a", "nada")
                null -> Log.d("a", "null")
            }

            Spacer(modifier = Modifier.height(16.dp))

            DurationInput(
                durationInMinutes = sessionDuration,
                onDurationChange = { duration ->
                    viewModel.updateSessionDuration(duration)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.scheduleSession() },
                text = "Confirmar sesión",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun DropdownMenu(
    selectedOption: Recurrence,
    onOptionSelected: (Recurrence) -> Unit
) {
    val options = Recurrence.values()
    val optionLabels = mapOf(
        Recurrence.ONCE to "Única vez",
        Recurrence.WEEKLY to "Semanal",
        Recurrence.MONTHLY to "Mensual",
        Recurrence.FORTNIGHTLY to "Quincenal",
        Recurrence.DAILY to "Más de una vez por semana",
        Recurrence.NONE to "Seleccione recurrencia"
    )
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .border(1.dp,
                if (expanded) ColorPrimary else Color.Gray,
                shape = RoundedCornerShape(8.dp))
            .padding(8.dp)
            .clickable { expanded = !expanded }
            .fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween, ) {
            Text(
                text = optionLabels[selectedOption] ?: selectedOption.name,
                color = if (selectedOption == Recurrence.NONE) Color.Gray else Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Icono desplegable"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(onClick = {
                    onOptionSelected(option)
                    expanded = false
                }, text = { Text(optionLabels[option] ?: option.name)})
            }
        }
    }
}

@Composable
fun WeekdaySelector(
    selectedDays: List<DayOfWeek>,
    onDaySelected: (List<DayOfWeek>) -> Unit
) {
    val daysOfWeek = listOf(
        DayOfWeek.MONDAY to "Lunes",
        DayOfWeek.TUESDAY to "Martes",
        DayOfWeek.WEDNESDAY to "Miércoles",
        DayOfWeek.THURSDAY to "Jueves",
        DayOfWeek.FRIDAY to "Viernes",
        DayOfWeek.SATURDAY to "Sábado",
        DayOfWeek.SUNDAY to "Domingo"
    )

    Column {
        daysOfWeek.forEach { (day, dayName) ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = selectedDays.contains(day),
                    onCheckedChange = { isChecked ->
                        val updatedDays = if (isChecked) {
                            selectedDays + day
                        } else {
                            selectedDays - day
                        }
                        onDaySelected(updatedDays)
                    },
                    colors = CheckboxDefaults.colors(checkedColor =Color(0xFFF5A5DE))
                )
                Text(dayName)
            }
        }
    }

}

@Composable
fun TimePickers(onStartTimeChange: (Date) -> Unit = {}) {
    val selectedStartTime = remember { mutableStateOf(Date()) }
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

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

    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
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

@Composable
fun DurationInput(
    durationInMinutes: Int,
    onDurationChange: (Int) -> Unit
) {
    var durationText by remember { mutableStateOf(durationInMinutes.toString()) }
    OutlinedTextField(
        value = durationText,
        onValueChange = { newValue ->
            if (newValue.isEmpty()) {
                durationText = ""
                onDurationChange(0)
            } else {
                val newDuration = newValue.toIntOrNull()
                if (newDuration != null) {
                    durationText = newValue
                    onDurationChange(newDuration)
                }
            }
        },
        label = { Text("Duración en minutos") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}



