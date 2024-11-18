package com.devmob.alaya.data

import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.NotificationRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class NotificationRepositoryImplTest {

    private var patientEmailMockk = "patientEmailMockk"

    private var professionalEmailMockk = "professionalEmailMockk"

    @MockK
    private lateinit var responseSuccessMockk: Response<Unit>

    @MockK
    private lateinit var responseFailureMockk: Response<Unit>

    @MockK
    private lateinit var apiClient: NotificationService

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @MockK
    private lateinit var getUserData: GetUserDataUseCase

    private lateinit var repository: NotificationRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        repository = NotificationRepositoryImpl(apiClient, firebaseClient, getUserData)
    }

    @Test
    fun `When a notification is sent successfully then it returns a response`(): Unit = runBlocking {
        every { responseSuccessMockk.isSuccessful } returns true
        val expected = responseSuccessMockk
        coEvery { apiClient.sendNotification(any()) } returns responseSuccessMockk
        val response = repository.sendNotificationInvitation(
            patientEmail = patientEmailMockk
        )
        Assert.assertEquals(expected, response)
    }

    @Test
    fun `When a notification is sent and fails then it returns a response`(): Unit = runBlocking {
        val expected = responseFailureMockk
        every { responseFailureMockk.isSuccessful } returns false
        coEvery { apiClient.sendNotification(any()) } returns responseFailureMockk
        val response = repository.sendNotificationInvitation(
            patientEmail = patientEmailMockk
        )
        Assert.assertEquals(expected, response)
    }


    @Test
    fun `When a new treatment notification is sent successfully then it returns a response`(): Unit = runBlocking {
        every { responseSuccessMockk.isSuccessful } returns true
        val expected = responseSuccessMockk
        coEvery { apiClient.sendNotification(any()) } returns responseSuccessMockk
        val response = repository.sendNotificationNewTreatment(
            patientEmail = patientEmailMockk
        )
        Assert.assertEquals(expected, response)
    }

    @Test
    fun `When a new treatment notification is sent and fails then it returns a response`(): Unit = runBlocking {
        val expected = responseFailureMockk
        every { responseFailureMockk.isSuccessful } returns false
        coEvery { apiClient.sendNotification(any()) } returns responseFailureMockk
        val response = repository.sendNotificationNewTreatment(
            patientEmail = patientEmailMockk
        )
        Assert.assertEquals(expected, response)
    }
}
