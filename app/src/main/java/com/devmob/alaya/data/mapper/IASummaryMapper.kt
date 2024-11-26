package com.devmob.alaya.data.mapper

import com.devmob.alaya.data.IASummaryNetworkResponse
import com.devmob.alaya.domain.IASummaryPrompt
import com.devmob.alaya.domain.model.BodySensationIAPrompt
import com.devmob.alaya.domain.model.CrisisDetailsDB
import com.devmob.alaya.domain.model.EmotionIAPrompt
import com.devmob.alaya.domain.model.TimeAndPlaceIAPrompt
import com.devmob.alaya.domain.model.ToolsIAPrompt

fun CrisisDetailsDB.toIASummaryModel(): IASummaryNetworkResponse{

    val mappedEmotions = emotions.map {
     EmotionIAPrompt(name = it.name, intensity = it.intensity.name)
    }

    val mappedBodySensations = bodySensations.map {
        BodySensationIAPrompt(name = it.name, intensity = it.intensity.name)
    }

    val mappedTools = tools.map {
        ToolsIAPrompt(name = it)
    }

    val mappedTimeAndPlace = TimeAndPlaceIAPrompt(place = place, day = start)

    return IASummaryNetworkResponse(
            additionalComments = notes?:"",
            emotions = mappedEmotions,
            sensations = mappedBodySensations,
            tools = mappedTools,
            timeAndPlace = mappedTimeAndPlace
    )
}