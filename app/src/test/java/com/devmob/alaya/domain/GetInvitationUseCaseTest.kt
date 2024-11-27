package com.devmob.alaya.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.domain.model.Invitation
import com.devmob.alaya.domain.model.InvitationStatus
import com.devmob.alaya.domain.model.Patient
import com.devmob.alaya.domain.model.Professional
import com.devmob.alaya.domain.model.User
import retrofit2.Response
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetInvitationUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getUserRepository: GetUserRepository

    @MockK
    private lateinit var notificationRepository: NotificationRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var getInvitationUseCase: GetInvitationUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        getInvitationUseCase = GetInvitationUseCase(getUserRepository, notificationRepository)
    }

    @Test
    fun `given email, when getInvitationProfessional is called, then return Invitation`() =
        runTest {
            // GIVEN
            val email = "email"
            val invitation = Invitation(email, InvitationStatus.PENDING)
            coEvery { getUserRepository.getUser(email) } returns User(invitation = invitation)

            // WHEN
            val invitationResult = getInvitationUseCase.getInvitationProfessional(email)

            // THEN
            assertEquals(invitation, invitationResult)
        }

    @Test
    fun `given email, fieldName and status, when updateInvitation is called, then call updateUserField`() =
        runTest {
            // GIVEN
            val email = "email"
            val fieldName = "fieldName"
            val status = InvitationStatus.PENDING

            // WHEN
            getInvitationUseCase.updateInvitation(email, fieldName, status)

            // THEN
            coVerify { getUserRepository.updateUserField(email, fieldName, status.name) }
        }

    @Test
    fun `given userId and professional, when addProfessional is called, then call addNewField`() =
        runTest {
            // GIVEN
            val userId = "userId"
            val professional = Professional()

            // WHEN
            getInvitationUseCase.addProfessional(userId, professional)

            // THEN
            coVerify { getUserRepository.addNewField(userId, "professional", professional) }
        }

    @Test
    fun `given userId and patient, when addPatient is called, then call updateUserField`() =
        runTest {
            // GIVEN
            val userId = "userId"
            val patient = Patient()

            // WHEN
            getInvitationUseCase.addPatient(userId, patient)

            // THEN
            coVerify { getUserRepository.updateUserField(userId, "patients", listOf(patient)) }
        }

    @Test
    fun `given patientEmail and professionalEmail, when sendInvitation is called and result is success, then call sendInvitation`() =
        runTest {
            // GIVEN
            val patientEmail = "patientEmail"
            val professionalEmail = "professionalEmail"
            val invitationForPatient = Invitation(professionalEmail, InvitationStatus.PENDING)
            val invitationForProfessional = Invitation(patientEmail, InvitationStatus.PENDING)
            coEvery {
                getUserRepository.sendInvitation(
                    invitationForPatient,
                    invitationForProfessional
                )
            } returns Result.success(Unit)

            // WHEN
            val result = getInvitationUseCase.sendInvitation(patientEmail, professionalEmail)

            // THEN
            coVerify {
                getUserRepository.sendInvitation(
                    invitationForPatient,
                    invitationForProfessional
                )
            }
            assertEquals(Result.success(Unit), result)
        }

    @Test
    fun `given patientEmail and professionalEmail, when sendInvitation is called and result is error, then return error`() =
        runTest {
            // GIVEN
            val patientEmail = "patientEmail"
            val professionalEmail = "professionalEmail"
            val invitationForPatient = Invitation(professionalEmail, InvitationStatus.PENDING)
            val invitationForProfessional = Invitation(patientEmail, InvitationStatus.PENDING)
            val exception = Throwable()
            coEvery {
                getUserRepository.sendInvitation(
                    invitationForPatient,
                    invitationForProfessional
                )
            } returns Result.failure(exception)

            // WHEN
            val result = getInvitationUseCase.sendInvitation(patientEmail, professionalEmail)

            // THEN
            coVerify {
                getUserRepository.sendInvitation(
                    invitationForPatient,
                    invitationForProfessional
                )
            }
            assertEquals(Result.failure<Unit>(exception), result)
        }

    @Test
    fun `given professionalEmail, patientEmail and status, when updateProfessionalInvitationList is called, then call updateUserField`() =
        runTest {
            // GIVEN
            val professionalEmail = "professionalEmail"
            val patientEmail = "patientEmail"
            val status = InvitationStatus.ACCEPTED
            val professional =
                User(invitations = listOf(Invitation(patientEmail, InvitationStatus.PENDING)))
            coEvery { getUserRepository.getUser(professionalEmail) } returns professional

            // WHEN
            getInvitationUseCase.updateProfessionalInvitationList(
                professionalEmail,
                patientEmail,
                status
            )

            // THEN
            coVerify {
                getUserRepository.updateUserField(
                    professionalEmail,
                    "invitations",
                    listOf(Invitation(patientEmail, status))
                )
            }
        }

    @Test
    fun `given patientEmail and professionalEmail, when sendNotification is called and result is success, then call sendNotificationInvitation`() =
        runTest {
            // GIVEN
            val patientEmail = "patientEmail"
            val responseSuccess = Response.success(Unit)
            coEvery {
                notificationRepository.sendNotificationInvitation(
                    patientEmail
                )
            } returns responseSuccess

            // WHEN
            val result = getInvitationUseCase.sendNotification(patientEmail)

            // THEN
            coVerify {
                notificationRepository.sendNotificationInvitation(
                    patientEmail
                )
            }
            assertEquals(responseSuccess, result)
        }

    @Test
    fun `given patientEmail and professionalEmail, when sendNotification is called and result is error, then return error`() =
        runTest {
            // GIVEN
            val patientEmail = "patientEmail"
            val responseBody = ResponseBody.create(null, "")
            val responseFailure = Response.error<Unit>(400, responseBody)
            coEvery {
                notificationRepository.sendNotificationInvitation(
                    patientEmail
                )
            } returns responseFailure

            // WHEN
            val result = getInvitationUseCase.sendNotification(patientEmail)

            // THEN
            coVerify {
                notificationRepository.sendNotificationInvitation(
                    patientEmail
                )
            }
            assertEquals(responseFailure, result)
        }
}
