package com.devmob.alaya.data.mapper

import com.devmob.alaya.data.IASummaryNetworkResponse
import com.devmob.alaya.domain.IASummaryPrompt
import com.devmob.alaya.domain.model.CrisisDetailsDB

fun CrisisDetailsDB.toIASummaryModel(): IASummaryNetworkResponse{
    return IASummaryNetworkResponse(
            additionalComments = this.notes?:"",
            emotions = this.emotions.map { it.name },
            sensations = this.bodySensations.map { it.name },
            place = this.place?: ""
    )
}