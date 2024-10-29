package com.devmob.alaya.ui.screen.login

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.data.preferences.SharedPreferences
import com.devmob.alaya.domain.GetRoleUseCase
import com.devmob.alaya.domain.LoginUseCase
import com.devmob.alaya.domain.model.AuthenticationResult
import com.devmob.alaya.domain.model.UserRole
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
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
class LoginViewmodelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel

    @MockK
    private lateinit var loginUseCase: LoginUseCase

    @MockK
    private lateinit var getRoleUseCase: GetRoleUseCase

    @MockK
    private lateinit var prefs: SharedPreferences

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel(loginUseCase, getRoleUseCase, prefs)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(loginUseCase, getRoleUseCase, prefs)
    }

    @Test
    fun `given a patient, when login is successfully, then navigate to patientHomeScreen`() {
        //GIVEN
        val email = "emailPatient"
        val password = "passwordPatient"
        coEvery { loginUseCase(email, password) } returns AuthenticationResult.Success
        coEvery { getRoleUseCase(email) } returns UserRole.PATIENT

        //WHEN
        viewModel.singInWithEmailAndPassword(email,password)

        //THEN
        assertTrue(viewModel.navigateToPatientHome.value)
    }


    @Test
    fun `given a professional, when login is successfully, then navigate to ProfessionalHomeScreen`() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        //GIVEN
        val email = "emailProfessional"
        val password = "passwordProfessional"
        coEvery { loginUseCase(email, password) } returns AuthenticationResult.Success
        coEvery { getRoleUseCase(email) } returns UserRole.PROFESSIONAL

        //WHEN
        viewModel.singInWithEmailAndPassword(email,password)

        //THEN
        assertTrue(viewModel.navigateToProfessionalHome.value)
    }

    @Test
    fun `given a incorrect login user, when tru to log in, then show error`() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        //GIVEN
        val email = "invalidEmail"
        val password = "invalidPassword"
        coEvery { loginUseCase(email, password) } returns AuthenticationResult.Error(Exception("Login failed exception"))

        //WHEN
        viewModel.singInWithEmailAndPassword(email,password)

        //THEN
        assertTrue(viewModel.showError.value)
    }

    @Test
    fun `given a patient logged in user, when check if user was already logged in, then navigate to PatientHomeScreen`() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        //GIVEN
        every { prefs.isLoggedIn() } returns true
        every { prefs.getRole() } returns UserRole.PATIENT

        //WHEN
        viewModel.checkIfUserWasLoggedIn()

        //THEN
        assertTrue(viewModel.navigateToPatientHome.value)
    }

    @Test
    fun `given a professional logged in user, when check if user was already logged in, then navigate ProfessionalHomeScreen`() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        //GIVEN
        every { prefs.isLoggedIn() } returns true
        every { prefs.getRole() } returns UserRole.PROFESSIONAL

        //WHEN
        viewModel.checkIfUserWasLoggedIn()

        //THEN
        assertTrue(viewModel.navigateToProfessionalHome.value)
    }


    @Test
    fun `given a not a logged in user, when check if user was already logged in, then not navigate and stop loading screen`() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        //GIVEN
        every { prefs.isLoggedIn() } returns false

        //WHEN
        viewModel.checkIfUserWasLoggedIn()

        //THEN
        assertFalse(viewModel.loading.value)
        assertFalse(viewModel.navigateToPatientHome.value)
        assertFalse(viewModel.navigateToProfessionalHome.value)
    }
}