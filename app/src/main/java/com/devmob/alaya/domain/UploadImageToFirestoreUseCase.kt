package com.devmob.alaya.domain

import android.net.Uri
import com.devmob.alaya.data.preferences.SharedPreferences

class UploadImageToFirestoreUseCase(
    private val repository: UploadImageToFirestoreRepository,
    prefs: SharedPreferences
) {
    private val email = prefs.getEmail()
    suspend operator fun invoke(imageUri: String): Uri? {
        val path = "customOptionTreatment/${email}/${imageUri.replace("/", "-")}"
        return repository.uploadImage(imageUri, path)
    }
}


