package com.devmob.alaya.ui.screen.send_invitation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetInvitationUseCase
import com.devmob.alaya.ui.screen.send_invitation_screen.SendInvitationViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
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
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SendInvitationViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SendInvitationViewModel

    @MockK
    private lateinit var getInvitationUseCase: GetInvitationUseCase

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = SendInvitationViewModel(getInvitationUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(getInvitationUseCase, firebaseClient)
    }

    @Test
    fun `when sendInvitation is called, then verify sendInvitationUseCase`(): Unit = runBlocking {
        // GIVEN
        val patientEmail = "patient@example.com"
        val professionalEmail = "professional@example.com"
        val result = Result.success(Unit)
        coEvery {
            getInvitationUseCase.sendInvitation(
                patientEmail,
                professionalEmail
            )
        } returns result

        val observer = Observer<Result<Unit>> { }

        // WHEN
        viewModel.sendInvitation(patientEmail, professionalEmail)

        viewModel.sendInvitationStatus.observeForever(observer)

        // THEN
        coVerify { getInvitationUseCase.sendInvitation(patientEmail, professionalEmail) }
        assertEquals(
            result,
            viewModel.sendInvitationStatus.value
        )

        viewModel.sendInvitationStatus.removeObserver(observer)
    }
}