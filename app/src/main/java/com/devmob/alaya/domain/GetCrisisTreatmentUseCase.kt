package com.devmob.alaya.domain

import com.devmob.alaya.data.CrisisTreatmentRepositoryImpl
import com.devmob.alaya.domain.model.OptionTreatment

class GetCrisisTreatmentUseCase {

    private val CustomTreatmentRepository = CrisisTreatmentRepositoryImpl()

    suspend operator fun invoke(
        patientEmail: String
    ): List<OptionTreatment>? {
        val result = CustomTreatmentRepository.getCustomTreatment(patientEmail)
        return result
    }
}