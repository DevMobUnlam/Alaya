package com.devmob.alaya.ui.screen.patientSummary

sealed interface SummaryUIState {

    object Initial: SummaryUIState

    object Loading: SummaryUIState

    data class Success(
        val outputText: String
    ): SummaryUIState

    data class Error(
        val errorMessage: String
    ): SummaryUIState
}