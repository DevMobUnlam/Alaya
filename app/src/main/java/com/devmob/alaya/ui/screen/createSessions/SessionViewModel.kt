package com.devmob.alaya.ui.screen.createSessions

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.SessionUseCase
import com.devmob.alaya.domain.model.DayOfWeek
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.Recurrence
import com.devmob.alaya.domain.model.Session
import com.devmob.alaya.domain.model.SessionStatus
import kotlinx.coroutines.launch
import java.util.*

class SessionViewModel (private val sessionUseCase: SessionUseCase): ViewModel() {
    val session = mutableStateOf(Session(recurrence = Recurrence.NONE))
    val selectedDays = mutableStateOf<List<DayOfWeek>>(emptyList())
    val selectedDate = mutableStateOf<Date?>(null)
    val selectedTime = mutableStateOf<String?>(null)
    val sessionDuration = mutableStateOf(0)
    val patientEmail = mutableStateOf("")
    val currentEmail = FirebaseClient().auth.currentUser?.email

    val sessionCreationResult = mutableStateOf<FirebaseResult?>(null)
    val sessionsList = mutableStateOf<List<Session>>(emptyList())

    fun scheduleSession() {
        viewModelScope.launch {
            val email = patientEmail.value
            if (email.isNotEmpty()) {
                val sessionData = session.value.copy(
                    dayOfWeek = selectedDays.value,
                    date = selectedDate.value,
                    duration = sessionDuration.value,
                    status = SessionStatus.PENDING,
                    recurrence = session.value.recurrence,
                    patientId = email,
                    professionalId = currentEmail!!
                )

                val result = sessionUseCase(sessionData, email)
                sessionCreationResult.value = result
            } else {
                println("No patient email found!")
            }
        }
    }

    fun getSessions() {
        viewModelScope.launch {
            val email = patientEmail.value
            if (email.isNotEmpty()) {
                val sessions = sessionUseCase.getSessions(email)
                sessionsList.value = sessions
            }
        }
    }

    fun updateRecurrence(recurrence: Recurrence) {
        session.value = session.value.copy(recurrence = recurrence)
        if (recurrence == Recurrence.WEEKLY || recurrence == Recurrence.DAILY) {
            selectedDays.value = emptyList()
        }
    }

    fun updateDate(date: Date) {
        selectedDate.value = date
    }

    fun updateTime(time: String) {
        selectedTime.value = time
    }

    fun updateSessionDuration(duration: Int) {
        sessionDuration.value = duration
    }
}