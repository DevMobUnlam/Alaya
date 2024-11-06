package com.devmob.alaya.data

import android.net.Uri
import android.util.Log
import com.devmob.alaya.domain.UploadImageToFirestore
import kotlinx.coroutines.tasks.await

class UploadImageToFirestoreImpl : UploadImageToFirestore {
    private val storage = FirebaseClient().storage
    private val storageRef = storage.reference
    private val email = FirebaseClient().auth.currentUser?.email

    override suspend fun uploadImage(imageUri: String, storagePath: String): Uri? {

        return try {
            val imageRef =
                storageRef.child(storagePath)

            if (imageUri.startsWith("https://")) {
                //Si es un paso predeterminado, la imagen ya está subida a firebase
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
