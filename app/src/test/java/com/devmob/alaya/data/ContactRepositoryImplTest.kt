package com.devmob.alaya.data

import android.net.Uri
import com.devmob.alaya.domain.model.FirebaseResult
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ContactRepositoryImplTest {

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var dbMock: FirebaseFirestore

    @MockK
    private lateinit var userRefMock: DocumentReference

    @MockK
    private lateinit var successMock: Task<Void>

    @MockK
    private lateinit var failureMock: Task<Void>

    @MockK
    private lateinit var exceptionMock: Exception

    @MockK
    private lateinit var uriMock: Uri

    @MockK
    private lateinit var storageRefMock: StorageReference

    @MockK
    private lateinit var fileRefMock: StorageReference

    private lateinit var repository: ContactRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        coEvery { firebaseClient.db } returns dbMock
        coEvery { dbMock.collection("users").document(any()) } returns userRefMock
        coEvery { successMock.isComplete } returns true
        coEvery { successMock.exception } returns null
        coEvery { successMock.isCanceled } returns false
        coEvery { successMock.result } returns mockk()
        coEvery { failureMock.isComplete } returns true
        coEvery { failureMock.exception } returns exceptionMock

        mockkStatic("kotlinx.coroutines.tasks.TasksKt")
        repository = ContactRepositoryImpl(firebaseClient)
    }

    @Test
    fun `when updating contacts successfully, it returns Success`() = runBlocking {
        coEvery { userRefMock.update("containmentNetwork", any()) } returns successMock

        val expected = FirebaseResult.Success
        val result = repository.updateContacts("test@test.com", listOf())

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `when updating contacts fails, it returns an error`() = runBlocking {

        coEvery { userRefMock.update("containmentNetwork", any()) } returns failureMock

        val expected = FirebaseResult.Error(exceptionMock)
        val result = repository.updateContacts("test@test.com", listOf())

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `when uploading image fails, it returns null`() = runBlocking {

        coEvery { fileRefMock.putFile(uriMock) } throws exceptionMock

        val result = repository.uploadImageToStorage(uriMock, "contactId")

        Assert.assertNull(result)
    }

}