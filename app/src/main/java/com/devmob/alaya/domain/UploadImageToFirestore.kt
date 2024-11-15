package com.devmob.alaya.domain

import android.content.Context
import android.net.Uri

interface UploadImageToFirestore {
    suspend fun uploadImage(imageUri: String, storagePath: String, context: Context): Uri?
}