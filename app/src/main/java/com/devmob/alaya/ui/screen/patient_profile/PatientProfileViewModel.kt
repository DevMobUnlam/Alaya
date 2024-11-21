package com.devmob.alaya.ui.screen.patient_profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.yml.charts.common.model.Point
import com.devmob.alaya.domain.GetRegistersUseCase
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.ui.theme.ColorQuaternary
import com.devmob.alaya.ui.theme.ColorSecondary
import com.devmob.alaya.ui.theme.ColorTertiary
import com.devmob.alaya.ui.theme.LightBlueColor
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PatientProfileViewModel(
    private val getEmailUseCase: GetUserDataUseCase,
    private val getRegistersUseCase: GetRegistersUseCase
) :
    ViewModel() {

    var patientData by mutableStateOf<User?>(null)
    var isLoading by mutableStateOf(false)
    private var listRegisters by mutableStateOf<List<CrisisDetailsDB>?>(null)

    fun getPatientData(email: String) {
        viewModelScope.launch {
            isLoading = true
            patientData = getEmailUseCase.getUser(email)
            listRegisters = getRegistersUseCase.getListOfRegisters(email)
            isLoading = false
        }
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
                Log.i("mes", month.toFloat().toString() + " " + value.toFloat().toString())
                allMonths.add(Point(month.toFloat(), value.toFloat(), getMonthName(month)))
            }
            return allMonths
        }
    }

    private fun getRegistersBetweenSessions(): Int {
        // obtener la ultima sesion y la proxima sesion del repository
        val lastSession = SimpleDateFormat("dd/MM/yyyy").parse("01/11/2024")
        val nextSession = SimpleDateFormat("dd/MM/yyyy").parse("10/11/2024")

        if (lastSession?.date == null || nextSession?.date == null) {
            return 0
        }

        return listRegisters?.let { register ->
            register.filter { it.start != null }
                .count { it.start!! > lastSession && it.start < nextSession }
        } ?: 0
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
        return listRegisters
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
