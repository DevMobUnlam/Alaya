package com.devmob.alaya.ui.screen.professionalHome

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.model.Patient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProfessionalHomeViewModel(
    private val getUserData: GetUserDataUseCase
) : ViewModel() {
    var patients by mutableStateOf<List<Patient>>(emptyList())
    var greetingMessage by mutableStateOf("")
    var currentEmail = FirebaseClient().auth.currentUser?.email
    var nameProfessional by mutableStateOf("")

    init {
        fetchProfessional()
        currentEmail?.let { loadPatients(it) }
        updateGreetingMessage()
    }

    private fun fetchProfessional() {
        viewModelScope.launch {
            nameProfessional = currentEmail?.let { getUserData.getName(it) } ?: ""
        }
    }

    fun loadPatients(professionalEmail: String) {
        viewModelScope.launch {
            val professional = getUserData.getUser(professionalEmail)
            professional?.let {
                val today = Calendar.getInstance()
                val todayString = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(today.time)

                patients = (it.patients ?: emptyList()).filter { patient ->
                    patient.nextSessionDate == todayString
                }
            }
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
