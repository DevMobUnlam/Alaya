package com.devmob.alaya.ui.screen

import com.devmob.alaya.viewmodel.FeedbackViewModel
import com.devmob.alaya.domain.model.FeedbackType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FeedbackViewModelTest {

    private lateinit var viewModel: FeedbackViewModel

    @Before
    fun setUp() {
        viewModel = FeedbackViewModel()
    }

    @Test
    fun `initial feedbackType should be Felicitaciones`() {
        assertEquals(FeedbackType.Felicitaciones, viewModel.feedbackType)
    }

    @Test
    fun `registerEpisode should execute without error`() {
        viewModel.registerEpisode()
    }

    @Test
    fun `registerLater should execute without error`() {
        viewModel.registerLater()
    }

    @Test
    fun `goToSupportNetwork should execute without error`() {
        viewModel.goToSupportNetwork()
    }

    @Test
    fun `goToHome should execute without error`() {
        viewModel.goToHome()
    }
}
