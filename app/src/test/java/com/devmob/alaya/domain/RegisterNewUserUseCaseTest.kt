package com.devmob.alaya.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.domain.model.AuthenticationResult
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
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
class RegisterNewUserUseCaseTest {
    private lateinit var registerNewUserUseCase: RegisterNewUserUseCase

    @MockK
    private lateinit var registerNewUserRepository: RegisterNewUserRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        registerNewUserUseCase = RegisterNewUserUseCase(registerNewUserRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(registerNewUserRepository)
    }

    @Test
    fun `given valid email and password, when user is created successfully, then return success`() = runTest {
        // GIVEN
        val email = "test@example.com"
        val password = "password123"
        val expectedResult = AuthenticationResult.Success

        coEvery { registerNewUserRepository.createUserWithEmailAndPassword(email, password) } returns expectedResult

        // WHEN
        val result = registerNewUserUseCase.invoke(email, password)

        // THEN
        assertEquals(expectedResult, result)
        coVerify { registerNewUserRepository.createUserWithEmailAndPassword(email, password) }
    }

    @Test
    fun `given invalid credentials, when user creation fails, then return error`() = runTest {
        // GIVEN
        val email = "invalid@example.com"
        val password = "invalidPassword"
        val expectedError = AuthenticationResult.Error(Throwable("Invalid credentials"))

        coEvery { registerNewUserRepository.createUserWithEmailAndPassword(email, password) } returns expectedError

        // WHEN
        val result = registerNewUserUseCase.invoke(email, password)

        // THEN
        assertEquals(expectedError, result)
        coVerify { registerNewUserRepository.createUserWithEmailAndPassword(email, password) }
    }
}