package com.devmob.alaya.domain

import android.util.Log
import com.devmob.alaya.data.CrisisTreatmentRepositoryImpl
import com.devmob.alaya.domain.model.OptionTreatment

class GetCrisisTreatmentUseCase {

    private val CustomTreatmentRepository = CrisisTreatmentRepositoryImpl()

    suspend operator fun invoke(
        patientEmail: String
    ): List<OptionTreatment>? {
        val result = CustomTreatmentRepository.getCustomTreatment(patientEmail)
        Log.d("leandro", "el result del caso de uso es: $result")
        return result
    }
}