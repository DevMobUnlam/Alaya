package com.devmob.alaya.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AddUserToFireStoreUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var userFireStoreRepository: UserFirestoreRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var addUserToFireStoreUseCase: AddUserToFirestoreUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        addUserToFireStoreUseCase = AddUserToFirestoreUseCase(userFireStoreRepository)
    }

    @Test
    fun `given a user, when invoke is called, then addUser is called and result is returned`() =
        runTest {
            // GIVEN
            val user = User("1", "name", "email", "photoUrl")
            val expectedResult = FirebaseResult.Success
            coEvery { userFireStoreRepository.addUser(user) } returns expectedResult

            // WHEN
            val result = addUserToFireStoreUseCase.invoke(user)

            // THEN
            coVerify { userFireStoreRepository.addUser(user) }
            assertEquals(expectedResult, result)
        }

    @Test
    fun `given a repository error, when invoke is called, then result is returned`() = runTest {
        // GIVEN
        val user = User("1", "name", "email", "photoUrl")
        val expectedResult = FirebaseResult.Error(Throwable("Network error"))
        coEvery { userFireStoreRepository.addUser(user) } returns expectedResult

        // WHEN
        val result = addUserToFireStoreUseCase.invoke(user)

        // THEN
        coVerify { userFireStoreRepository.addUser(user) }
        assertEquals(expectedResult, result)
    }
}
