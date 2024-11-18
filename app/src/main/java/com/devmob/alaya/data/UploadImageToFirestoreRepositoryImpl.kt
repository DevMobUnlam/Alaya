package com.devmob.alaya.data

import android.net.Uri
import com.devmob.alaya.domain.UploadImageToFirestoreRepository
import kotlinx.coroutines.tasks.await

class UploadImageToFirestoreRepositoryImpl(
    firebaseClient: FirebaseClient
) : UploadImageToFirestoreRepository {
    private val storage = firebaseClient.storage
    private val storageRef = storage.reference

    override suspend fun uploadImage(imageUri: String, storagePath: String): Uri? {
        return try {
            val imageRef = storageRef.child(storagePath)
            if (imageUri.startsWith("https://")) {
                Uri.parse(imageUri)
            } else {
                imageRef.putFile(Uri.parse(imageUri)).await()
                imageRef.downloadUrl.await()
            }
        } catch (e: Exception) {
            null
        }
    }
}
