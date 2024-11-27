package com.devmob.alaya.ui.screen.searchUser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.GetUserRepository
import com.devmob.alaya.domain.model.Patient
import com.devmob.alaya.domain.model.User
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.mockkStatic
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchUserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SearchUserViewModel

    @MockK
    private lateinit var getUserDataUseCase: GetUserDataUseCase

    @MockK
    private lateinit var getUserRepository: GetUserRepository


    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this,relaxed = true)
        Dispatchers.setMain(testDispatcher)
        getUserDataUseCase = GetUserDataUseCase(getUserRepository)
        viewModel = SearchUserViewModel(getUserDataUseCase)
        mockkStatic("kotlinx.coroutines.tasks.TasksKt")

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `given there are patients, when patients are loaded, patients is updated`() = runBlocking{

        //GIVEN
        val patient = Patient()
        val professional = User(email = "email@test.com",patients = listOf(patient))
        coEvery { getUserDataUseCase.getUser(professional.email)} returns professional

        //WHEN
        viewModel.loadPatients(professional.email)

        //THEN
        Assert.assertEquals(1,viewModel.patients.size)

    }


    @Test
    fun `given there are no patients, when patients are loaded, patients is empty`() = runBlocking{

        //GIVEN

        val professional = User(email = "email@test.com")
        coEvery { getUserDataUseCase.getUser(professional.email)} returns professional

        //WHEN

        viewModel.loadPatients(professional.email)

        //THEN
        assertEquals(emptyList<Patient>(),viewModel.patients)

    }
}