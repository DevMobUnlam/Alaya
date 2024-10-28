package com.devmob.alaya.ui.screen.register

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.domain.AddUserToFirestoreUseCase
import com.devmob.alaya.domain.RegisterNewUserUseCase
import com.devmob.alaya.domain.model.AuthenticationResult
import com.devmob.alaya.domain.model.FirebaseResult
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.domain.model.UserRole
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
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
class RegisterViewmodelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var viewModel: RegisterViewmodel

    @MockK
    private lateinit var  registerNewUserUseCase: RegisterNewUserUseCase

    @MockK
    private lateinit var addUserToFirestoreUseCase: AddUserToFirestoreUseCase

    @MockK
    private lateinit var userPatient: User

    @MockK
    private lateinit var userProfessional: User


    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = RegisterViewmodel(registerNewUserUseCase, addUserToFirestoreUseCase)
        userPatient = User(name = "Pedro", surname = "Lopez", email = "mail@mail.com", role = UserRole.PATIENT)
        userProfessional = User(name = "Pedro", surname = "Lopez", email = "mail@mail.com", role = UserRole.PROFESSIONAL)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(registerNewUserUseCase,addUserToFirestoreUseCase)
        userPatient
        userProfessional
    }

    @Test
    fun `given an patient user and password, create the user and navigate`() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        //GIVEN
        val password = "NewUserPassword"
        coEvery { registerNewUserUseCase(userPatient.email, password) } returns AuthenticationResult.Success
        coEvery { addUserToFirestoreUseCase(userPatient) } returns FirebaseResult.Success

        //WHEN
        viewModel.createUser(userPatient, password)

        //THEN
        assertTrue(viewModel.navigateToPatientHome.value)
    }

    @Test
    fun `given an professional user and password, create the user and navigate`() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        //GIVEN
        val password = "NewUserPassword"
        coEvery { registerNewUserUseCase(userProfessional.email, password) } returns AuthenticationResult.Success
        coEvery { addUserToFirestoreUseCase(userProfessional) } returns FirebaseResult.Success

        //WHEN
        viewModel.createUser(userProfessional, password)

        //THEN
        assertTrue(viewModel.navigateToProfessionalHome.value)
    }

    @Test
    fun `given an invalid  user or password, show authentication error`() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        //GIVEN
        val password = "NewUserPassword"
        coEvery { registerNewUserUseCase(userPatient.email, password) } returns AuthenticationResult.Error(Exception("Authentication failed exception"))
        //coEvery { addUserToFirestoreUseCase(userPatient) } returns FirebaseResult.Success

        //WHEN
        viewModel.createUser(userPatient, password)

        //THEN
        assertTrue(viewModel.showError.value)
    }

    @Test
    fun `given an user and password but a failed connection to Firestore, then show error`() {
        mockkStatic(Log::class)
        every { Log.d(any(), any()) } returns 0
        //GIVEN
        val password = "NewUserPassword"
        coEvery { registerNewUserUseCase(userProfessional.email, password) } returns AuthenticationResult.Success
        coEvery { addUserToFirestoreUseCase(userProfessional) } returns FirebaseResult.Error(Exception("Firestore conecction failed exception"))

        //WHEN
        viewModel.createUser(userProfessional, password)

        //THEN
        assertTrue(viewModel.showError.value)
    }
}