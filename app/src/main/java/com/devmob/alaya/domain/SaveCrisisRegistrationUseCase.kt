package com.devmob.alaya.domain


import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult

class SaveCrisisRegistrationUseCase(
    private val crisisRepository: CrisisRepository
) {
    suspend operator fun invoke(register: CrisisDetailsDB): FirebaseResult {
        return crisisRepository.addRegister(register)
    }

    suspend fun getLastCrisisDetails(): CrisisDetailsDB? {
        return crisisRepository.getLastCrisisDetails()
    }

    suspend fun updateCrisisDetails(register: CrisisDetailsDB): FirebaseResult {
        return crisisRepository.updateCrisisDetails(register)
    }
}