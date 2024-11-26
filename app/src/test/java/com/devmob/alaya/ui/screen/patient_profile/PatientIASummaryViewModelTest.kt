package com.devmob.alaya.ui.screen.patient_profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.devmob.alaya.domain.GetIASummaryUseCase
import com.devmob.alaya.domain.GetUserDataUseCase
import com.devmob.alaya.domain.GetUserRepository
import com.devmob.alaya.domain.model.IASummaryText
import com.devmob.alaya.domain.model.Patient
import com.devmob.alaya.domain.model.User
import com.devmob.alaya.ui.screen.searchUser.SearchUserViewModel
import com.google.firebase.FirebaseException
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.jvm.Throws

class PatientIASummaryViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: PatientIASummaryViewModel

    @MockK
    private lateinit var getIASummaryUseCase: GetIASummaryUseCase

    @MockK
    private val savedInstancedStateHandle = mockk<SavedStateHandle>(relaxed = true)

    @MockK
    private lateinit var outputContent: IASummaryText



    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this,relaxed = true)
        Dispatchers.setMain(testDispatcher)
        every { savedInstancedStateHandle.get<String>("patientID") } returns "email@test.com"
        viewModel = PatientIASummaryViewModel(savedInstancedStateHandle,getIASummaryUseCase)
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
    fun `when summarize is called state is Loading state`() = runBlocking{

        //WHEN

        viewModel.onRetryClick()

        //THEN
        assertTrue(viewModel.uiState.value is IASummaryUIState.Loading)

    }

    @Test
    fun `when content is returned, then summarize emits success`() = runBlocking{

        //GIVEN

        coEvery { getIASummaryUseCase(any<String>())
        } returns flow{emit(IASummaryUIState.Success(outputContent))}


        //WHEN

        viewModel.onRetryClick()

        //THEN
        assertEquals(IASummaryUIState.Success(outputContent),viewModel.uiState.value)
    }


    @Test
    fun `when there are no registers, then success state is empty`() = runBlocking{

        //GIVEN

        coEvery { getIASummaryUseCase(any<String>())
        } returns flow{emit(IASummaryUIState.EmptyContent)}
        // WHEN

        viewModel.onRetryClick()

        //THEN

        assert(viewModel.uiState.value is IASummaryUIState.EmptyContent)
    }

    @Test
    fun `when exception is thrown, uiState is Error`() = runBlocking{

        //GIVEN

        val exceptionMessage = "This is exception message"
        val exception = Exception(exceptionMessage)

        coEvery{getIASummaryUseCase(any<String>())
        } throws exception

        //WHEN

        viewModel.onRetryClick()

        //THEN

        assert(viewModel.uiState.value is IASummaryUIState.Error)

    }

}