package com.devmob.alaya.ui.screen.professionalHome

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.model.Patient
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class ProfessionalHomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var getUserData: GetUserDataUseCase

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: ProfessionalHomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        every { firebaseClient.auth.currentUser?.email } returns "test@gmail.com"
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(getUserData, firebaseClient)
    }

    @Test
    fun `given emailProfessional from auth, when init viewModel, then verify that getUserData is called`() {
        // GIVEN
        coEvery { getUserData.getName("test@gmail.com") } returns "Test User"

        // WHEN
        viewModel = ProfessionalHomeViewModel(getUserData, firebaseClient)

        // THEN
        coVerify { getUserData.getName("test@gmail.com") }
        assertEquals("Test User", viewModel.nameProfessional)
        assertFalse(viewModel.isLoading)
    }

    @Test
    fun `given emailProfessional, when loadPatients and they have session today, then patients is not empty`() {
        // GIVEN
        val mockkPatient =
            Patient("email", "name", "surname", "phone", Date(), "nextSessionTime")
        coEvery { getUserData.getUser("test@gmail.com")!!.patients } returns List(2) { mockkPatient }

        // WHEN
        viewModel = ProfessionalHomeViewModel(getUserData, firebaseClient)

        // THEN
        coVerify { getUserData.getUser("test@gmail.com") }
        assertEquals(2, viewModel.patients.size)
    }

/*    @Test
    fun `given emailProfessional, when loadPatients but they don't have session today, then patients is empty`() {
        // GIVEN
        val anyDate = "01/01/2024"
        val mockkPatient =
            Patient("email", "name", "surname", "phone", Date(), "nextSessionTime")
        coEvery { getUserData.getUser("test@gmail.com")!!.patients } returns List(2) { mockkPatient }

        // WHEN
        viewModel = ProfessionalHomeViewModel(getUserData, firebaseClient)

        // THEN
        coVerify { getUserData.getUser("test@gmail.com") }
        assertEquals(0, viewModel.patients.size)
    }*/

    @Test
    fun `given emailProfessional, when init viewModel, then greetingMessage is not empty`() {
        // GIVEN
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val greetingMessage = when (hourOfDay) {
            in 5..11 -> "Buenos días"
            in 12..19 -> "Buenas tardes"
            else -> "Buenas noches"
        }

        // WHEN
        viewModel = ProfessionalHomeViewModel(getUserData, firebaseClient)

        // THEN
        assertEquals(greetingMessage, viewModel.greetingMessage)
    }
}
