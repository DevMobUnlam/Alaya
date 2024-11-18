package com.devmob.alaya.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.domain.model.AuthenticationResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
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

@OptIn(ExperimentalCoroutinesApi::class)
class LoginUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var loginRepository: LoginRepository

    @MockK
    private lateinit var exceptionMock: Exception

    private lateinit var loginUseCase: LoginUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        loginUseCase = LoginUseCase(loginRepository)
    }

    @Test
    fun `given an email, when login is called, then return Success`() =
        runBlocking {
            //GIVEN
            val email = "email"
            val password = "password"
            coEvery { loginRepository.login(email, password) } returns AuthenticationResult.Success
            val expected = AuthenticationResult.Success

            //WHEN
            val result = loginUseCase(email, password)

            //THEN
            assertEquals(expected, result)
        }

    @Test
    fun `given an incorrect email, when login is called, then return Error`() =
        runBlocking {
            //GIVEN
            val email = "incorrectEmail"
            val password = "incorrectPassword"
            coEvery { loginRepository.login(email, password) } returns AuthenticationResult.Error(exceptionMock)
            val expected = AuthenticationResult.Error(exceptionMock)

            //WHEN
            val result = loginUseCase(email, password)

            //THEN
            assertEquals(expected, result)
        }

}