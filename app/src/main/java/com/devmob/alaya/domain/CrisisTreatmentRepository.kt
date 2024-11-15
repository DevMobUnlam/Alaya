package com.devmob.alaya.domain

import android.content.Context
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment

interface CrisisTreatmentRepository {
    suspend fun addCustomTreatment(patientEmail: String, treatment: List<OptionTreatment?>, context: Context) : FirebaseResult
    suspend fun getCustomTreatment(patientEmail: String) : List<OptionTreatment>?
}