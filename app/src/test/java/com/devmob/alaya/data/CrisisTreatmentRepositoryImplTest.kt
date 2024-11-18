package com.devmob.alaya.data

import android.net.Uri
import com.devmob.alaya.domain.CrisisTreatmentRepository
import com.devmob.alaya.domain.UploadImageToFirestoreUseCase
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CrisisTreatmentRepositoryImplTest {

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var uploadImage: UploadImageToFirestoreUseCase

    @MockK (relaxed = true)
    private lateinit var treatmentMockk: OptionTreatment

    @MockK
    private lateinit var exceptionMock: Exception

    @MockK
    private lateinit var failureMock: Task<Void>

    @MockK
    private lateinit var successMock: Task<Void>

    @MockK
    private lateinit var uriMock: Uri

    @MockK
    private lateinit var dbMockk: FirebaseFirestore

    private val patientEmailMockk = "patientEmailMockk"

    private lateinit var repository: CrisisTreatmentRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { failureMock.exception } returns exceptionMock
        every { firebaseClient.db } returns dbMockk

        every {
            dbMockk.collection("users").document(any()).update("stepCrisis", any())
        } returns successMock
        every { treatmentMockk.imageUri } returns "someImageUri"
        every { treatmentMockk.copy(any()) } returns treatmentMockk.copy(imageUri = "newImageUri")
        coEvery { uploadImage(any()) } returns uriMock

        repository = CrisisTreatmentRepositoryImpl(firebaseClient, uploadImage)
    }

    @Test
    fun `When save custom treatment then Success result`(): Unit = runTest {
        //GIVEN
        val treatmentList = listOf(treatmentMockk)
        val expected = FirebaseResult.Success

        //WHEN
        val result = repository.saveCustomTreatment(patientEmailMockk, treatmentList)

        // THEN
        Assert.assertEquals(expected, result)
    }

    @Test
    fun `When save custom treatment then Error result`(): Unit = runTest {
        val treatmentList = listOf(treatmentMockk)
        coEvery {
            dbMockk.collection("users").document("null").update("stepCrisis", treatmentMockk)
        } throws exceptionMock

        val result = repository.saveCustomTreatment("null", treatmentList)

        assertTrue(result is FirebaseResult.Error)
    }
}