package com.devmob.alaya.domain

import android.net.Uri

interface UploadImageToFirestoreRepository {
    suspend fun uploadImage(imageUri: String, storagePath: String): Uri?
}