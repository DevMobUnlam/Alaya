package com.devmob.alaya.ui.screen.patient_home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetInvitationUseCase
import com.devmob.alaya.domain.GetUserDataUseCase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class PatientHomeScreenViewmodelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PatientHomeScreenViewmodel

    @MockK
    private lateinit var getUserData: GetUserDataUseCase

    @MockK
    private lateinit var getInvitationUseCase: GetInvitationUseCase

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = PatientHomeScreenViewmodel(getUserData, getInvitationUseCase, firebaseClient)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given emailPatient from auth, when fetchPatient, then verify getUserData`() = runBlocking {
        // GIVEN
        val emailPatient = "emailPatient"
        coEvery { firebaseClient.auth.currentUser?.email } returns emailPatient
        coEvery { getUserData.getName(emailPatient) } returns "namePatient"

        // WHEN
        viewModel.fetchPatient()

        // THEN
        coVerify { getUserData.getName(emailPatient) }
    }
}