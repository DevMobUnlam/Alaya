package com.devmob.alaya.domain

import android.util.Log
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.devmob.alaya.data.SaveCrisisTreatmentRepositoryImpl
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment

class SaveCrisisTreatmentUseCase {
    private val saveCustomTreatmentRepository = SaveCrisisTreatmentRepositoryImpl()

    suspend operator fun invoke(
        patientEmail: String,
        treatment: List<OptionTreatment?>
    ): FirebaseResult {
        Log.d("leandro", "usecase usuario $patientEmail")
        val result = saveCustomTreatmentRepository.addCustomTreatment(patientEmail, treatment)
        Log.d("leandro", "usecase usuario return $result")
        return result
    }
}