package com.devmob.alaya.ui.screen.patient_profile

import com.devmob.alaya.domain.model.IASummaryText

sealed interface IASummaryUIState {

    object Initial: IASummaryUIState

    object Loading: IASummaryUIState

    data class Success(
        val outputText: IASummaryText
    ): IASummaryUIState

    data class Error(
        val errorMessage: String
    ): IASummaryUIState

    object EmptyContent: IASummaryUIState
}