package com.devmob.alaya.ui.screen.patient_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.SessionUseCase
import com.devmob.alaya.domain.model.Session
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorSecondary
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.LightBlueColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatientProfileViewModel(
    private val getEmailUseCase: GetUserDataUseCase,
    private val getSessionUseCase : SessionUseCase
) :
    ViewModel() {

    var patientData by mutableStateOf<User?>(null)
    var isLoading by mutableStateOf(false)

    private val _nextSession = MutableStateFlow<Session?>(null)
    val nextSession: StateFlow<Session?> get() = _nextSession

    fun getNextSession(patientEmail: String) {
        viewModelScope.launch {
            val session = getSessionUseCase.getNextSession(patientEmail)
            _nextSession.value = session
        }
    }

    fun getPatientData(email: String) {
        viewModelScope.launch {
            isLoading = true
            patientData = getEmailUseCase.getUser(email)
            isLoading = false
        }
    }

    fun getPointsData(): List<Point> {
        // TODO obtener los datos de la base de datos
        return listOf(
            Point(0f, 8f, "Dic"),
            Point(1f, 6f, "Ene"),
            Point(2f, 0f, "Feb"),
            Point(3f, 2f, "Mar"),
            Point(4f, 6f, "Abr"),
            Point(5f, 0f, "May"),
            Point(6f, 4f, "Jun"),
            Point(7f, 5f, "Jul"),
            Point(8f, 0f, "Ago"),
            Point(9f, 2f, "Sep"),
            Point(10f, 1f, "Oct"),
            Point(11f, 5f, "Nov"),
            Point(12f, 0f, "Dic"),
        )
    }

    fun getCarouselItems(): List<CarouselItem> {
        return listOf(
            CarouselItem.GenerateSummary(ColorQuaternary),
            CarouselItem.Crisis("Crisis", "5", ColorTertiary.copy(alpha = 0.2f)),
            CarouselItem.Activities("Actividades", 0.7f, LightBlueColor),
            CarouselItem.Tools(
                "Herramientas", ColorSecondary.copy(alpha = 0.3f), listOf(
                    ToolProgress("Respiración", 0.8f),
                    ToolProgress("Meditación", 0.6f),
                    ToolProgress("Ejercicio", 0.4f)
                )
            )
        )
    }
}
