package com.devmob.alaya.ui.screen.professionalHome

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.model.Patient
import com.devmob.alaya.utils.toCalendarOrNull
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ProfessionalHomeViewModel(
    private val getUserData: GetUserDataUseCase,
    firebaseClient: FirebaseClient
) : ViewModel() {
    var patients by mutableStateOf<List<Patient>>(emptyList())
    var greetingMessage by mutableStateOf("")
    var currentEmail = firebaseClient.auth.currentUser?.email
    var nameProfessional by mutableStateOf("")
    var isLoading by mutableStateOf(true)

    init {
        fetchProfessional()
        currentEmail?.let { loadPatients(it) }
        updateGreetingMessage()
    }

    private fun fetchProfessional() {
        viewModelScope.launch {
            nameProfessional = currentEmail?.let { getUserData.getName(it) } ?: ""
            checkIfLoadingCompleted()
        }
    }

    private fun checkIfLoadingCompleted() {
        if (nameProfessional.isNotEmpty()) {
            isLoading = false
        }
    }

    fun loadPatients(professionalEmail: String) {
        viewModelScope.launch {
            val professional = getUserData.getUser(professionalEmail)
            professional?.let {
                val today = Date()
                Log.d("hoy", today.toString())
                patients = (it.patients ?: emptyList()).filter { patient ->
                    Log.d ("nextSession", patient.nextSession.toString())
                    patient.nextSession.toCalendarOrNull()?.get(Calendar.DAY_OF_MONTH) == today.toCalendarOrNull()?.get(Calendar.DAY_OF_MONTH)
                            && patient.nextSession.toCalendarOrNull()?.get(Calendar.MONTH) == today.toCalendarOrNull()?.get(Calendar.MONTH)
                            && patient.nextSession.toCalendarOrNull()?.get(Calendar.YEAR) == today.toCalendarOrNull()?.get(Calendar.YEAR)
                }
                Log.d ("patients", patients.toString())
            }
            checkIfLoadingCompleted()
        }
    }

    fun updateGreetingMessage() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        greetingMessage = when (hourOfDay) {
            in 5..11 -> "Buenos días"
            in 12..19 -> "Buenas tardes"
            else -> "Buenas noches"
        }
    }
}
