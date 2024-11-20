package com.devmob.alaya.ui.screen.createSessions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.SessionUseCase
import com.devmob.alaya.domain.model.DayOfWeek
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.Session
import com.devmob.alaya.domain.model.SessionStatus
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SessionViewModel(private val sessionUseCase: SessionUseCase) : ViewModel() {
    val selectedDayOfWeek = mutableStateOf<DayOfWeek?>(null)
    val selectedTime = mutableStateOf<String?>("")
    val sessionDuration = mutableStateOf(0)
    val patientEmail = mutableStateOf("")
    val currentEmail = FirebaseClient().auth.currentUser?.email
    var nextSessionDate = mutableStateOf<ZonedDateTime?>(null)
    val isMultipleSessions = mutableStateOf(false)
    val isMultipleSessionsState: State<Boolean> get() = isMultipleSessions

    val sessionCreationResult = mutableStateOf<FirebaseResult?>(null)

    private val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    val upcomingSessions = mutableStateOf<List<ZonedDateTime>>(emptyList())

    fun calculateNextSessionDate() {
        val selectedDay = selectedDayOfWeek.value
        val selectedHour = selectedTime.value?.substringBefore(":")?.toIntOrNull()
        val selectedMinute = selectedTime.value?.substringAfter(":")?.toIntOrNull()

        if (selectedDay != null && selectedHour != null && selectedMinute != null) {
            val today = Calendar.getInstance()
            val todayDayOfWeek = today.get(Calendar.DAY_OF_WEEK)

            val adjustedTodayDayOfWeek = if (todayDayOfWeek == Calendar.SUNDAY) 7 else todayDayOfWeek - 1
            val daysUntilNextDay = (selectedDay.ordinal - adjustedTodayDayOfWeek + 7) % 7

            today.add(Calendar.DAY_OF_YEAR, daysUntilNextDay)
            today.set(Calendar.HOUR_OF_DAY, selectedHour)
            today.set(Calendar.MINUTE, selectedMinute)
            today.set(Calendar.SECOND, 0)
            today.set(Calendar.MILLISECOND, 0)

            if (today.time.before(Date())) {
                today.add(Calendar.WEEK_OF_YEAR, 1)
            }

            val zoneId = ZoneId.systemDefault()
            val nextSessionDateTime = today.time.toInstant().atZone(zoneId)
            nextSessionDate.value = nextSessionDateTime
        } else {
            nextSessionDate.value = null
        }
    }

    fun scheduleMonthlySessions() {
        viewModelScope.launch {
            val email = patientEmail.value
            calculateNextSessionDate()

            val nextSessionDateValue = nextSessionDate.value
            if (!email.isNullOrEmpty() && nextSessionDateValue != null) {
                val sessions = mutableListOf<Session>()
                val sessionDates = mutableListOf<ZonedDateTime>()

                for (week in 0 until 4) {
                    val sessionDate = nextSessionDateValue.plusWeeks(week.toLong())
                    sessionDates.add(sessionDate)
                    val session = Session(
                        professionalId = currentEmail!!,
                        patientId = email,
                        date = Date.from(sessionDate.toInstant()),
                        time = sessionDate.format(dateFormatter),
                        status = SessionStatus.PENDING
                    )
                    sessions.add(session)
                }

                upcomingSessions.value = sessionDates

                val results = sessions.map { session ->
                    sessionUseCase.invoke(session, email)
                }
                if (results.all { it is FirebaseResult.Success }) {
                    println("Sesiones del mes programadas correctamente.")
                } else {
                    println("Error al programar las sesiones.")
                }
            } else {
                println("Faltan datos para programar las sesiones o la fecha inicial es inválida.")
            }
        }
    }

    fun scheduleSession() {
        viewModelScope.launch {
            val email = patientEmail.value
            calculateNextSessionDate()

            val nextSessionDateValue = nextSessionDate.value
            if (!email.isNullOrEmpty() && nextSessionDateValue != null) {
                val session = Session(
                    professionalId = currentEmail!!,
                    patientId = email,
                    date = Date.from(nextSessionDateValue.toInstant()),
                    time = nextSessionDateValue.format(dateFormatter),
                    status = SessionStatus.PENDING
                )

                val result = sessionUseCase.invoke(session, email)
                sessionCreationResult.value = result
            } else {
                println("Faltan datos para programar la sesión o la fecha es inválida.")
            }
        }
    }
}