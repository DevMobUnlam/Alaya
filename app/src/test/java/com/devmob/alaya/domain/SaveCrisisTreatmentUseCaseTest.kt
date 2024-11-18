package com.devmob.alaya.domain

import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.domain.model.AuthenticationResult
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.OptionTreatment
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveCrisisTreatmentUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var customTreatmentRepository: CrisisTreatmentRepository

    @MockK
    private lateinit var uploadImage: UploadImageToFirestoreUseCase

    @MockK
    private lateinit var treatment: List<OptionTreatment>

    private lateinit var saveCrisisTreatmentUseCase: SaveCrisisTreatmentUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        coEvery {  customTreatmentRepository.saveCustomTreatment("email", treatment) } returns FirebaseResult.Success
        saveCrisisTreatmentUseCase =
            SaveCrisisTreatmentUseCase(customTreatmentRepository, uploadImage)
    }


    @Test
    fun `given an email and a treatment, when saveCrisisTreatment is called, then return Success`() =
        runBlocking {
            //GIVEN
            val email = "email"

            //WHEN
            val result = saveCrisisTreatmentUseCase(email, treatment)

            //THEN
            assertTrue(result is FirebaseResult.Success)
        }

    @Test
    fun `given an incorrect email and a treatment, when saveCrisisTreatment is called, then return Error`() =
        runBlocking {
            //GIVEN
            val incorrectEmail = "incorrectEmail"

            //WHEN
            val result = saveCrisisTreatmentUseCase(incorrectEmail, treatment)

            //THEN
            assertTrue(result is FirebaseResult.Error)
        }
}