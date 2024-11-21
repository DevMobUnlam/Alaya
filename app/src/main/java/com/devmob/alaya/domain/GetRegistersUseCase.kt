package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.CrisisDetailsDB

class GetRegistersUseCase(private val crisisRepository: CrisisRepository) {

    suspend fun getListOfRegisters(patientId: String): List<CrisisDetailsDB>? {
        return crisisRepository.getListRegisters(patientId)
    }
}