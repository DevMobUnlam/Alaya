package com.devmob.alaya.ui.screen.feedback

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
    fun `setFeedbackType should update feedbackType`() {
        viewModel.setFeedbackType(FeedbackType.TodoVaAEstarBien)
        assertEquals(FeedbackType.TodoVaAEstarBien, viewModel.feedbackType)
    }



}
