package com.devmob.alaya.ui.screen.patient_profile

sealed interface IASummaryUIState {

    object Initial: IASummaryUIState

    object Loading: IASummaryUIState

    data class Success(
        val outputText: String
    ): IASummaryUIState

    data class Error(
        val errorMessage: String
    ): IASummaryUIState
}