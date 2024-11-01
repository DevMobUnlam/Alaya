package com.devmob.alaya.domain

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.devmob.alaya.data.SaveCustomTreatmentRepositoryImpl
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment

class SaveCustomTreatmentUseCase {
    private val saveCustomTreatmentRepository = SaveCustomTreatmentRepositoryImpl()

    suspend operator fun invoke(
        patientEmail: String,
        treatment: SnapshotStateList<OptionTreatment>
    ): FirebaseResult {
        return saveCustomTreatmentRepository.addCustomTreatment(patientEmail, treatment)
    }
}