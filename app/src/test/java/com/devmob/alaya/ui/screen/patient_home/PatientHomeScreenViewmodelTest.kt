package com.devmob.alaya.ui.screen.patient_home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetInvitationUseCase
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.InvitationStatus
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

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

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = PatientHomeScreenViewmodel(getUserData, getInvitationUseCase, firebaseClient)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(getUserData, getInvitationUseCase, firebaseClient)
    }

    @Test
    fun `given emailPatient from auth, when fetchPatient, then verify getUserData`() {
        // GIVEN
        val emailPatient = "emailPatient"
        every { firebaseClient.auth.currentUser?.email } returns emailPatient
        coEvery { getUserData.getName(emailPatient) } returns "namePatient"

        // WHEN
        viewModel.fetchPatient()

        // THEN
        coVerify { getUserData.getName(emailPatient) }
    }

    @Test
    fun `given invitation status is PENDING, when checkProfessionalInvitation, then verify fetchProfessional`() {
        // GIVEN
        val emailPatient = "emailPatient"
        val professionalEmail = "professionalEmail"
        every { firebaseClient.auth.currentUser?.email } returns emailPatient
        coEvery { getInvitationUseCase.getInvitationProfessional(emailPatient) } returns Invitation(
            professionalEmail,
            InvitationStatus.PENDING
        )

        // WHEN
        viewModel.checkProfessionalInvitation()

        // THEN
        coVerify { getUserData.getName(any()) }
        coVerify { getUserData.getSurname(any()) }
    }

    @Test
    fun `given invitation status is ACCEPTED, when checkProfessionalInvitation, then shouldShowInvitation is false`() {
        val emailPatient = "emailPatient"
        every { firebaseClient.auth.currentUser?.email } returns emailPatient
        coEvery { getInvitationUseCase.getInvitationProfessional(emailPatient) } returns Invitation(
            "professionalEmail",
            InvitationStatus.ACCEPTED
        )

        viewModel.checkProfessionalInvitation()

        assertFalse(viewModel.shouldShowInvitation)
        coVerify(exactly = 0) { getUserData.getName(any()) }
    }

    @Test
    fun `given invitation status is REJECTED, when checkProfessionalInvitation, then shouldShowInvitation is false`() {
        val emailPatient = "emailPatient"
        every { firebaseClient.auth.currentUser?.email } returns emailPatient
        coEvery { getInvitationUseCase.getInvitationProfessional(emailPatient) } returns Invitation(
            "professionalEmail",
            InvitationStatus.REJECTED
        )

        viewModel.checkProfessionalInvitation()

        assertFalse(viewModel.shouldShowInvitation)
        coVerify(exactly = 0) { getUserData.getName(any()) }
    }

    @Test
    fun `given invitation status is NONE, when checkProfessionalInvitation, then shouldShowInvitation is false`() {
        val emailPatient = "emailPatient"
        every { firebaseClient.auth.currentUser?.email } returns emailPatient
        coEvery { getInvitationUseCase.getInvitationProfessional(emailPatient) } returns Invitation(
            "professionalEmail",
            InvitationStatus.NONE
        )

        viewModel.checkProfessionalInvitation()

        assertFalse(viewModel.shouldShowInvitation)
        coVerify(exactly = 0) { getUserData.getName(any()) }
    }
}