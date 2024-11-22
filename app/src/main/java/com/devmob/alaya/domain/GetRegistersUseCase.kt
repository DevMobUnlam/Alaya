package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.CrisisDetailsDB
import java.util.Calendar
import java.util.Date

class GetRegistersUseCase(private val crisisRepository: CrisisRepository) {

    suspend fun getListOfRegisters(patientId: String): List<CrisisDetailsDB>? {
        return crisisRepository.getListRegisters(patientId)
    }

    suspend fun getRegistersBetweenDates(
        patientId: String,
        endDate: Date
    ): List<CrisisDetailsDB>? {
        val calendar = Calendar.getInstance().apply {
            time = endDate
            add(Calendar.DAY_OF_YEAR, -7)
        }
        val startDate = calendar.time

        val registers = crisisRepository.getListRegisters(patientId)
        return registers?.filter { it.start?.let { start -> start in startDate..endDate } == true }
    }
}