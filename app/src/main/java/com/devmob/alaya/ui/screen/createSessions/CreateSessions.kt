package com.devmob.alaya.ui.screen.createSessions

import android.app.TimePickerDialog
import android.view.ContextThemeWrapper
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.RadioButton
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.devmob.alaya.R
import com.devmob.alaya.domain.model.DayOfWeek
import com.devmob.alaya.ui.components.Button
import com.devmob.alaya.ui.components.Modal
import com.devmob.alaya.ui.theme.ColorText
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


@Composable
fun ScheduleSessionScreen(
    viewModel: SessionViewModel,
    navController: NavController,
) {
    val selectedDayOfWeek = viewModel.selectedDayOfWeek.value
    val showModal = remember { mutableStateOf(false) }
    val nextSessionDate = viewModel.nextSessionDate.value
    val isMultipleSessions = viewModel.isMultipleSessions.value

    fun onConfirmButtonClick() {
        viewModel.calculateNextSessionDate()
        if (viewModel.nextSessionDate.value != null) {
            showModal.value = true
        }
    }

    fun onConfirmSession() {
        if (isMultipleSessions) {
            viewModel.scheduleMonthlySessions()
        } else {
            viewModel.scheduleSession()
        }
        showModal.value = false
    }

    fun onCancelSession() {
        showModal.value = false
    }

    if (showModal.value) {
        Modal(
            show = showModal.value,
            title = "Próxima sesión",
            description = nextSessionDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            primaryButtonText = "Confirmar",
            secondaryButtonText = "Cancelar",
            onDismiss = { showModal.value = false },
            onConfirm = { onConfirmSession() },
            onDismissRequest = { onCancelSession() }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.fondo_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.8f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Seleccione un día y horario para programar sesiones semanales",
                    color = ColorText,
                    modifier = Modifier.padding(bottom = 16.dp),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold,
                )

                WeekdaySelector(
                    selectedDay = selectedDayOfWeek ?: DayOfWeek.MONDAY,
                    onDaySelected = { day -> viewModel.selectedDayOfWeek.value = day }
                )
                Spacer(modifier = Modifier.height(16.dp))

                TimePickers(
                    onStartTimeChange = { date ->
                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                        viewModel.selectedTime.value = timeFormat.format(date)
                    }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = !isMultipleSessions,
                        onClick = { viewModel.isMultipleSessions.value = false },
                                colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFF5A5DE) )
                    )
                    Text(text = "Programar solo una sesión", color = ColorText)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = isMultipleSessions,
                        onClick = { viewModel.isMultipleSessions.value = true },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFFF5A5DE) )
                    )
                    Text(text = "Programar sesiones del mes", color = ColorText)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onConfirmButtonClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                text = "Confirmar sesión"
            )
        }
    }
}


@Composable
fun WeekdaySelector(
    selectedDay: DayOfWeek,
    onDaySelected: (DayOfWeek?) -> Unit
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
                    checked = selectedDay == day,
                    onCheckedChange = { isChecked ->
                        if (isChecked) {
                            onDaySelected(day)
                        } else {
                            onDaySelected(null)
                        }
                    },
                    colors = CheckboxDefaults.colors(checkedColor =Color(0xFFF5A5DE), uncheckedColor = ColorText)
                )
                Text(text = dayName, color = ColorText)
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



