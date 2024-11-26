package com.devmob.alaya.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.mapper.toNetwork
import com.devmob.alaya.domain.model.DailyActivity
import com.devmob.alaya.domain.model.FirebaseResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@OptIn(ExperimentalCoroutinesApi::class)
class CreateDailyActivityUseCaseTest {

    private lateinit var createDailyActivityUseCase: CreateDailyActivityUseCase

    @MockK
    private lateinit var repository: DailyActivityRepository

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var dailyActivity: DailyActivity

    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        coEvery {
            repository.createDailyActivity(
                "id",
                dailyActivity.toNetwork()
            )
        } returns FirebaseResult.Success

        createDailyActivityUseCase = CreateDailyActivityUseCase(repository)
    }

    @Test
    fun `given a patient and an activity, when invoke is called correctly, then return Success`() =
        runBlocking {
            //GIVEN
            val id = "id"
            val expected = FirebaseResult.Success
            //WHEN
            val result = createDailyActivityUseCase(id, dailyActivity)

            //THEN
            assertEquals(expected, result)
            coVerify { repository.createDailyActivity(id, dailyActivity.toNetwork()) }
        }

    @Test
    fun `given a invalid patient, when invoke is called, then return Error`() =
        runBlocking {
            //GIVEN
            val invalidID = "invalid id"

            //WHEN
            val result = createDailyActivityUseCase(invalidID, dailyActivity)

            //THEN
            assert(result is FirebaseResult.Error)
        }
}