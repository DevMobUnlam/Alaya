package com.devmob.alaya.data

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.devmob.alaya.domain.UploadImageToFirestore
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class UploadImageToFirestoreImpl : UploadImageToFirestore {
    private val storage = FirebaseClient().storage
    private val storageRef = storage.reference

    override suspend fun uploadImage(imageUri: String, storagePath: String, context: Context): Uri? {

        return try {
            val imageRef =
                storageRef.child(storagePath)

            if (imageUri.startsWith("https://")) {
                //Si es un paso predeterminado, la imagen ya está subida a firebase
                Uri.parse(imageUri)
            } else {
                val compressedImage = compressAndResizeImage(Uri.parse(imageUri), context)
                imageRef.putBytes(compressedImage).await()
                imageRef.downloadUrl.await()
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun compressAndResizeImage(imageUri: Uri, context: Context, quality: Int = 75, maxWidth: Int = 800, maxHeight: Int = 800): ByteArray {
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, maxWidth, maxHeight, true)
        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        return outputStream.toByteArray()
    }
}
