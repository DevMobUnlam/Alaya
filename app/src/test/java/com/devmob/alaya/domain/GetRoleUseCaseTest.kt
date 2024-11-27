package com.devmob.alaya.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.model.UserRole
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain

import com.devmob.alaya.domain.model.User
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetRoleUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getUserRepository: GetUserRepository


    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var getRoleUseCase: GetRoleUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        getRoleUseCase = GetRoleUseCase(getUserRepository)
    }

    @Test
    fun `given a patient email, when getRoleUseCase is called, then return patient UseRole`() =
        runBlocking {
            //GIVEN
            val patientEmail = "patientEmail"
            coEvery { getUserRepository.getUser(patientEmail) } returns User(role = UserRole.PATIENT)

            //WHEN
            val result = getRoleUseCase(patientEmail)

            //THEN
            assertEquals(UserRole.PATIENT, result)
        }

    @Test
    fun `given a professional email, when getRoleUseCase is called, then return patient UseRole`() =
        runBlocking {
            //GIVEN
            val professionalEmail = "professionalEmail"
            coEvery { getUserRepository.getUser(professionalEmail) } returns User(role = UserRole.PROFESSIONAL)

            //WHEN
            val result = getRoleUseCase(professionalEmail)

            //THEN
            assertEquals(UserRole.PROFESSIONAL, result)
        }

    @Test
    fun `given an incorrect email, when getRoleUseCase is called, then return null`() =
        runBlocking {
            //GIVEN
            val incorrectEmail = "incorrectEmail"
            coEvery { getUserRepository.getUser(incorrectEmail) } returns null

            //WHEN
            val result = getRoleUseCase(incorrectEmail)

            //THEN
            assertEquals(null, result)
        }

    @Test
    fun `given a NONE role email, when getRoleUseCase is called, then return UserRole NONE`() =
        runBlocking {
            //GIVEN
            val email = "email"
            coEvery { getUserRepository.getUser(email) } returns User()

            //WHEN
            val result = getRoleUseCase(email)

            //THEN
            assertEquals(UserRole.NONE, result)
        }
}