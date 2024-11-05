package com.devmob.alaya.domain

import android.net.Uri

interface UploadImageToFirestore {
    suspend fun uploadImage(imageUri: Uri, storagePath: String): Uri?
}