package com.devmob.alaya.ui.screen.searchUser

import com.devmob.alaya.data.FirebaseClient
import com.devmob.alaya.data.GetUserRepositoryImpl
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.GetUserRepository
import com.devmob.alaya.domain.model.Patient
import com.devmob.alaya.domain.model.User
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.BDDMockito.Then

class SearchUserViewModelTest {

    private lateinit var viewModel: SearchUserViewModel

    @MockK
    private lateinit var getUserDataUseCase: GetUserDataUseCase

    @MockK
    private lateinit var getUserRepository: GetUserRepository

    @MockK
    private lateinit var firebaseClient: FirebaseClient

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this,relaxed = true)
        Dispatchers.setMain(testDispatcher)
        getUserRepository = GetUserRepositoryImpl(firebaseClient)
        getUserDataUseCase = GetUserDataUseCase(getUserRepository)
        viewModel = SearchUserViewModel(getUserDataUseCase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `given there are patients, when patients are loaded, patients is updated`(){
        //GIVEN
        val patient = Patient()
        val professional = User(patients = listOf(patient))
        coEvery { getUserRepository.getUser("") } returns professional
        coEvery { getUserDataUseCase.getUser("")} returns professional
//        coEvery { firebaseClient.db.collection("users").document(professional.email).get().await() } returns professional

        //WHEN

        viewModel.loadPatients("")

        //THEN
        assertEquals(1,viewModel.patients.size)

    }

    // when there are no patients

    @Test
    fun `given there are no patients, when patients are loaded, patients is empty`(){
        //GIVEN

        val professional = User(patients = emptyList())
        coEvery { getUserRepository.getUser("") } returns professional
        coEvery { getUserDataUseCase.getUser("")} returns professional

        //WHEN

        viewModel.loadPatients("")

        //THEN
        assertEquals(emptyList<Patient>(),viewModel.patients)

    }
}