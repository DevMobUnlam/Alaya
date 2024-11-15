package com.devmob.alaya.domain

import android.content.Context
import com.devmob.alaya.data.CrisisTreatmentRepositoryImpl
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment

class SaveCrisisTreatmentUseCase {
    private val CustomTreatmentRepository = CrisisTreatmentRepositoryImpl()

    suspend operator fun invoke(
        patientEmail: String,
        treatment: List<OptionTreatment?>,
        context: Context
    ): FirebaseResult {
        val result = CustomTreatmentRepository.addCustomTreatment(patientEmail, treatment, context)
        return result
    }
}