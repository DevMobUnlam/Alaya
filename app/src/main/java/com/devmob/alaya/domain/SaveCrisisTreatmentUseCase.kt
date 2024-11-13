package com.devmob.alaya.domain

import com.devmob.alaya.data.CrisisTreatmentRepositoryImpl
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment

class SaveCrisisTreatmentUseCase(
    private val customTreatmentRepository: CrisisTreatmentRepositoryImpl = CrisisTreatmentRepositoryImpl(),
    private val uploadImage: UploadImageToFirestoreUseCase = UploadImageToFirestoreUseCase()
) {
    suspend operator fun invoke(
        patientEmail: String,
        treatment: List<OptionTreatment?>
    ): FirebaseResult {
        val treatmentList = mutableListOf<OptionTreatment>()
        treatment.forEach { optionTreatment ->
            optionTreatment?.imageUri?.let { imageUri ->
                val image = uploadImage(imageUri)
                if (image != null) {
                    val updatedTreatment = optionTreatment.copy(imageUri = image.toString())
                    treatmentList.add(updatedTreatment)
                } else {
                    val updatedTreatment = optionTreatment.copy(imageUri = "")
                    treatmentList.add(updatedTreatment)
                }
            }
        }
        return customTreatmentRepository.saveCustomTreatment(patientEmail, treatment)
    }
}