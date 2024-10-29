package com.devmob.alaya.domain


import com.devmob.alaya.data.CrisisRepositoryImpl
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.FirebaseResult

class SaveCrisisRegistrationUseCase {
    private val crisisRepository = CrisisRepositoryImpl()

    suspend operator fun invoke(register: CrisisDetailsDB): FirebaseResult {
        return crisisRepository.addRegister(register)
    }
}