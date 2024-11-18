package com.devmob.alaya.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.domain.model.User
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
class GetUserDataUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getUserRepository: GetUserRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var getUserDataUseCase: GetUserDataUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        getUserDataUseCase = GetUserDataUseCase(getUserRepository)
    }

    @Test
    fun `given an email, when getUser is called, then return User`() =
        runBlocking {
            //GIVEN
            val email = "email"
            coEvery {getUserRepository.getUser(email) } returns User(email = "email")
            val expectedUser = User(email = "email")

            //WHEN
            val result = getUserDataUseCase.getUser(email)

            //THEN
            assertEquals(expectedUser, result)
        }

    @Test
    fun `given an incorrect email, when getUser is called, then return null`() =
        runBlocking {
            //GIVEN
            val email = "incorrectEmail"
            coEvery {getUserRepository.getUser(email) } returns null
            val expectedUser = null

            //WHEN
            val result = getUserDataUseCase.getUser(email)

            //THEN
            assertEquals(expectedUser, result)
        }

    @Test
    fun `given an email, when getName is called, then return user name`() =
        runBlocking {
            //GIVEN
            val email = "email"
            coEvery {getUserRepository.getUser(email) } returns User(name = "name")
            val expectedUser = User(name = "name").name

            //WHEN
            val result = getUserDataUseCase.getName(email)

            //THEN
            assertEquals(expectedUser, result)
        }

    @Test
    fun `given an incorrect email, when getName is called, then return null`() =
        runBlocking {
            //GIVEN
            val email = "incorrectEmail"
            coEvery {getUserRepository.getUser(email) } returns null
            val expectedUser = null

            //WHEN
            val result = getUserDataUseCase.getName(email)

            //THEN
            assertEquals(expectedUser, result)
        }

    @Test
    fun `given an email, when getSurname is called, then return user surname`() =
        runBlocking {
            //GIVEN
            val email = "email"
            coEvery {getUserRepository.getUser(email) } returns User(surname = "surname")
            val expectedUser = User(surname = "surname").surname

            //WHEN
            val result = getUserDataUseCase.getSurname(email)

            //THEN
            assertEquals(expectedUser, result)
        }

    @Test
    fun `given an incorrect email, when getSurname is called, then return null`() =
        runBlocking {
            //GIVEN
            val email = "incorrectEmail"
            coEvery {getUserRepository.getUser(email) } returns null
            val expectedUser = null

            //WHEN
            val result = getUserDataUseCase.getSurname(email)

            //THEN
            assertEquals(expectedUser, result)
        }

    @Test
    fun `given an email, when getPhone is called, then return user phoneNumber`() =
        runBlocking {
            //GIVEN
            val email = "email"
            coEvery {getUserRepository.getUser(email) } returns User(phone = "1234567")
            val expectedUser = User(phone = "1234567").phone

            //WHEN
            val result = getUserDataUseCase.getPhone(email)

            //THEN
            assertEquals(expectedUser, result)
        }

    @Test
    fun `given an incorrect email, when getPhone is called, then return null`() =
        runBlocking {
            //GIVEN
            val email = "incorrectEmail"
            coEvery {getUserRepository.getUser(email) } returns null
            val expectedUser = null

            //WHEN
            val result = getUserDataUseCase.getPhone(email)

            //THEN
            assertEquals(expectedUser, result)
        }

}