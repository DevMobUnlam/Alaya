package com.devmob.alaya.domain

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.devmob.alaya.data.SaveCrisisTreatmentRepositoryImpl
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment

class SaveCrisisTreatmentUseCase {
    private val saveCustomTreatmentRepository = SaveCrisisTreatmentRepositoryImpl()

    suspend operator fun invoke(
        patientEmail: String,
        treatment: SnapshotStateList<OptionTreatment>
    ): FirebaseResult {
        return saveCustomTreatmentRepository.addCustomTreatment(patientEmail, treatment)
    }
}