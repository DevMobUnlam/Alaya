package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.CrisisDetailsDB
import java.util.Date

class GetRegistersUseCase(private val crisisRepository: CrisisRepository) {

    suspend fun getListOfRegisters(patientId: String): List<CrisisDetailsDB>? {
        return crisisRepository.getListRegisters(patientId)
    }

    suspend fun getRegistersBetweenDates(patientId: String, startDate: Date, endDate: Date): List<CrisisDetailsDB>? {
        val registers = crisisRepository.getListRegisters(patientId)
        return registers?.filter { it.start?.let { start -> start in startDate..endDate } == true }
    }
}