package com.devmob.alaya.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.local_storage.CrisisStepsDao
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.model.OptionTreatment
import com.devmob.alaya.domain.model.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetCrisisTreatmentUseCaseTest {


    private lateinit var getCrisisTreatmentUseCase: GetCrisisTreatmentUseCase

    @MockK
    private lateinit var prefs: SharedPreferences

    @MockK
    private lateinit var userRepository: GetUserRepository

    @MockK
    private lateinit var crisisStepsDao: CrisisStepsDao

    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        getCrisisTreatmentUseCase = GetCrisisTreatmentUseCase(prefs, userRepository, crisisStepsDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given valid email, when user is found, then return treatment options`() = runTest {
        // GIVEN
        val email = "test@example.com"
        val user = mockk<User>(relaxed = true)
        val options = listOf(OptionTreatment(), OptionTreatment())
        coEvery { prefs.getEmail() } returns email
        coEvery { userRepository.getUser(email) } returns user
        coEvery { user.stepCrisis } returns options

        // WHEN
        val result = getCrisisTreatmentUseCase.invoke()

        // THEN
        assertEquals(options, result)
        coVerify { userRepository.getUser(email) }
    }

    @Test
    fun `given no email, when user is not found, then return null`() = runTest {
        // GIVEN
        coEvery { prefs.getEmail() } returns null

        // WHEN
        val result = getCrisisTreatmentUseCase.invoke()

        // THEN
        assertEquals(null, result)
    }

    @Test
    fun `given invalid email, when user is not found, then return null`() = runTest {
        // GIVEN
        val email = "invalid@example.com"
        coEvery { prefs.getEmail() } returns email
        coEvery { userRepository.getUser(email) } returns null

        // WHEN
        val result = getCrisisTreatmentUseCase.invoke()

        // THEN
        assertEquals(null, result)
        coVerify { userRepository.getUser(email) }
    }

    @Test
    fun `given valid email, when local database is not empty, then return treatment options from local database`() = runTest{
        // GIVEN
        val email = "test@example.com"
        val options = listOf(OptionTreatment(), OptionTreatment())
        coEvery { prefs.getEmail() } returns email
        coEvery { crisisStepsDao.getCrisisSteps() } returns options

        // WHEN
        val result = getCrisisTreatmentUseCase.invoke()

        // THEN
        assertEquals(options, result)
        coVerify { crisisStepsDao.getCrisisSteps() }
    }

    @Test
    fun `given valid email, when local database is empty, then return treatment options from remote database and update local database`() = runTest {
        // GIVEN
        val email = "test@example.com"
        val user = mockk<User>(relaxed = true)
        val options = listOf(OptionTreatment(), OptionTreatment())
        coEvery { prefs.getEmail() } returns email
        coEvery { userRepository.getUser(email) } returns user
        coEvery { user.stepCrisis } returns options

        // WHEN
        val result = getCrisisTreatmentUseCase.invoke()

        // THEN
        assertEquals(options, result)
        coVerify { userRepository.getUser(email) }
        coVerify { crisisStepsDao.insertCrisisStep(any()) }
    }
}