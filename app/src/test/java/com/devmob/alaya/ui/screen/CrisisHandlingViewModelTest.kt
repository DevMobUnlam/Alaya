package com.devmob.alaya.ui.screen

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CrisisHandlingViewModelTest {

    private lateinit var viewModel: CrisisHandlingViewModel

    @Before
    fun setUp() {
        viewModel = CrisisHandlingViewModel()
    }

    @Test
    fun `when nextStep is called, currentStepIndex is incremented`() {
        viewModel.nextStep()
        assertEquals(1, viewModel.currentStepIndex)
    }

    // TODO este test va a cambiar cuando consultemos el repositorio
    @Test
    fun `when nextStep is called on the last step, shouldShowModal is true`(){
        viewModel.currentStepIndex = 2
        viewModel.nextStep()
        assertTrue(viewModel.shouldShowModal)
    }

    @Test
    fun `when showModal is called, shouldShowModal is true`() {
        viewModel.showModal()
        assertTrue(viewModel.shouldShowModal)
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
}