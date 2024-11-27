package com.devmob.alaya.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.model.DailyActivity
import com.devmob.alaya.domain.model.FirebaseResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
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
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
class ChangeDailyActivityStatusUseCaseTest {

    private lateinit var changeDailyActivityStatusUseCase: ChangeDailyActivityStatusUseCase

    @MockK
    private lateinit var dailyActivityRepository: DailyActivityRepository

    @MockK
    private lateinit var dailyActivity: DailyActivity

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var exception: Exception

    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        every { dailyActivity.currentProgress } returns 0
        every { dailyActivity.id } returns "id"
        every { firebaseClient.auth.currentUser?.email } returns "email"

        changeDailyActivityStatusUseCase = ChangeDailyActivityStatusUseCase(dailyActivityRepository)
    }

    @Test
    fun `given an activity and an updated process, when the updated is completed, then return success`(): Unit = runBlocking {
        //GIVEN
        val newStatus = true
        val updatedActivity = dailyActivity.currentProgress+1
        coEvery { dailyActivityRepository.changeDailyActivityStatus(dailyActivity, newStatus, updatedActivity) } returns FirebaseResult.Success

        //WHEN
        val result = changeDailyActivityStatusUseCase(newStatus, dailyActivity, updatedActivity)
        val expected = FirebaseResult.Success

        //THEN
        assertEquals(expected, result)
    }

    @Test
    fun `given an activity and an updated process, when the updated is failed, then return Error`(): Unit = runBlocking {
        //GIVEN
        val newStatus = true
        val updatedActivity = dailyActivity.currentProgress+1
        coEvery { dailyActivityRepository.changeDailyActivityStatus(dailyActivity, newStatus, updatedActivity) } returns FirebaseResult.Error(exception)

        //WHEN
        val result = changeDailyActivityStatusUseCase(newStatus, dailyActivity, updatedActivity)
        val expected = FirebaseResult.Error(exception)

        //THEN
        assertEquals(expected, result)
    }

}