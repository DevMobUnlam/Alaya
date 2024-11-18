package com.devmob.alaya.data

import com.devmob.alaya.domain.CrisisTreatmentRepository
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CrisisTreatmentRepositoryImplTest {

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var treatmentMockk: List<OptionTreatment?>

    @MockK
    private lateinit var exceptionMock: Exception

    @MockK
    private lateinit var failureMock: Task<Void>

    @MockK
    private lateinit var successMock: Task<Void>

    @MockK
    private lateinit var dbMockk: FirebaseFirestore

    private val patientEmailMockk = "patientEmailMockk"

    private lateinit var repository: CrisisTreatmentRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { successMock.isComplete } returns true
        every { successMock.exception } returns null
        every { successMock.isCanceled } returns false
        every { failureMock.isComplete } returns true
        every { failureMock.isSuccessful } returns false
        every { failureMock.exception } returns exceptionMock
        every { firebaseClient.db } returns dbMockk

        repository = CrisisTreatmentRepositoryImpl(firebaseClient)
    }

    @Test
    fun `When save custom treatment then Success result`(): Unit = runTest {
        val expected = FirebaseResult.Success
        every {
            dbMockk.collection("users").document(any()).update("stepCrisis", any())
        } returns successMock
        val result = repository.saveCustomTreatment(patientEmailMockk, treatmentMockk)

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `When save custom treatment then Error result`(): Unit = runTest {
        val expected = FirebaseResult.Error(exceptionMock)
        coEvery {
            dbMockk.collection("users").document("null").update("stepCrisis", treatmentMockk)
        } throws exceptionMock

        val result = repository.saveCustomTreatment("null", treatmentMockk)

        Assert.assertEquals(expected, result)
    }
}