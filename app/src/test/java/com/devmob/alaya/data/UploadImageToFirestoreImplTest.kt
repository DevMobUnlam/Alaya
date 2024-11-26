package com.devmob.alaya.data

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString


class UploadImageToFirestoreImplTest {
    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var storage: FirebaseStorage

    @MockK
    private lateinit var storageRef: StorageReference

    @MockK
    private lateinit var imageRef: StorageReference

    @MockK
    private lateinit var uploadTask: UploadTask

    @MockK
    private lateinit var storageTask: StorageTask<UploadTask.TaskSnapshot>

    private lateinit var repository: UploadImageToFirestoreRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        coEvery { firebaseClient.storage } returns storage
        coEvery { storage.reference } returns storageRef
        coEvery { storageRef.child(anyString()) } returns imageRef
        coEvery { imageRef.putFile(any()) } returns uploadTask
        coEvery { uploadTask.addOnSuccessListener(any()) } returns uploadTask
        coEvery { storageTask.addOnSuccessListener(any()) } returns storageTask
        repository = UploadImageToFirestoreRepositoryImpl(firebaseClient)
        mockkStatic(Uri::class)
        every { Uri.parse(any<String>()) } returns mockk()
    }

    @Test
    fun `uploadImage with HTTPS URI should return URI`() = runBlocking {
        val imageUri = "https://example.com/image.jpg"
        val storagePath = "path/to/image"

        val result = repository.uploadImage(imageUri, storagePath)

        Assert.assertEquals(Uri.parse(imageUri), result)
    }

    @Test
    fun `uploadImage with error should return null`() = runTest {
        val imageUri = "content://local/image.jpg"
        val storagePath = "path/to/image"

        coEvery { firebaseClient.storage } returns storage
        coEvery { storage.reference } returns storageRef
        coEvery { storageRef.child(storagePath) } returns imageRef
        coEvery { imageRef.putFile(any()) } throws RuntimeException("Upload failed")

        val result = repository.uploadImage(imageUri, storagePath)

        Assert.assertNull(result)
    }
}