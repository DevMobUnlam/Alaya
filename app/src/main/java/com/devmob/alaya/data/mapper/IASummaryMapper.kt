package com.devmob.alaya.data.mapper

import com.devmob.alaya.domain.IASummaryNetworkResponse
import com.devmob.alaya.domain.IASummaryPrompt
import com.devmob.alaya.domain.model.CrisisDetailsDB

fun CrisisDetailsDB.toIASummaryModel(name: String): IASummaryPrompt{
    return IASummaryPrompt(
        patientName = name,
        input = IASummaryNetworkResponse(additionalComments = this.notes?:"")
    )
}