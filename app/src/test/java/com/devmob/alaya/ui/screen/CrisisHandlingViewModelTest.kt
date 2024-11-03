package com.devmob.alaya.ui.screen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devmob.alaya.domain.SaveCrisisRegistrationUseCase
import com.devmob.alaya.ui.screen.crisis_handling.CrisisHandlingViewModel
import io.mockk.MockKAnnotations
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CrisisHandlingViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CrisisHandlingViewModel

    @MockK
    private lateinit var saveCrisisRegistrationUseCase: SaveCrisisRegistrationUseCase

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        Dispatchers.setMain(testDispatcher)
        viewModel = CrisisHandlingViewModel(saveCrisisRegistrationUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearMocks(saveCrisisRegistrationUseCase)
    }

    @Test
    fun `when nextStep is called, currentStepIndex is incremented`() {
        viewModel.nextStep()
        assertEquals(1, viewModel.currentStepIndex)
    }

    // TODO este test va a cambiar cuando consultemos el repositorio
    @Test
    fun `when nextStep is called on the last step, shouldShowModal is true and saveCrisisData is called`() {
        viewModel.currentStepIndex = 2
        viewModel.nextStep()
        assertTrue(viewModel.shouldShowModal)
        coVerify { saveCrisisRegistrationUseCase.invoke(any()) }
    }


    @Test
    fun `when showModal is called, shouldShowModal is true and tool is added`() {
        viewModel.showModal()
        assertTrue(viewModel.shouldShowModal)
        assertEquals(1, viewModel.toolsUsed.size)
    }

    @Test
    fun `when dismissModal is called, shouldShowModal is false`() {
        viewModel.dismissModal()
        assertFalse(viewModel.shouldShowModal)
    }

    @Test
    fun `when showExitModal is called, shouldShowExitModal is true`() {
        viewModel.showExitModal()
        assertTrue(viewModel.shouldShowExitModal)
    }

    @Test
    fun `when dismissExitModal is called, shouldShowExitModal is false`() {
        viewModel.dismissExitModal()
        assertFalse(viewModel.shouldShowExitModal)
    }

    @Test
    fun `when saveCrisisData is called, SaveCrisisRegistrationUseCase is invoked`() {
        viewModel.saveCrisisData()
        coVerify { saveCrisisRegistrationUseCase.invoke(any()) }
    }
}