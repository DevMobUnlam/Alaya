package com.devmob.alaya.domain

import android.net.Uri
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.UploadImageToFirestoreImpl
import kotlinx.coroutines.tasks.await

class UploadImageToFirestoreUseCase {
    private val email = FirebaseClient().auth.currentUser?.email
    val repository = UploadImageToFirestoreImpl()

    suspend operator fun invoke(imageUri: String): Uri? {

        val path = "customOptionTreatment/${email}/${imageUri}"
        return repository.uploadImage(imageUri, path)
    }
}


