package com.devmob.alaya.domain

import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment

interface CrisisTreatmentRepository {
    suspend fun addCustomTreatment(patientEmail: String, treatment: List<OptionTreatment?>) : FirebaseResult
    suspend fun getCustomTreatment(patientEmail: String) : List<OptionTreatment>?
}