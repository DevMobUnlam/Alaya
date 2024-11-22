package com.devmob.alaya.ui.screen.patient_profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import com.devmob.alaya.domain.GetRegistersUseCase
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.SessionUseCase
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.Session
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorSecondary
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.LightBlueColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PatientProfileViewModel(
    private val getEmailUseCase: GetUserDataUseCase,
    private val getSessionUseCase : SessionUseCase,
    private val getRegistersUseCase: GetRegistersUseCase
) :
    ViewModel() {

    var patientData by mutableStateOf<User?>(null)
    var isLoading by mutableStateOf(false)
    private var listRegisters by mutableStateOf<List<CrisisDetailsDB>?>(null)
    private var listRegistersBetweenDates by mutableStateOf<List<CrisisDetailsDB>?>(null)

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
            listRegisters = getRegistersUseCase.getListOfRegisters(email)
            listRegistersBetweenDates = getRegistersUseCase.getRegistersBetweenDates(
                email,
                getLastSessionDate(),
                getNextSessionDate()
            )
            isLoading = false
        }
    }

    private fun getLastSessionDate(): Date {
        // consultar session repository
        return Date()
    }

    private fun getNextSessionDate(): Date {
        // consultar session repository
        return Date()
    }

    fun getPointsData(): List<Point> {
        val listCrisisByMonth = getCountCrisisByMonth()
        if (listCrisisByMonth.isEmpty()) {
            return emptyList()
        }

        listCrisisByMonth.let {
            val minMonth = it.keys.minOf { key -> key.split("-")[0].toInt() }
            val maxMonth = it.keys.maxOf { key -> key.split("-")[0].toInt() }

            val allMonths = mutableListOf<Point>()
            allMonths.add(Point((minMonth - 1).toFloat(), 0f, getMonthName(minMonth - 1)))
            for (month in minMonth..maxMonth) {
                val monthString =
                    String.format("%02d-%d", month, Calendar.getInstance().get(Calendar.YEAR))
                val value = it[monthString] ?: 0
                allMonths.add(Point(month.toFloat(), value.toFloat(), getMonthName(month)))
            }
            return allMonths
        }
    }

    private fun getRegistersBetweenSessions(): Int {
        return listRegistersBetweenDates?.size ?: 0
    }

    private fun getCountCrisisByMonth(): Map<String, Int> {
        val dateFormat = SimpleDateFormat("MM-yyyy", Locale.getDefault())

        if (listRegisters.isNullOrEmpty()) return emptyMap()

        val validDates = listRegisters!!.mapNotNull { it.start }

        if (validDates.isEmpty()) return emptyMap()

        val minDate = validDates.minOrNull() ?: return emptyMap()
        val maxDate = validDates.maxOrNull() ?: return emptyMap()

        val calendar = Calendar.getInstance()
        val allMonths = mutableListOf<String>()

        calendar.time = minDate
        while (calendar.time <= maxDate) {
            allMonths.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.MONTH, 1)
        }

        val counts = listRegisters!!
            .filter { it.start != null }
            .groupingBy { dateFormat.format(it.start!!) }
            .eachCount()

        return allMonths.associateWith { counts[it] ?: 0 }
    }

    private fun getTopTools(): List<String> {
        return listRegistersBetweenDates
            ?.flatMap { it.tools }
            ?.groupingBy { it }
            ?.eachCount()
            ?.toList()
            ?.sortedByDescending { it.second }
            ?.take(3)
            ?.map { it.first }
            ?: emptyList()
    }

    fun getCarouselItems(): List<CarouselItem> {
        return listOf(
            CarouselItem.GenerateSummary(ColorQuaternary),
            CarouselItem.Crisis(
                "Crisis",
                getRegistersBetweenSessions().toString(),
                ColorTertiary.copy(alpha = 0.2f)
            ),
            CarouselItem.Activities("Actividades", 0.7f, LightBlueColor),
            CarouselItem.Tools(
                "Herramientas más usadas", ColorSecondary.copy(alpha = 0.3f), getTopTools()
            )
        )
    }

    private fun getMonthName(monthNumber: Int): String {
        val monthNames = mapOf(
            1 to "Ene",
            2 to "Feb",
            3 to "Mar",
            4 to "Abr",
            5 to "May",
            6 to "Jun",
            7 to "Jul",
            8 to "Ago",
            9 to "Sep",
            10 to "Oct",
            11 to "Nov",
            12 to "Dic"
        )
        return monthNames[monthNumber] ?: ""
    }
}
