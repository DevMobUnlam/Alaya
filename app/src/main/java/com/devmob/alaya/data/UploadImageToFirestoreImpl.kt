package com.devmob.alaya.data

import android.net.Uri
import com.devmob.alaya.domain.UploadImageToFirestore
import kotlinx.coroutines.tasks.await

class UploadImageToFirestoreImpl : UploadImageToFirestore {
    private val storage = FirebaseClient().storage
    private val storageRef = storage.reference
    private val email = FirebaseClient().auth.currentUser?.email

    override suspend fun uploadImage(imageUri: Uri, storagePath: String): Uri? {

        return try {
            val imageRef =
                storageRef.child(storagePath)

            imageRef.putFile(imageUri).await()
            imageRef.downloadUrl.await()
        } catch (e: Exception) {
            null
        }
    }
}
